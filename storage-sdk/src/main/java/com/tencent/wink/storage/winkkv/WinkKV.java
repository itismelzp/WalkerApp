package com.tencent.wink.storage.winkkv;

import androidx.annotation.NonNull;


import com.tencent.wink.storage.winkkv.encoder.EncoderManager;
import com.tencent.wink.storage.winkkv.encoder.StringSetEncoder;
import com.tencent.wink.storage.winkkv.log.WinkKVLog;
import com.tencent.wink.storage.winkkv.type.DataType;
import com.tencent.wink.storage.winkkv.type.MaskType;
import com.tencent.wink.storage.winkkv.type.WritingModeType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WinkKV外部接口
 * <p>
 * Created by walkerzpli on 2021/10/15.
 */
@SuppressWarnings("rawtypes")
public class WinkKV {

    private static final String TAG = "WinkKV";

    private static final String BOTH_FILES_ERROR = "both files error";
    private static final String PARSE_DATA_FAILED = "parse data failed";
    private static final String OPEN_FILE_FAILED = "open file failed";
    private static final String MAP_FAILED = "map failed";

    static final String TRUNCATE_FINISH = "truncate finish";
    static final String GC_FINISH = "gc finish";

    private static final String A_SUFFIX = ".kva";
    private static final String B_SUFFIX = ".kvb";
    private static final String C_SUFFIX = ".kvc";
    private static final String TEMP_SUFFIX = ".tmp";

    private static final int DATA_SIZE_LIMIT = 1 << 29;

    private static final int[] TYPE_SIZE = {0, 1, 4, 4, 8, 8};
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final int DATA_START = 12;
    private static final int BASE_GC_KEYS_THRESHOLD = 80;
    private static final int BASE_GC_BYTES_THRESHOLD = 4096;
    private static final int INTERNAL_LIMIT = 2048;

    private static final int PAGE_SIZE = Util.getPageSize();
    private static final int DOUBLE_LIMIT = Math.max(PAGE_SIZE << 1, 1 << 14);
    private static final int TRUNCATE_THRESHOLD = DOUBLE_LIMIT << 1;

    private final String path;
    private final String name;
    private final Map<String, Encoder<?>> encoderMap;
    private final WinkKVLog logger = WinkKVConfig.mLogger;

    private FileChannel aChannel;
    private FileChannel bChannel;
    private MappedByteBuffer aBuffer;
    private MappedByteBuffer bBuffer;
    private WinkBuffer winkBuffer;

    private int dataEnd;
    private long checksum;
    private final Map<String, Container.BaseContainer> data = new HashMap<>();
    private boolean startLoading = false;

    private int updateStart;
    private int updateSize;
    private int removeStart;
    private boolean sizeChanged;

    private String tempExternalName;

    private int invalidBytes;
    private final ArrayList<Segment> invalids = new ArrayList<>();

    // 1）默认写入模式为mmap；
    // 2）如果mmap api异常，则降级为常规的blocking I/O；
    // 3）WinkKV.Builder可以指定写入模式。
    private int writingMode;

    // Only take effect when mode is not NON_BLOCKING
    private boolean autoCommit = true;

    public WinkKV(final String path, final String name, @WritingModeType int writingMode) {
        this(path, name, EncoderManager.g().getEncoderList(), writingMode);
    }

    public WinkKV(final String path, final String name, List<Encoder<?>> encoders, @WritingModeType int writingMode) {
        this.path = path;
        this.name = name;
        this.writingMode = writingMode;
        if (encoders == null) {
            encoders = new ArrayList<>();
        }
        encoders.addAll(EncoderManager.g().getEncoderList());
        Map<String, Encoder<?>> map = new HashMap<>();
        StringSetEncoder encoder = StringSetEncoder.INSTANCE;
        map.put(encoder.tag(), encoder);
        if (encoders.size() > 0) {
            for (Encoder e : encoders) {
                String tag = e.tag();
                if (map.containsKey(tag)) {
                    error("duplicate encoder tag:" + tag);
                } else {
                    map.put(tag, e);
                }
            }
        }
        this.encoderMap = map;

        synchronized (data) {
            WinkKVConfig.getExecutor().execute(this::loadData);
            while (!startLoading) {
                try {
                    // wait util loadData() get the object lock
                    data.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private synchronized void loadData() {
        // we got the object lock, notify the waiter to continue the constructor
        synchronized (data) {
            startLoading = true;
            data.notify();
        }
        long start = System.nanoTime();
        boolean hadWriteToABFile = loadFromCFile();
        if (!hadWriteToABFile && writingMode == WritingModeType.NON_BLOCKING) {
            loadFromABFile();
        }
        if (logger != null) {
            long t = (System.nanoTime() - start) / 1000000;
            info("loading finish, data len:" + dataEnd + ", get keys:" + data.size() + ", use time:" + t + " ms");
        }
    }

    private void loadFromABFile() {
        File aFile = new File(path, name + A_SUFFIX);
        File bFile = new File(path, name + B_SUFFIX);
        try {
            if (!Util.makeFileIfNotExist(aFile) || !Util.makeFileIfNotExist(bFile)) {
                throw new Exception(OPEN_FILE_FAILED);
            }
            RandomAccessFile aAccessFile = new RandomAccessFile(aFile, "rw");
            RandomAccessFile bAccessFile = new RandomAccessFile(bFile, "rw");
            long aFileLen = aAccessFile.length();
            long bFileLen = bAccessFile.length();
            aChannel = aAccessFile.getChannel();
            bChannel = bAccessFile.getChannel();
            aBuffer = aChannel.map(FileChannel.MapMode.READ_WRITE, 0, aFileLen > 0 ? aFileLen : PAGE_SIZE);
            aBuffer.order(ByteOrder.LITTLE_ENDIAN);
            bBuffer = bChannel.map(FileChannel.MapMode.READ_WRITE, 0, bFileLen > 0 ? bFileLen : PAGE_SIZE);
            bBuffer.order(ByteOrder.LITTLE_ENDIAN);
            winkBuffer = new WinkBuffer(aBuffer.capacity());

            if (aFileLen == 0 && bFileLen == 0) {
                dataEnd = DATA_START;
            } else {
                int aDataSize = aBuffer.getInt();
                long aCheckSum = aBuffer.getLong();
                int bDataSize = bBuffer.getInt();
                long bCheckSum = bBuffer.getLong();

                boolean isAValid = false;
                if (aDataSize >= 0 && (aDataSize <= aFileLen - DATA_START)) {
                    dataEnd = DATA_START + aDataSize;
                    loadBytes(aBuffer, winkBuffer, dataEnd);
                    if (aCheckSum == winkBuffer.getChecksum(DATA_START, aDataSize) && parseData() == 0) {
                        checksum = aCheckSum;
                        isAValid = true;
                    }
                }
                if (isAValid) {
                    if (aFileLen != bFileLen || !isABFileEqual()) {
                        warning(new Exception("B file error"));
                        copyBuffer(aBuffer, bBuffer, dataEnd);
                    }
                } else {
                    boolean isBValid = false;
                    if (bDataSize >= 0 && (bDataSize <= bFileLen - DATA_START)) {
                        data.clear();
                        clearInvalid();
                        dataEnd = DATA_START + bDataSize;
                        if (winkBuffer.hb.length != bBuffer.capacity()) {
                            winkBuffer = new WinkBuffer(bBuffer.capacity());
                        }
                        loadBytes(bBuffer, winkBuffer, dataEnd);
                        if (bCheckSum == winkBuffer.getChecksum(DATA_START, bDataSize) && parseData() == 0) {
                            warning(new Exception("A file error"));
                            copyBuffer(bBuffer, aBuffer, dataEnd);
                            checksum = bCheckSum;
                            isBValid = true;
                        }
                    }
                    if (!isBValid) {
                        error(BOTH_FILES_ERROR);
                        resetData();
                    }
                }
            }
        } catch (Exception e) {
            error(e);
            toResetBlockMode();
        }
    }

    private boolean isABFileEqual() {
        WinkBuffer tempBuffer = new WinkBuffer(dataEnd);
        loadBytes(bBuffer, tempBuffer, dataEnd);
        byte[] a = winkBuffer.hb;
        byte[] b = tempBuffer.hb;
        for (int i = 0; i < dataEnd; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private void loadBytes(MappedByteBuffer byteBuf, WinkBuffer buffer, int size) {
        byteBuf.rewind();
        byteBuf.get(buffer.hb, 0, size);
    }

    private boolean loadFromCFile() {
        boolean hadWriteToABFile = false;
        File cFile = new File(path, name + C_SUFFIX);
        File tmpFile = new File(path, name + TEMP_SUFFIX);
        try {
            File srcFile = null;
            if (cFile.exists()) {
                srcFile = cFile;
            } else if (tmpFile.exists()) {
                srcFile = tmpFile;
            }
            if (srcFile != null) {
                long fileLen = srcFile.length();
                if (fileLen == 0 || fileLen > DATA_SIZE_LIMIT) {
                    deleteCFiles();
                    return false;
                }
                int fileSize = (int) fileLen;
                int capacity = getNewCapacity(PAGE_SIZE, fileSize);
                byte[] bytes = new byte[capacity];
                Util.readBytes(srcFile, bytes, fileSize);
                winkBuffer = new WinkBuffer(bytes);
                WinkBuffer buffer = winkBuffer;
                int dataSize = buffer.getInt(); // data_len
                long sum = buffer.getLong(); // checksum
                dataEnd = DATA_START + dataSize;
                if (dataSize >= 0 && (dataSize <= fileSize - DATA_START)
                        && sum == buffer.getChecksum(DATA_START, dataSize)
                        && parseData() == 0) {
                    checksum = sum;
                    if (writingMode == WritingModeType.NON_BLOCKING) {
                        if (writeToABFile(buffer)) {
                            info("recover from c file");
                            hadWriteToABFile = true;
                        } else {
                            writingMode = WritingModeType.ASYNC_BLOCKING;
                        }
                    }
                } else {
                    clearData();
                }
                if (writingMode == WritingModeType.NON_BLOCKING) {
                    deleteCFiles();
                }
            }
        } catch (Exception e) {
            error(e);
            deleteCFiles();
        }
        return hadWriteToABFile;
    }

    private boolean writeToABFile(WinkBuffer buffer) {
        int fileLen = buffer.hb.length;
        File aFile = new File(path, name + A_SUFFIX);
        File bFile = new File(path, name + B_SUFFIX);
        try {
            if (!Util.makeFileIfNotExist(aFile) || !Util.makeFileIfNotExist(bFile)) {
                throw new Exception(OPEN_FILE_FAILED);
            }
            RandomAccessFile aAccessFile = new RandomAccessFile(aFile, "rw");
            RandomAccessFile bAccessFile = new RandomAccessFile(bFile, "rw");
            aAccessFile.setLength(fileLen);
            bAccessFile.setLength(fileLen);
            aChannel = aAccessFile.getChannel();
            bChannel = bAccessFile.getChannel();
            aBuffer = aChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileLen);
            aBuffer.order(ByteOrder.LITTLE_ENDIAN);
            bBuffer = bChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileLen);
            bBuffer.order(ByteOrder.LITTLE_ENDIAN);
            aBuffer.put(buffer.hb, 0, dataEnd);
            bBuffer.put(buffer.hb, 0, dataEnd);
            return true;
        } catch (Exception e) {
            error(e);
        }
        return false;
    }

    private void toResetBlockMode() {
        degrade();
        clearData();
        if (winkBuffer == null || winkBuffer.hb.length != PAGE_SIZE) {
            winkBuffer = new WinkBuffer(PAGE_SIZE);
        } else {
            winkBuffer.putInt(0, 0);
            winkBuffer.putLong(4, 0L);
        }
    }

    private void degrade() {
        writingMode = WritingModeType.ASYNC_BLOCKING;
        aChannel = null;
        bChannel = null;
        aBuffer = null;
        bBuffer = null;
    }

    private void checkValueSize(int size, boolean external) {
        if (external) {
            if (size != Util.NAME_SIZE) {
                throw new IllegalStateException("name size not match");
            }
        } else {
            if (size < 0 || size >= INTERNAL_LIMIT) {
                throw new IllegalStateException("value size out of bound");
            }
        }
    }

    private void copyBuffer(MappedByteBuffer src, MappedByteBuffer des, int end) {
        if (src.capacity() != des.capacity()) {
            try {
                FileChannel channel = (des == bBuffer) ? bChannel : aChannel;
                MappedByteBuffer newBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, src.capacity());
                newBuffer.order(ByteOrder.LITTLE_ENDIAN);
                if (des == bBuffer) {
                    bBuffer = newBuffer;
                } else {
                    aBuffer = newBuffer;
                }
                des = newBuffer;
            } catch (IOException e) {
                error(e);
                degrade();
                return;
            }
        }
        src.rewind();
        des.rewind();
        src.limit(end);
        des.put(src);
        src.limit(src.capacity());
    }

    private int parseData() {
        WinkBuffer buffer = winkBuffer;
        buffer.position = DATA_START;
        try {
            while (buffer.position < dataEnd) {
                int start = buffer.position;
                byte typeInfo = buffer.get(); // see @MaskType
                byte type = (byte) (typeInfo & MaskType.MASK_TYPE_DATA_TYPE);
                if (type < DataType.BOOLEAN || type > DataType.OBJECT) {
                    throw new Exception(PARSE_DATA_FAILED);
                }
                int keySize = buffer.get() & 0xFF;
                if (typeInfo < 0) {
                    buffer.position += keySize;
                    int valueSize = (type <= DataType.DOUBLE) ? TYPE_SIZE[type] : buffer.getShort() & 0xFFFF;
                    winkBuffer.position += valueSize;
                    countInvalid(start, winkBuffer.position);
                    continue;
                }
                String key = buffer.getString(keySize);
                int pos = buffer.position;
                if (type <= DataType.DOUBLE) { // 基础类型
                    switch (type) {
                        case DataType.BOOLEAN:
                            data.put(key, new Container.BooleanContainer(pos, buffer.get() == 1));
                            break;
                        case DataType.INT:
                            data.put(key, new Container.IntContainer(pos, buffer.getInt()));
                            break;
                        case DataType.LONG:
                            data.put(key, new Container.LongContainer(pos, buffer.getLong()));
                            break;
                        case DataType.FLOAT:
                            data.put(key, new Container.FloatContainer(pos, buffer.getFloat()));
                            break;
                        default:
                            data.put(key, new Container.DoubleContainer(pos, buffer.getDouble()));
                            break;
                    }
                } else { // 非基础类型value部分分为长度和实现value部分
                    int valueSize = buffer.getShort() & 0xFFFF;
                    boolean external = (typeInfo & MaskType.MASK_TYPE_EXTERNAL) != 0;
                    checkValueSize(valueSize, external);
                    switch (type) {
                        case DataType.STRING:
                            String str = buffer.getString(valueSize);
                            data.put(key, new Container.StringContainer(start, pos + 2, str, valueSize, external));
                            break;
                        case DataType.ARRAY:
                            Object value = external ? buffer.getString(valueSize) : buffer.getBytes(valueSize);
                            data.put(key, new Container.ArrayContainer(start, pos + 2, value, valueSize, external));
                            break;
                        default:
                            if (external) {
                                String fileName = buffer.getString(valueSize);
                                data.put(key, new Container.ObjectContainer(start, pos + 2, fileName, valueSize, true));
                            } else {
                                int tagSize = buffer.get() & 0xFF;
                                String tag = buffer.getString(tagSize);
                                Encoder encoder = encoderMap.get(tag);
                                int objectSize = valueSize - (tagSize + 1);
                                if (objectSize < 0) {
                                    throw new Exception(PARSE_DATA_FAILED);
                                }
                                if (encoder != null) {
                                    try {
                                        Object obj = encoder.decode(buffer.hb, buffer.position, objectSize);
                                        if (obj != null) {
                                            data.put(key, new Container.ObjectContainer(start, pos + 2, obj, valueSize, false));
                                        }
                                    } catch (Exception e) {
                                        error(e);
                                    }
                                } else {
                                    error("object with tag: " + tag + " without encoder");
                                }
                                buffer.position += objectSize;
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            error(e);
            return -1;
        }
        if (buffer.position != dataEnd) {
            warning(new Exception(PARSE_DATA_FAILED));
            return -1;
        }
        return 0;
    }

    /**
     * 是否包括key
     *
     * @param key 键
     * @return 是否包括
     */
    public synchronized boolean contains(String key) {
        return data.containsKey(key);
    }

    /**
     * 根据key，获取boolean型数据，默认返回false
     * @param key 键
     * @return 返回boolean值
     */
    public synchronized boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 根据key，获取boolean型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回boolean值
     */
    public synchronized boolean getBoolean(String key, boolean defValue) {
        Container.BooleanContainer c = (Container.BooleanContainer) data.get(key);
        return c == null ? defValue : c.value;
    }

    /**
     * 根据key，获取int型数据，默认返回0
     * @param key 键
     * @return 返回int值
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 根据key，获取int型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回int值
     */
    public synchronized int getInt(String key, int defValue) {
        Container.IntContainer c = (Container.IntContainer) data.get(key);
        return c == null ? defValue : c.value;
    }

    /**
     * 根据key，获取float型数据，默认返回0f
     * @param key 键
     * @return 返回float值
     */
    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    /**
     * 根据key，获取float型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回float值
     */
    public synchronized float getFloat(String key, float defValue) {
        Container.FloatContainer c = (Container.FloatContainer) data.get(key);
        return c == null ? defValue : c.value;
    }

    /**
     * 根据key，获取long型数据，默认返回0L
     * @param key 键
     * @return 返回long值
     */
    public synchronized long getLong(String key) {
        Container.LongContainer c = (Container.LongContainer) data.get(key);
        return c == null ? 0L : c.value;
    }

    /**
     * 根据key，获取long型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回long值
     */
    public synchronized long getLong(String key, long defValue) {
        Container.LongContainer c = (Container.LongContainer) data.get(key);
        return c == null ? defValue : c.value;
    }

    /**
     * 根据key，获取double型数据，默认返回0D
     * @param key 键
     * @return 返回double值
     */
    public double getDouble(String key) {
        return getDouble(key, 0D);
    }

    /**
     * 根据key，获取double型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回double值
     */
    public synchronized double getDouble(String key, double defValue) {
        Container.DoubleContainer c = (Container.DoubleContainer) data.get(key);
        return c == null ? defValue : c.value;
    }

    /**
     * 根据key，获取string型数据，默认返回""
     * @param key 键
     * @return 返回string值
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * 根据key，获取string型数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回string值
     */
    public synchronized String getString(String key, String defValue) {
        Container.StringContainer c = (Container.StringContainer) data.get(key);
        if (c != null) {
            return c.external ? getStringFromFile(c) : (String) c.value;
        }
        return defValue;
    }

    private String getStringFromFile(Container.StringContainer c) {
        String fileName = (String) c.value;
        File file = new File(path + name, fileName);
        try {
            byte[] bytes = Util.getBytes(file);
            if (bytes != null) {
                return (bytes.length == 0) ? "" : new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            error(e);
        }
        return "";
    }

    /**
     * 根据key，获取二进制数据
     * @param key 键
     * @return 返回二进制值，默认返回空byte数组
     */
    public byte[] getArray(String key) {
        return getArray(key, EMPTY_ARRAY);
    }

    /**
     * 根据key，获取二进制数据
     * @param key 键
     * @param defValue 默认返回值
     * @return 返回二进制值
     */
    public synchronized byte[] getArray(String key, byte[] defValue) {
        Container.ArrayContainer c = (Container.ArrayContainer) data.get(key);
        if (c != null) {
            return c.external ? getArrayFromFile(c) : (byte[]) c.value;
        }
        return defValue;
    }

    private byte[] getArrayFromFile(Container.ArrayContainer c) {
        File file = new File(path + name, (String) c.value);
        try {
            byte[] a = Util.getBytes(file);
            return a != null ? a : EMPTY_ARRAY;
        } catch (Exception e) {
            error(e);
        }
        return EMPTY_ARRAY;
    }

    /**
     * 根据key，获取对象类型数据
     * @param key 键
     * @param <T> 对象类型
     * @return 返回对象值
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T getObject(String key) {
        Container.ObjectContainer c = (Container.ObjectContainer) data.get(key);
        if (c != null) {
            return c.external ? (T) getObjectFromFile(c) : (T) c.value;
        }
        return null;
    }

    private Object getObjectFromFile(Container.ObjectContainer c) {
        File file = new File(path + name, (String) c.value);
        try {
            byte[] bytes = Util.getBytes(file);
            if (bytes != null) {
                int tagSize = bytes[0] & 0xFF;
                String tag = new String(bytes, 1, tagSize, StandardCharsets.UTF_8);
                Encoder encoder = encoderMap.get(tag);
                if (encoder != null) {
                    int offset = 1 + tagSize;
                    return encoder.decode(bytes, offset, bytes.length - offset);
                } else {
                    warning(new Exception("No encoder for tag:" + tag));
                }
            } else {
                warning(new Exception("Read object data failed"));
            }
        } catch (Exception e) {
            error(e);
        }
        return null;
    }

    /**
     * 根据key，获取Set<String>类型数据
     *
     * @param key 键
     * @return 返回Set<String>值
     */
    public synchronized Set<String> getStringSet(String key) {
        return getObject(key);
    }

    /**
     * 保存boolean型数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putBoolean(String key, boolean value) {
        checkKey(key);
        Container.BooleanContainer c = (Container.BooleanContainer) data.get(key);
        if (c == null) {
            wrapHeader(key, DataType.BOOLEAN);
            int offset = winkBuffer.position;
            winkBuffer.put((byte) (value ? 1 : 0));
            updateChange();
            data.put(key, new Container.BooleanContainer(offset, value));
            checkIfCommit();
        } else if (c.value != value) {
            c.value = value;
            updateBoolean((byte) (value ? 1 : 0), c.offset);
            checkIfCommit();
        }
    }

    /**
     * 保存int型数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putInt(String key, int value) {
        checkKey(key);
        Container.IntContainer c = (Container.IntContainer) data.get(key);
        if (c == null) {
            wrapHeader(key, DataType.INT);
            int offset = winkBuffer.position;
            winkBuffer.putInt(value);
            updateChange();
            data.put(key, new Container.IntContainer(offset, value));
            checkIfCommit();
        } else if (c.value != value) {
            long sum = (value ^ c.value) & 0xFFFFFFFFL;
            c.value = value;
            updateInt32(value, sum, c.offset);
            checkIfCommit();
        }
    }

    /**
     * 保存float型数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putFloat(String key, float value) {
        checkKey(key);
        Container.FloatContainer c = (Container.FloatContainer) data.get(key);
        if (c == null) {
            wrapHeader(key, DataType.FLOAT);
            int offset = winkBuffer.position;
            winkBuffer.putInt(Float.floatToRawIntBits(value));
            updateChange();
            data.put(key, new Container.FloatContainer(offset, value));
            checkIfCommit();
        } else if (c.value != value) {
            int newValue = Float.floatToRawIntBits(value);
            long sum = (Float.floatToRawIntBits(c.value) ^ newValue) & 0xFFFFFFFFL;
            c.value = value;
            updateInt32(newValue, sum, c.offset);
            checkIfCommit();
        }
    }

    /**
     * 保存long型数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putLong(String key, long value) {
        checkKey(key);
        Container.LongContainer c = (Container.LongContainer) data.get(key);
        if (c == null) {
            wrapHeader(key, DataType.LONG);
            int offset = winkBuffer.position;
            winkBuffer.putLong(value);
            updateChange();
            data.put(key, new Container.LongContainer(offset, value));
            checkIfCommit();
        } else if (c.value != value) {
            long sum = value ^ c.value;
            c.value = value;
            updateInt64(value, sum, c.offset);
            checkIfCommit();
        }
    }

    /**
     * 保存double数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putDouble(String key, double value) {
        checkKey(key);
        Container.DoubleContainer c = (Container.DoubleContainer) data.get(key);
        if (c == null) {
            wrapHeader(key, DataType.DOUBLE);
            int offset = winkBuffer.position;
            winkBuffer.putLong(Double.doubleToRawLongBits(value));
            updateChange();
            data.put(key, new Container.DoubleContainer(offset, value));
            checkIfCommit();
        } else if (c.value != value) {
            long newValue = Double.doubleToRawLongBits(value);
            long sum = Double.doubleToRawLongBits(c.value) ^ newValue;
            c.value = value;
            updateInt64(newValue, sum, c.offset);
            checkIfCommit();
        }
    }

    /**
     * 保存string数据
     *
     * @param key 键
     * @param value 值
     */
    public synchronized void putString(String key, String value) {
        checkKey(key);
        if (value == null) {
            remove(key);
        } else {
            Container.StringContainer c = (Container.StringContainer) data.get(key);
            if (value.length() * 3 < INTERNAL_LIMIT) {
                // putString is a frequently operation,
                // so we make some redundant code to speed up putString method.
                winkPutString(key, value, c);
            } else {
                byte[] bytes = value.isEmpty() ? EMPTY_ARRAY : value.getBytes(StandardCharsets.UTF_8);
                addOrUpdate(key, value, bytes, c, DataType.STRING);
            }
        }
    }

    /**
     * 保存二进制数据
     *
     * @param key 键
     * @param value 二进制数据
     */
    public synchronized void putArray(String key, byte[] value) {
        checkKey(key);
        if (value == null) {
            remove(key);
        } else {
            Container.ArrayContainer c = (Container.ArrayContainer) data.get(key);
            addOrUpdate(key, value, value, c, DataType.ARRAY);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Encoder<T> getEncoder(T value) {
        Encoder<T> INSTANCE = null;
        if (value == null) {
            return null;
        }
        try {
            String canonicalName = value.getClass().getSimpleName() + EncoderManager.SUFFIX;
            INSTANCE = (Encoder<T>) encoderMap.get(canonicalName);
        } catch (Exception e) {
            WinkKVLog.e(TAG, "getEncoder error: ", e);
        }
        return INSTANCE;
    }

    /**
     * @param key     The name of the data to modify
     * @param value   The new value
     * @param <T>     Type of value
     */
    public synchronized <T> void putObject(String key, T value) {
        checkKey(key);
        putObject(key, value, getEncoder(value));
    }

    /**
     * @param key     The name of the data to modify
     * @param value   The new value
     * @param encoder The encoder to encode value to byte[], encoder must register in  Builder.encoder(),
     *                for decoding byte[] to object in next loading.
     * @param <T>     Type of value
     */
    public synchronized <T> void putObject(String key, T value, Encoder<T> encoder) {
        checkKey(key);
        if (encoder == null) {
            throw new IllegalArgumentException("Encoder is null");
        }
        String tag = encoder.tag();
        WinkKVLog.i(TAG, "[putObject] encoder.tag: " + tag);
        if (tag == null || tag.isEmpty() || tag.length() > 50) {
            throw new IllegalArgumentException("Invalid encoder tag:" + tag);
        }
        if (!encoderMap.containsKey(tag)) {
            throw new IllegalArgumentException("Encoder hasn't been registered");
        }

        if (value == null) {
            remove(key);
            return;
        }
        byte[] obj = null;
        try {
            obj = encoder.encode(value);
        } catch (Exception e) {
            error(e);
        }
        if (obj == null) {
            remove(key);
            return;
        }

        // assemble object bytes
        int tagSize = WinkBuffer.getStringSize(tag);
        WinkBuffer buffer = new WinkBuffer(1 + tagSize + obj.length);
        buffer.put((byte) tagSize);
        buffer.putString(tag);
        buffer.putBytes(obj);
        byte[] bytes = buffer.hb;

        Container.ObjectContainer c = (Container.ObjectContainer) data.get(key);
        addOrUpdate(key, value, bytes, c, DataType.OBJECT);
    }

    /**
     * 保存Set<String>数据
     * @param key 键
     * @param set 值
     */
    public synchronized void putStringSet(String key, Set<String> set) {
        if (set == null) {
            remove(key);
        } else {
            putObject(key, set, StringSetEncoder.INSTANCE);
        }
    }

    /**
     * 根据key，删除数据。并且可能要自动触发GC
     *
     * @param key 键
     */
    public synchronized void remove(String key) {
        Container.BaseContainer container = data.get(key);
        if (container != null) {
            String oldFileName = null;
            data.remove(key);
            byte type = container.getType();
            if (type <= DataType.DOUBLE) {
                int keySize = WinkBuffer.getStringSize(key);
                int start = container.offset - (2 + keySize);
                remove(type, start, container.offset + TYPE_SIZE[type]);
            } else {
                Container.VarContainer c = (Container.VarContainer) container;
                remove(type, c.start, c.offset + c.valueSize);
                oldFileName = c.external ? (String) c.value : null;
            }
            byte newByte = (byte) (type | MaskType.MASK_TYPE_DELETE);
            if (writingMode == WritingModeType.NON_BLOCKING) {
                aBuffer.putLong(4, checksum);
                aBuffer.put(removeStart, newByte);
                bBuffer.putLong(4, checksum);
                bBuffer.put(removeStart, newByte);
            } else {
                winkBuffer.putLong(4, checksum);
            }
            removeStart = 0;
            if (oldFileName != null) {
                Util.deleteFile(new File(path + name, oldFileName));
            }
            checkGC();
            checkIfCommit();
        }
    }

    /**
     * 清除所有数据
     */
    public synchronized void clear() {
        try {
            resetData();
        } catch (IOException e) {
            error(e);
            toResetBlockMode();
        }
        if (writingMode != WritingModeType.NON_BLOCKING) {
            deleteCFiles();
        }
    }

    /**
     * 获取所有数据
     *
     * @return 返回包括所有数据的map
     */
    public synchronized Map<String, Object> getAll() {
        int size = data.size();
        if (size == 0) {
            return new HashMap<>();
        }
        Map<String, Object> result = new HashMap<>(size * 4 / 3 + 1);
        for (Map.Entry<String, Container.BaseContainer> entry : data.entrySet()) {
            String key = entry.getKey();
            Container.BaseContainer c = entry.getValue();
            Object value = null;
            switch (c.getType()) {
                case DataType.BOOLEAN:
                    value = ((Container.BooleanContainer) c).value;
                    break;
                case DataType.INT:
                    value = ((Container.IntContainer) c).value;
                    break;
                case DataType.FLOAT:
                    value = ((Container.FloatContainer) c).value;
                    break;
                case DataType.LONG:
                    value = ((Container.LongContainer) c).value;
                    break;
                case DataType.DOUBLE:
                    value = ((Container.DoubleContainer) c).value;
                    break;
                case DataType.STRING:
                    Container.StringContainer sc = (Container.StringContainer) c;
                    value = sc.external ? getStringFromFile(sc) : sc.value;
                    break;
                case DataType.ARRAY:
                    Container.ArrayContainer ac = (Container.ArrayContainer) c;
                    value = ac.external ? getArrayFromFile(ac) : ac.value;
                    break;
                case DataType.OBJECT:
                    Container.ObjectContainer oc = (Container.ObjectContainer) c;
                    value = oc.external ? getObjectFromFile(oc) : ((Container.ObjectContainer) c).value;
                    break;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 保存所有数据
     *
     * @param values 需保存的数据
     */
    public void putAll(Map<String, Object> values) {
        putAll(values, null);
    }

    /**
     * Batch put objects.
     * Only support type in [boolean, int, long, float, double, String, byte[], Set of String] and object with encoder.
     *
     * @param values   map of key to value
     * @param encoders map of value Class to Encoder
     */
    public synchronized void putAll(Map<String, Object> values, Map<Class, Encoder> encoders) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && !key.isEmpty()) {
                if (value instanceof String) {
                    putString(key, (String) value);
                } else if (value instanceof Boolean) {
                    putBoolean(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    putLong(key, (Long) value);
                } else if (value instanceof Float) {
                    putFloat(key, (Float) value);
                } else if (value instanceof Double) {
                    putDouble(key, (Double) value);
                } else if (value instanceof Set) {
                    Set set = (Set) value;
                    if (!set.isEmpty() && set.iterator().next() instanceof String) {
                        //noinspection unchecked
                        putStringSet(key, (Set<String>) value);
                    }
                } else if (value instanceof byte[]) {
                    putArray(key, (byte[]) value);
                } else {
                    if (encoders != null) {
                        Encoder encoder = encoders.get(value.getClass());
                        if (encoder != null) {
                            //noinspection unchecked
                            putObject(key, value, encoder);
                        } else {
                            warning(new Exception("missing encoder for type:" + value.getClass()));
                        }
                    } else {
                        warning(new Exception("missing encoders"));
                    }
                }
            }
        }
    }

    /**
     * Forces any changes to be written to the storage device containing the mapped file.
     * No need to call this unless what's had written is very import
     * and you worry about system crash or power off would happen before data sync to disk.
     */
    public synchronized void force() {
        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.force();
            bBuffer.force();
        }
    }

    /**
     * When you open file with mode of SYNC_BLOCKING or ASYNC_BLOCKING,
     * It will auto commit after every putting or removing, by default.
     * If you need to batch update several key-values, you could call this method at first,
     * and call {@link #commit()} after updating, that method will recover {@link #autoCommit} to 'true' again.
     */
    public synchronized void disableAutoCommit() {
        this.autoCommit = false;
    }

    public synchronized boolean commit() {
        autoCommit = true;
        return commitToCFile();
    }

    private void checkIfCommit() {
        if (writingMode != WritingModeType.NON_BLOCKING && autoCommit) {
            commitToCFile();
        }
    }

    private boolean commitToCFile() {
        if (writingMode == WritingModeType.ASYNC_BLOCKING) {
            WinkKVConfig.getExecutor().execute(this::writeToCFile);
        } else if (writingMode == WritingModeType.SYNC_BLOCKING) {
            return writeToCFile();
        }
        return true;
    }

    private synchronized boolean writeToCFile() {
        try {
            File tmpFile = new File(path, name + TEMP_SUFFIX);
            if (Util.makeFileIfNotExist(tmpFile)) {
                RandomAccessFile accessFile = new RandomAccessFile(tmpFile, "rw");
                accessFile.setLength(dataEnd);
                accessFile.write(winkBuffer.hb, 0, dataEnd);
                accessFile.close();
                File cFile = new File(path, name + C_SUFFIX);
                if (!cFile.exists() || cFile.delete()) {
                    if (tmpFile.renameTo(cFile)) {
                        return true;
                    } else {
                        warning(new Exception("rename failed"));
                    }
                }
            }
        } catch (Exception e) {
            error(e);
        }
        return false;
    }

    private void deleteCFiles() {
        try {
            Util.deleteFile(new File(path, name + C_SUFFIX));
            Util.deleteFile(new File(path, name + TEMP_SUFFIX));
        } catch (Exception e) {
            error(e);
        }
    }

    private void resetData() throws IOException {
        if (winkBuffer == null || winkBuffer.hb.length != PAGE_SIZE) {
            winkBuffer = new WinkBuffer(PAGE_SIZE);
        }
        if (writingMode == WritingModeType.NON_BLOCKING) {
            resetBuffer(aBuffer);
            resetBuffer(bBuffer);
        }
        clearData();
        Util.deleteFile(new File(path + name));
    }

    private void resetBuffer(MappedByteBuffer buffer) throws IOException {
        if (buffer.capacity() != PAGE_SIZE) {
            FileChannel channel = buffer == aBuffer ? aChannel : bChannel;
            channel.truncate(PAGE_SIZE);
            MappedByteBuffer newBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, PAGE_SIZE);
            newBuffer.order(ByteOrder.LITTLE_ENDIAN);
            if (buffer == aBuffer) {
                aBuffer = newBuffer;
            } else {
                bBuffer = newBuffer;
            }
            buffer = newBuffer;
        }
        buffer.putInt(0, 0);
        buffer.putLong(4, 0L);
    }

    private void clearData() {
        dataEnd = DATA_START;
        checksum = 0L;
        clearInvalid();
        data.clear();
    }

    private void checkKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("key is empty");
        }
    }

    private void checkKeySize(int keySize) {
        if (keySize > 0xFF) {
            throw new IllegalArgumentException("key's length must less than 256");
        }
    }

    private void wrapHeader(String key, byte type) {
        wrapHeader(key, type, TYPE_SIZE[type]);
    }

    private void wrapHeader(String key, byte type, int valueSize) {
        int keySize = WinkBuffer.getStringSize(key);
        checkKeySize(keySize);
        updateSize = 2 + keySize + valueSize;
        preparePutBytes();
        winkBuffer.put(type);
        putKey(key, keySize);
    }

    private void updateChange() {
        checksum ^= winkBuffer.getChecksum(updateStart, updateSize);
        if (writingMode == WritingModeType.NON_BLOCKING) {
            // When size of changed data is more than 8 bytes,
            // checksum might fail to check the integrity in small probability.
            // So we make the dataLen to be negative,
            // if crash happen when writing data to mmap memory,
            // we can know that the writing was not completely.
            aBuffer.putInt(0, -1);
            syncABBuffer(aBuffer);
            aBuffer.putInt(0, dataEnd - DATA_START);

            // bBuffer doesn't need to mark dataLen's part before writing bytes,
            // cause aBuffer has already written completely.
            // We just need to have one file to be completely at least at any time.
            syncABBuffer(bBuffer);
        } else {
            if (sizeChanged) {
                winkBuffer.putInt(0, dataEnd - DATA_START);
            }
            winkBuffer.putLong(4, checksum);
        }
        sizeChanged = false;
        removeStart = 0;
        updateSize = 0;
    }

    private void syncABBuffer(MappedByteBuffer buffer) {
        if (sizeChanged && buffer != aBuffer) {
            buffer.putInt(0, dataEnd - DATA_START);
        }
        buffer.putLong(4, checksum);
        if (removeStart != 0) {
            buffer.put(removeStart, winkBuffer.hb[removeStart]);
        }
        if (updateSize != 0) {
            buffer.position(updateStart);
            buffer.put(winkBuffer.hb, updateStart, updateSize);
        }
    }

    private int bytesThreshold() {
        if (dataEnd <= (1 << 14)) {
            return BASE_GC_BYTES_THRESHOLD;
        } else {
            return dataEnd <= (1 << 16) ? BASE_GC_BYTES_THRESHOLD << 1 : BASE_GC_BYTES_THRESHOLD << 2;
        }
    }

    private void ensureSize(int allocate) {
        int capacity = winkBuffer.hb.length;
        int expected = dataEnd + allocate;
        if (expected >= capacity) {
            if (invalidBytes > allocate && invalidBytes > bytesThreshold()) {
                gc(allocate);
            } else {
                int newCapacity = getNewCapacity(capacity, expected);
                byte[] bytes = new byte[newCapacity];
                System.arraycopy(winkBuffer.hb, 0, bytes, 0, dataEnd);
                winkBuffer.hb = bytes;
                if (writingMode == WritingModeType.NON_BLOCKING) {
                    try {
                        aBuffer = aChannel.map(FileChannel.MapMode.READ_WRITE, 0, newCapacity);
                        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        bBuffer = bChannel.map(FileChannel.MapMode.READ_WRITE, 0, newCapacity);
                        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    } catch (IOException e) {
                        error(new Exception(MAP_FAILED, e));
                        winkBuffer.putInt(0, dataEnd - DATA_START);
                        winkBuffer.putLong(4, checksum);
                        degrade();
                    }
                }
            }
        }
    }

    private long shiftCheckSum(long checkSum, int offset) {
        int shift = (offset & 7) << 3;
        return (checkSum << shift) | (checkSum >>> (64 - shift));
    }

    private void updateBoolean(byte value, int offset) {
        checksum ^= shiftCheckSum(1L, offset);
        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.putLong(4, checksum);
            aBuffer.put(offset, value);
            bBuffer.putLong(4, checksum);
            bBuffer.put(offset, value);
        } else {
            winkBuffer.putLong(4, checksum);
        }
        winkBuffer.hb[offset] = value;
    }

    private void updateInt32(int value, long sum, int offset) {
        checksum ^= shiftCheckSum(sum, offset);
        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.putLong(4, checksum);
            aBuffer.putInt(offset, value);
            bBuffer.putLong(4, checksum);
            bBuffer.putInt(offset, value);
        } else {
            winkBuffer.putLong(4, checksum);
        }
        winkBuffer.putInt(offset, value);
    }

    private void updateInt64(long value, long sum, int offset) {
        checksum ^= shiftCheckSum(sum, offset);
        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.putLong(4, checksum);
            aBuffer.putLong(offset, value);
            bBuffer.putLong(4, checksum);
            bBuffer.putLong(offset, value);
        } else {
            winkBuffer.putLong(4, checksum);
        }
        winkBuffer.putLong(offset, value);
    }

    private void updateBytes(int offset, byte[] bytes) {
        int size = bytes.length;
        checksum ^= winkBuffer.getChecksum(offset, size);
        winkBuffer.position = offset;
        winkBuffer.putBytes(bytes);
        checksum ^= winkBuffer.getChecksum(offset, size);
        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.putInt(0, -1);
            aBuffer.putLong(4, checksum);
            aBuffer.position(offset);
            aBuffer.put(bytes);
            aBuffer.putInt(0, dataEnd - DATA_START);
            bBuffer.putLong(4, checksum);
            bBuffer.position(offset);
            bBuffer.put(bytes);
        } else {
            winkBuffer.putLong(4, checksum);
        }
    }

    private void preparePutBytes() {
        ensureSize(updateSize);
        updateStart = dataEnd;
        dataEnd += updateSize;
        winkBuffer.position = updateStart;
        sizeChanged = true;
    }

    private void putKey(String key, int keySize) {
        winkBuffer.put((byte) keySize);
        if (keySize == key.length()) {
            //noinspection deprecation
            key.getBytes(0, keySize, winkBuffer.hb, winkBuffer.position);
            winkBuffer.position += keySize;
        } else {
            winkBuffer.putString(key);
        }
    }

    private void putStringValue(String value, int valueSize) {
        winkBuffer.putShort((short) valueSize);
        if (valueSize == value.length()) {
            //noinspection deprecation
            value.getBytes(0, valueSize, winkBuffer.hb, winkBuffer.position);
        } else {
            winkBuffer.putString(value);
        }
    }

    private void winkPutString(String key, String value, Container.StringContainer c) {
        int stringSize = WinkBuffer.getStringSize(value);
        if (c == null) {
            int keySize = WinkBuffer.getStringSize(key);
            checkKeySize(keySize);
            int preSize = 4 + keySize;
            updateSize = preSize + stringSize;
            preparePutBytes();
            winkBuffer.put(DataType.STRING);
            putKey(key, keySize);
            putStringValue(value, stringSize);
            data.put(key, new Container.StringContainer(updateStart, updateStart + preSize, value, stringSize, false));
            updateChange();
        } else {
            String oldFileName = null;
            boolean needCheckGC = false;
            int preSize = c.offset - c.start;
            if (c.valueSize == stringSize) {
                checksum ^= winkBuffer.getChecksum(c.offset, c.valueSize);
                if (stringSize == value.length()) {
                    //noinspection deprecation
                    value.getBytes(0, stringSize, winkBuffer.hb, c.offset);
                } else {
                    winkBuffer.position = c.offset;
                    winkBuffer.putString(value);
                }
                updateStart = c.offset;
                updateSize = stringSize;
            } else {
                updateSize = preSize + stringSize;
                preparePutBytes();
                winkBuffer.put(DataType.STRING);
                int keyBytes = preSize - 3;
                System.arraycopy(winkBuffer.hb, c.start + 1, winkBuffer.hb, winkBuffer.position, keyBytes);
                winkBuffer.position += keyBytes;
                putStringValue(value, stringSize);

                remove(DataType.STRING, c.start, c.offset + c.valueSize);
                needCheckGC = true;
                if (c.external) {
                    oldFileName = (String) c.value;
                }

                c.external = false;
                c.start = updateStart;
                c.offset = updateStart + preSize;
                c.valueSize = stringSize;
            }
            c.value = value;
            updateChange();
            if (needCheckGC) {
                checkGC();
            }
            if (oldFileName != null) {
                Util.deleteFile(new File(path + name, oldFileName));
            }
        }
        checkIfCommit();
    }

    private void addOrUpdate(String key, Object value, byte[] bytes, Container.VarContainer c, byte type) {
        if (c == null) {
            addObject(key, value, bytes, type);
        } else {
            if (c.external || c.valueSize != bytes.length) {
                updateObject(key, value, bytes, c);
            } else {
                updateBytes(c.offset, bytes);
                c.value = value;
            }
        }
        checkIfCommit();
    }

    private void addObject(String key, Object value, byte[] bytes, byte type) {
        int offset = saveArray(key, bytes, type);
        if (offset != 0) {
            int size;
            Object v;
            boolean external = tempExternalName != null;
            if (external) {
                size = Util.NAME_SIZE;
                v = tempExternalName;
                tempExternalName = null;
            } else {
                size = bytes.length;
                v = value;
            }
            Container.BaseContainer c;
            if (type == DataType.STRING) {
                c = new Container.StringContainer(updateStart, offset, (String) v, size, external);
            } else if (type == DataType.ARRAY) {
                c = new Container.ArrayContainer(updateStart, offset, v, size, external);
            } else {
                c = new Container.ObjectContainer(updateStart, offset, v, size, external);
            }
            data.put(key, c);
            updateChange();
        }
    }

    private void updateObject(String key, Object value, byte[] bytes, Container.VarContainer c) {
        int offset = saveArray(key, bytes, c.getType());
        if (offset != 0) {
            String oldFileName = c.external ? (String) c.value : null;
            remove(c.getType(), c.start, c.offset + c.valueSize);
            boolean external = tempExternalName != null;
            c.start = updateStart;
            c.offset = offset;
            c.external = external;
            if (external) {
                c.value = tempExternalName;
                c.valueSize = Util.NAME_SIZE;
                tempExternalName = null;
            } else {
                c.value = value;
                c.valueSize = bytes.length;
            }
            updateChange();
            checkGC();
            if (oldFileName != null) {
                Util.deleteFile(new File(path + name, oldFileName));
            }
        }
    }

    private int saveArray(String key, byte[] value, byte type) {
        tempExternalName = null;
        if (value.length < INTERNAL_LIMIT) {
            return wrapArray(key, value, type, false);
        } else {
            info("large value, key: " + key + ", size: " + value.length);
            String fileName = Util.randomName();
            File file = new File(path + name, fileName);
            if (Util.saveBytes(file, value)) {
                tempExternalName = fileName;
                byte[] fileNameBytes = new byte[Util.NAME_SIZE];
                //noinspection deprecation
                fileName.getBytes(0, Util.NAME_SIZE, fileNameBytes, 0);
                return wrapArray(key, fileNameBytes, type, true);
            } else {
                error("save large value failed");
                return 0;
            }
        }
    }

    private int wrapArray(String key, byte[] value, byte type, boolean external) {
        if (external) {
            type |= MaskType.MASK_TYPE_EXTERNAL;
        }
        wrapHeader(key, type, 2 + value.length);
        winkBuffer.putShort((short) value.length);
        int offset = winkBuffer.position;
        winkBuffer.putBytes(value);
        return offset;
    }

    private void remove(byte type, int start, int end) {
        countInvalid(start, end);
        byte newByte = (byte) (type | MaskType.MASK_TYPE_DELETE);
        byte oldByte = winkBuffer.hb[start];
        int shift = (start & 7) << 3;
        checksum ^= ((long) (newByte ^ oldByte) & 0xFF) << shift;
        winkBuffer.hb[start] = newByte;
        removeStart = start;
    }

    private void checkGC() {
        if (invalidBytes >= (bytesThreshold() << 1)
                || invalids.size() >= (dataEnd < (1 << 14) ? BASE_GC_KEYS_THRESHOLD : BASE_GC_KEYS_THRESHOLD << 1)) {
            gc(0);
        }
    }

    private void mergeInvalids() {
        int i = invalids.size() - 1;
        Segment p = invalids.get(i);
        while (i > 0) {
            Segment q = invalids.get(--i);
            if (p.start == q.end) {
                q.end = p.end;
                invalids.remove(i + 1);
            }
            p = q;
        }
    }

    void gc(int allocate) {
        Collections.sort(invalids);
        mergeInvalids();

        final Segment head = invalids.get(0);
        final int gcStart = head.start;
        final int newDataEnd = dataEnd - invalidBytes;
        final int newDataSize = newDataEnd - DATA_START;
        final int updateSize = newDataEnd - gcStart;
        final int gcSize = dataEnd - gcStart;
        final boolean fullChecksum = newDataSize < gcSize + updateSize;
        if (!fullChecksum) {
            checksum ^= winkBuffer.getChecksum(gcStart, gcSize);
        }
        // compact and record shift
        int n = invalids.size();
        final int remain = dataEnd - invalids.get(n - 1).end;
        int shiftCount = (remain > 0) ? n : n - 1;
        int[] srcToShift = new int[shiftCount << 1];
        int desPos = head.start;
        int srcPos = head.end;
        for (int i = 1; i < n; i++) {
            Segment q = invalids.get(i);
            int size = q.start - srcPos;
            System.arraycopy(winkBuffer.hb, srcPos, winkBuffer.hb, desPos, size);
            int index = (i - 1) << 1;
            srcToShift[index] = srcPos;
            srcToShift[index + 1] = srcPos - desPos;
            desPos += size;
            srcPos = q.end;
        }
        if (remain > 0) {
            System.arraycopy(winkBuffer.hb, srcPos, winkBuffer.hb, desPos, remain);
            int index = (n - 1) << 1;
            srcToShift[index] = srcPos;
            srcToShift[index + 1] = srcPos - desPos;
        }
        clearInvalid();

        if (fullChecksum) {
            checksum = winkBuffer.getChecksum(DATA_START, newDataEnd - DATA_START);
        } else {
            checksum ^= winkBuffer.getChecksum(gcStart, newDataEnd - gcStart);
        }
        dataEnd = newDataEnd;

        if (writingMode == WritingModeType.NON_BLOCKING) {
            aBuffer.putInt(0, -1);
            aBuffer.putLong(4, checksum);
            aBuffer.position(gcStart);
            aBuffer.put(winkBuffer.hb, gcStart, updateSize);
            aBuffer.putInt(0, newDataSize);
            bBuffer.putInt(0, newDataSize);
            bBuffer.putLong(4, checksum);
            bBuffer.position(gcStart);
            bBuffer.put(winkBuffer.hb, gcStart, updateSize);
        } else {
            winkBuffer.putInt(0, newDataSize);
            winkBuffer.putLong(4, checksum);
        }

        updateOffset(gcStart, srcToShift);
        int expectedEnd = newDataEnd + allocate;
        if (winkBuffer.hb.length - expectedEnd > TRUNCATE_THRESHOLD) {
            truncate(expectedEnd);
        }
        info(GC_FINISH);
    }

    private void updateOffset(int gcStart, int[] srcToShift) {
        Collection<Container.BaseContainer> values = data.values();
        for (Container.BaseContainer c : values) {
            if (c.offset > gcStart) {
                int index = Util.binarySearch(srcToShift, c.offset);
                int shift = srcToShift[(index << 1) + 1];
                c.offset -= shift;
                if (c.getType() >= DataType.STRING) {
                    ((Container.VarContainer) c).start -= shift;
                }
            }
        }
    }

    private void truncate(int expectedEnd) {
        // reserve at least one page space
        int newCapacity = getNewCapacity(PAGE_SIZE, expectedEnd + PAGE_SIZE);
        if (newCapacity >= winkBuffer.hb.length) {
            return;
        }
        byte[] bytes = new byte[newCapacity];
        System.arraycopy(winkBuffer.hb, 0, bytes, 0, dataEnd);
        winkBuffer.hb = bytes;
        if (writingMode == WritingModeType.NON_BLOCKING) {
            try {
                aChannel.truncate(newCapacity);
                aBuffer = aChannel.map(FileChannel.MapMode.READ_WRITE, 0, newCapacity);
                aBuffer.order(ByteOrder.LITTLE_ENDIAN);
                bChannel.truncate(newCapacity);
                bBuffer = bChannel.map(FileChannel.MapMode.READ_WRITE, 0, newCapacity);
                bBuffer.order(ByteOrder.LITTLE_ENDIAN);
            } catch (IOException e) {
                error(new Exception(MAP_FAILED, e));
                degrade();
            }
        }
        info(TRUNCATE_FINISH);
    }

    private int getNewCapacity(int capacity, int expected) {
        if (expected > DATA_SIZE_LIMIT) {
            throw new IllegalStateException("data size out of limit");
        }
        if (expected <= PAGE_SIZE) {
            return PAGE_SIZE;
        } else {
            while (capacity < expected) {
                if (capacity <= DOUBLE_LIMIT) {
                    capacity <<= 1;
                } else {
                    capacity += DOUBLE_LIMIT;
                }
            }
            return capacity;
        }
    }

    private void countInvalid(int start, int end) {
        invalidBytes += (end - start);
        invalids.add(new Segment(start, end));
    }

    private void clearInvalid() {
        invalidBytes = 0;
        invalids.clear();
    }

    private static class Segment implements Comparable<Segment> {
        int start;
        int end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Segment o) {
            return start - o.start;
        }
    }

    private void error(String message) {
        WinkKVLog.e(TAG, name, new Throwable(message));
    }

    private void error(Throwable e) {
        WinkKVLog.e(TAG, name, e);
    }

    private void warning(Exception e) {
        WinkKVLog.w(TAG, name, e);
    }

    private void info(String message) {
        WinkKVLog.i(TAG, message);
    }

    public interface Encoder<T> {
        String tag();

        byte[] encode(T obj);

        // bytes is not null (The caller had checked)
        T decode(byte[] bytes, int offset, int length);
    }

    public static class Builder {
        private static final Map<String, WinkKV> INSTANCE_MAP = new ConcurrentHashMap<>();
        private final String path;
        private final String name;
        private List<Encoder<?>> encoders;
        private int writingMode = WritingModeType.NON_BLOCKING;

        public Builder(String path, String name) {
            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("path is empty");
            }
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            this.path = path.endsWith("/") ? path : (path + '/');
            this.name = name;
        }

        /**
         * Set obj Encoders
         *
         * @param encoders The encoder array to decode the bytes to obj.
         * @return the builder
         */
        public Builder encoder(List<Encoder<?>> encoders) {
            this.encoders = encoders;
            return this;
        }

        /**
         * Assigned writing mode to SYNC_BLOCKING.
         * <p>
         * In non-blocking mode (write data with mmap),
         * it might loss update if the system crash or power off before flush data to disk.
         * You could use {@link #force()} to avoid loss update, or use SYNC_BLOCKING mode.
         * <p>
         * In blocking mode, every update will write all data to the file, which is expensive cost.
         * <p>
         * So it's recommended to use blocking mode only if the data is every important.
         * <p>
         * NOTE: DON'T CHANGE THE WRITING MODE ONCE THE FILE WAS CREATED, OTHERWISE MIGHT LOSS DATA.
         *
         * @return the builder
         */
        public Builder blocking() {
            writingMode = WritingModeType.SYNC_BLOCKING;
            return this;
        }

        /**
         * Similar to {@link #blocking()}, but put writing task to async thread.
         *
         * @return the builder
         */
        public Builder asyncBlocking() {
            writingMode = WritingModeType.ASYNC_BLOCKING;
            return this;
        }

        public WinkKV build() {
            String key = path + name;
            WinkKV kv = INSTANCE_MAP.get(key);
            if (kv == null) {
                synchronized (Builder.class) {
                    kv = INSTANCE_MAP.get(key);
                    if (kv == null) {
                        kv = new WinkKV(path, name, encoders, writingMode);
                        INSTANCE_MAP.put(key, kv);
                    }
                }
            }
            return kv;
        }

    }

    @NonNull
    @Override
    public synchronized String toString() {
        return "WinkKV: path:" + path + " name:" + name;
    }
}
