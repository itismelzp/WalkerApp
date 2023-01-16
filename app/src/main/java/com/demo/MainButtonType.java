package com.demo;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@IntDef({
        MainButtonType.TYPE_NONE,
        MainButtonType.TYPE_SYSTEM_VIEW,
        MainButtonType.TYPE_CUSTOM_VIEW,
        MainButtonType.TYPE_COMPILE,
        MainButtonType.TYPE_STORAGE,
        MainButtonType.TYPE_SYSTEM_COMPONENT,
        MainButtonType.TYPE_OTHER
})
@Retention(RetentionPolicy.SOURCE)
public @interface MainButtonType {

    int TYPE_NONE = 0;
    int TYPE_SYSTEM_VIEW = 1;
    int TYPE_CUSTOM_VIEW = 2;
    int TYPE_SYSTEM_COMPONENT = 3;
    int TYPE_COMPILE = 4;
    int TYPE_STORAGE = 5;
    int TYPE_OTHER = 100;

}
