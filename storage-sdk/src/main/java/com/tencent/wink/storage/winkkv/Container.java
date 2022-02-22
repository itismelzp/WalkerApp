package com.tencent.wink.storage.winkkv;

import com.tencent.wink.storage.winkkv.type.DataType;

/**
 * 基本类型数据组装结构 (boolean/int/float/long/double):
 * [type|keyLen|keyData|value]
 *
 * <p>
 * 类类型的数据组装结构 (string/array/object):
 * [type|keyLen|keyData|valueLen|value]
 *
 * <p>
 * 类型长度: 1 byte
 * 键长度: 1 byte
 * 值长度: 2 bytes
 * <p>
 * Created by walkerzpli on 2021/10/15.
 */
class Container {
    static abstract class BaseContainer {
        // The offset record the start of value, not the start of key-value.
        int offset;

        abstract byte getType();
    }

    static class BooleanContainer extends BaseContainer {
        boolean value;

        BooleanContainer(int offset, boolean value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        byte getType() {
            return DataType.BOOLEAN;
        }
    }

    static class IntContainer extends BaseContainer {
        int value;

        IntContainer(int offset, int value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        byte getType() {
            return DataType.INT;
        }
    }

    static class FloatContainer extends BaseContainer {
        float value;

        FloatContainer(int offset, float value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        byte getType() {
            return DataType.FLOAT;
        }
    }

    static class LongContainer extends BaseContainer {
        long value;

        LongContainer(int offset, long value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        byte getType() {
            return DataType.LONG;
        }
    }

    static class DoubleContainer extends BaseContainer {
        double value;

        DoubleContainer(int offset, double value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        byte getType() {
            return DataType.DOUBLE;
        }
    }

    static abstract class VarContainer extends BaseContainer {
        Object value;
        int start;
        int valueSize;
        boolean external;

        VarContainer(int start, int offset, Object value, int size, boolean external) {
            this.start = start;
            this.offset = offset;
            this.value = value;
            this.valueSize = size;
            this.external = external;
        }
    }

    static class StringContainer extends VarContainer {
        StringContainer(int start, int offset, String value, int size, boolean external) {
            super(start, offset, value, size, external);
        }

        @Override
        byte getType() {
            return DataType.STRING;
        }
    }

    static class ArrayContainer extends VarContainer {

        ArrayContainer(int start, int offset, Object value, int size, boolean external) {
            super(start, offset, value, size, external);
        }

        @Override
        byte getType() {
            return DataType.ARRAY;
        }
    }

    static class ObjectContainer extends VarContainer {
        ObjectContainer(int start, int offset, Object value, int size, boolean external) {
            super(start, offset, value, size, external);
        }

        @Override
        byte getType() {
            return DataType.OBJECT;
        }
    }
}
