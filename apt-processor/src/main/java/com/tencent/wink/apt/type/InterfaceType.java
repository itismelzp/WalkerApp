package com.tencent.wink.apt.type;

/**
 * 实现的接口类型
 * <p>
 * Created by walkerzpli on 2022/1/19.
 */
public @interface InterfaceType {
    int NONE_TYPE       = 0; // undefine
    int PARCELABLE_TYPE = 1; // Parcelable
    int PACKABLE_TYPE   = 2; // Packable
}
