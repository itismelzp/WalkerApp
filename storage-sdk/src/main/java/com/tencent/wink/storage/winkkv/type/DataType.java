package com.tencent.wink.storage.winkkv.type;

/**
 * 内部数据类型
 * <p>
 * Created by walkerzpli on 2021/11/30.
 */
public @interface DataType {
    byte BOOLEAN    = 1;
    byte INT        = 2;
    byte FLOAT      = 3;
    byte LONG       = 4;
    byte DOUBLE     = 5;
    byte STRING     = 6;
    byte ARRAY      = 7;
    byte OBJECT     = 8;
}
