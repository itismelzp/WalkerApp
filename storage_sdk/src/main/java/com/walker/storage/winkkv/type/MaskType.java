package com.walker.storage.winkkv.type;

/**
 * 掩码类型
 * <p>
 * Created by walkerzpli on 2021/11/30.
 */
public @interface MaskType {
    byte MASK_TYPE_DELETE = (byte) 0x80;  // 删除掩码
    byte MASK_TYPE_EXTERNAL = 0x40;         // 扩展掩码
    byte MASK_TYPE_DATA_TYPE = 0x3F;         // 数据类型掩码
}
