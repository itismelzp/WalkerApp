package com.walker.storage.winkkv;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 编解码类
 * <p>
 * Created by walkerzpli on 2021/10/15.
 */
class StringSetEncoder implements WinkKV.Encoder<Set<String>> {

    static final StringSetEncoder INSTANCE = new StringSetEncoder();

    private StringSetEncoder() {
    }

    @Override
    public String tag() {
        return "StringSetEncoder";
    }

    @Override
    public byte[] encode(Set<String> src) {
        if (src.isEmpty()) {
            return new byte[0];
        }
        int index = 0;
        int count = 0;
        int n = src.size();
        int[] sizeArray = new int[n];
        String[] strArray = new String[n];
        for (String str : src) {
            if (str == null) {
                count += 5;
                sizeArray[index] = -1;
            } else {
                int strSize = WinkBuffer.getStringSize(str);
                strArray[index] = str;
                sizeArray[index] = strSize;
                count += WinkBuffer.getVariant32Size(strSize) + strSize;
            }
            index++;
        }
        WinkBuffer buffer = new WinkBuffer(count);
        for (int i = 0; i < n; i++) {
            int size = sizeArray[i];
            buffer.putVariant32(size);
            if (size >= 0) {
                buffer.putString(strArray[i]);
            }
        }
        return buffer.hb;
    }

    @Override
    public Set<String> decode(byte[] bytes, int offset, int length) {
        Set<String> set = new LinkedHashSet<>();
        if (length > 0) {
            WinkBuffer buffer = new WinkBuffer(bytes, offset);
            int limit = offset + length;
            while (buffer.position < limit) {
                set.add(buffer.getString(buffer.getVarint32()));
            }
            if (buffer.position != limit) {
                throw new IllegalArgumentException("Invalid String set");
            }
        }
        return set;
    }
}
