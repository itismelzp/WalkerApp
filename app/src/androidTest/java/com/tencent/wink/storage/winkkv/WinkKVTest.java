package com.tencent.wink.storage.winkkv;

import com.demo.storage.EncoderUtil;
import com.demo.storage.MyObject;
import com.demo.storage.MyParcelObject;
import com.demo.storage.PackableObject;
import com.demo.storage.PackableObject$Encoder;
import com.tencent.wink.storage.winkkv.WinkKV;
import com.tencent.wink.storage.winkkv.log.WinkKVLog;
import com.tencent.wink.storage.winkkv.type.WritingModeType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * WinkKV测试类
 * <p>
 * Created by walkerzpli on 2021/12/05.
 */
@SuppressWarnings("SimplifiableJUnitAssertion")
public class WinkKVTest {

    private static final String TAG = "WinkKVTest";
    private static final String FILE_NAME = "test_put_and_get";

    @Before
    public void init() {
        WinkKVLog.init(WinkTestHelper.logger);
        EncoderUtil.init();
    }

    @Test
    public void testPutAndGet() {

        List<WinkKV.Encoder<?>> encoders = new ArrayList<>();
        String name = "test_put_and_get";
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).build();
        kv1.clear();

        String boolKey = "bool_key";
        kv1.putBoolean(boolKey, true);

        String intKey = "int_key";
        kv1.putInt(intKey, 20220109);

        String floatKey = "float_key";
        kv1.putFloat(floatKey, 3.14159f);

        String longKey = "long_key";
        kv1.putLong(longKey, Long.MAX_VALUE);

        String doubleKey = "double_key";
        kv1.putDouble(doubleKey, 99.9);

        String stringKey = "string_key";
        kv1.putString(stringKey, "hello, world");

        String stringSetKey = "string_set_key";
        kv1.putStringSet(stringSetKey, WinkTestHelper.makeStringSet());

        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, name, /*encoders, */WritingModeType.NON_BLOCKING);

        Assert.assertEquals(kv1.getBoolean(boolKey), kv2.getBoolean(boolKey));
        Assert.assertEquals(kv1.getInt(intKey), kv2.getInt(intKey));
        Assert.assertTrue(kv1.getFloat(floatKey) == kv2.getFloat(floatKey));
        Assert.assertEquals(kv1.getLong(longKey), kv2.getLong(longKey));
        Assert.assertTrue(kv1.getDouble(doubleKey) == kv2.getDouble(doubleKey));
        Assert.assertEquals(kv1.getString(stringKey), kv1.getString(stringKey));
        Assert.assertEquals(kv1.getStringSet(stringSetKey), kv2.getStringSet(stringSetKey));
        Assert.assertNull(kv2.getObject("foo"));
        Assert.assertNull(kv2.getString("foo2", null));
        Assert.assertEquals(kv2.getLong("foo3"), 0);
        Assert.assertTrue(kv2.contains(boolKey));
        Assert.assertFalse(kv2.contains("foo"));
        Assert.assertEquals(100, kv2.getLong("foo_long", 100));

        String name_2 = "put_all";
        // noinspection rawtypes
        Map<Class, WinkKV.Encoder> encoderMap = new HashMap<>();
        Map<String, Object> all_1 = kv1.getAll();
        WinkKV kv3 = new WinkKV(WinkTestHelper.DIR, name_2, encoders, WritingModeType.NON_BLOCKING);
        kv3.clear();
        kv3.putAll(all_1, encoderMap);

//        WinkKV kv4 = new WinkKV(WinkTestHelper.DIR, name_2, encoders, WritingModeType.NON_BLOCKING);
//        Assert.assertEquals(all_1, kv4.getAll());

        Map<String, Object> m = new HashMap<>();
        m.put("key_a", "value_a");
        m.put("key_b", "value_b");
        kv3.putAll(m);
        WinkKV kv5 = new WinkKV(WinkTestHelper.DIR, name_2, encoders, WritingModeType.NON_BLOCKING);
        Assert.assertEquals("value_a", kv5.getString("key_a"));
        Assert.assertEquals("value_b", kv5.getString("key_b"));

    }

    @Test
    public void testObjectPutAndGet() {

        List<WinkKV.Encoder<?>> encoders = new ArrayList<>();
        encoders.add(PackableObject$Encoder.INSTANCE);
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, FILE_NAME).encoder(encoders).build();

        kv1.clear();
        String objKey = "obj_key";
        MyObject obj = new MyObject(12345, "my test info.");
        kv1.putObject(objKey, obj);

        String parcelObjKey = "parcel_obj_key";
        MyParcelObject parcelObj = new MyParcelObject(1,2,3, "str_str");
        kv1.putObject(parcelObjKey, parcelObj);

        String packableObjKey = "packable_obj_key";
        PackableObject packableObject = new PackableObject(1218, "walker");
        kv1.putObject(packableObjKey, packableObject, PackableObject$Encoder.INSTANCE);

        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, FILE_NAME, encoders, WritingModeType.NON_BLOCKING);
        WinkKVLog.d(TAG, "kv1.objKey: " + kv1.getObject(objKey) + ", kv2.objKey: " + kv2.getObject(objKey));
        Assert.assertTrue(kv1.getObject(objKey).equals(kv2.getObject(objKey)));

        WinkKVLog.d(TAG, "kv1.parcelObjKey: " + kv1.getObject(parcelObjKey) + ", kv2.parcelObjKey: " + kv2.getObject(parcelObjKey));
        Assert.assertTrue(kv1.getObject(parcelObjKey).equals(kv2.getObject(parcelObjKey)));

        WinkKVLog.d(TAG, "kv1.packableObjKey: " + kv1.getObject(parcelObjKey) + ", kv2.packableObjKey: " + kv2.getObject(parcelObjKey));
        Assert.assertTrue(kv1.getObject(packableObjKey).equals(kv2.getObject(packableObjKey)));
    }

    @Test
    public void testNotASCII() {
        String name = "test_not_ascii";
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).build();
        kv1.clear();
        String[] notAscii = new String[]{"\uD842\uDFB7", "一", "二", "三", "四", "五"};
        int i = 1000;
        for (String str : notAscii) {
            kv1.putString(str, str + i);
            i++;
        }
        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.NON_BLOCKING);
        i = 1000;
        for (String str : notAscii) {
            Assert.assertEquals(str + i, kv2.getString(str));
            i++;
        }
        i = 1000;
        for (String str : notAscii) {
            kv1.putString(str, str + (i * i));
            i++;
        }
        i = 1000;
        for (String str : notAscii) {
            Assert.assertEquals(str + i, kv2.getString(str));
            i++;
        }
    }

    @Test
    public void testForce() throws Exception {
        String name = "test_force";
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).build();
        long newTime = System.currentTimeMillis() ^ System.nanoTime();

        kv1.putLong("time", newTime);
        kv1.force();

        File aFile = new File(WinkTestHelper.DIR, name + ".kva");
        RandomAccessFile accessFile = new RandomAccessFile(aFile, "r");
        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        accessFile.read(buffer.array(), 0, 26);
        long t = buffer.getLong(18);
        Assert.assertEquals(newTime, t);
    }

    @Test
    public void testAsyncBlockingMode() throws Exception {
        String name = "test_blocking";
        testSync(name);
        testAsync(name);
        testDisableAutoCommit(name);
    }

    private void testSync(String name) throws Exception {
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).blocking().build();
        kv1.clear();

        Assert.assertEquals(false, kv1.contains("time"));

        long newTime = System.currentTimeMillis() ^ System.nanoTime();
        kv1.putLong("time", newTime);

        File aFile = new File(WinkTestHelper.DIR, name + ".kvc");
        RandomAccessFile accessFile = new RandomAccessFile(aFile, "r");
        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        accessFile.read(buffer.array(), 0, 26);
        long t = buffer.getLong(18);
        Assert.assertEquals(newTime, t);

        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.SYNC_BLOCKING);
        Assert.assertEquals(newTime, kv2.getLong("time"));

        kv1.putLong("time", 100L);
        WinkKV kv3 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.SYNC_BLOCKING);
        Assert.assertEquals(100L, kv3.getLong("time"));
    }

    private void testAsync(String name) throws Exception {
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).asyncBlocking().build();
        kv1.clear();

        Assert.assertEquals(false, kv1.contains("time"));

        long newTime = System.currentTimeMillis() ^ System.nanoTime();
        kv1.putLong("time", newTime);
        Thread.sleep(50L);

        File aFile = new File(WinkTestHelper.DIR, name + ".kvc");
        RandomAccessFile accessFile = new RandomAccessFile(aFile, "r");
        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        accessFile.read(buffer.array(), 0, 26);
        long t = buffer.getLong(18);
        Assert.assertEquals(newTime, t);

        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.ASYNC_BLOCKING);
        Assert.assertEquals(newTime, kv2.getLong("time"));

        kv1.putLong("time", 100L);
        Thread.sleep(50L);
        WinkKV kv3 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.ASYNC_BLOCKING);
        Assert.assertEquals(100L, kv3.getLong("time"));
    }

    private void testDisableAutoCommit(String name) throws Exception {
        WinkKV kv1 = new WinkKV.Builder(WinkTestHelper.DIR, name).blocking().build();
        kv1.clear();

        Assert.assertEquals(false, kv1.contains("time"));
        long newTime = System.currentTimeMillis() ^ System.nanoTime();

        kv1.disableAutoCommit();
        kv1.putLong("time", newTime);
        kv1.putString("str", "hello");
        kv1.putInt("int", 100);

        WinkKV kv2 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.SYNC_BLOCKING);
        Assert.assertNotEquals(100, kv2.getInt("int"));

        boolean result = kv1.commit();
        Assert.assertEquals(true, result);

        WinkKV kv3 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.SYNC_BLOCKING);
        Assert.assertEquals(100, kv3.getInt("int"));

        File aFile = new File(WinkTestHelper.DIR, name + ".kvc");
        RandomAccessFile accessFile = new RandomAccessFile(aFile, "r");
        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        accessFile.read(buffer.array(), 0, 26);
        long t = buffer.getLong(18);
        Assert.assertEquals(newTime, t);

        kv1.putBoolean("bool", false);
        kv1.putBoolean("bool", true);

        WinkKV kv4 = new WinkKV(WinkTestHelper.DIR, name, null, WritingModeType.SYNC_BLOCKING);
        Assert.assertEquals(newTime, kv4.getLong("time"));
        Assert.assertEquals(true, kv4.getBoolean("bool"));
    }

}
