package com.walker.apt.processor;

public class BindButtonAnnotationInfo {
    public int resId;
    public String className;

    public BindButtonAnnotationInfo(int resId, String className) {
        this.resId = resId;
        this.className = className;
    }
}
