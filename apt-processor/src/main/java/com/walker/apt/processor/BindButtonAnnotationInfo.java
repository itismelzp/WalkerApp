package com.walker.apt.processor;

public class BindButtonAnnotationInfo {
    public int resId;
    public Class<?> clazz;

    public BindButtonAnnotationInfo(int resId, Class<?> clazz) {
        this.resId = resId;
        this.clazz = clazz;
    }
}
