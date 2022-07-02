package com.tencent.wink.apt.bean;

public class BindButtonAnnotationInfo {

    public String filedName;
    public int resId;
    public String className;
    public int flag = -1;

    public BindButtonAnnotationInfo(int resId, String className) {
        this.resId = resId;
        this.className = className;
    }

    public BindButtonAnnotationInfo(int resId, String className, int flag) {
        this.resId = resId;
        this.className = className;
        this.flag = flag;
    }
}
