package com.demo;

public class MainButton {

    public String name;

    public int type = MainButtonType.TYPE_OTHER;

    public Class<?> jumpClass;

    public OnclickListener onclickListener;

    public MainButton(String name, Class<?> jumpClass) {
        this.name = name;
        this.jumpClass = jumpClass;
    }

    public MainButton(String name, int type, Class<?> jumpClass) {
        this.name = name;
        this.type = type;
        this.jumpClass = jumpClass;
    }

    public MainButton(String name, int type, Class<?> jumpClass, OnclickListener onclickListener) {
        this.name = name;
        this.type = type;
        this.jumpClass = jumpClass;
        this.onclickListener = onclickListener;
    }

    public int getColor() {

        switch (type) {
            case MainButtonType.TYPE_SYSTEM_VIEW:
                return 0xFF00FF00;
            case MainButtonType.TYPE_CUSTOM_VIEW:
                return 0xFF009900;
            case MainButtonType.TYPE_COMPILE:
                return 0xFFFF0000;
            case MainButtonType.TYPE_STORAGE:
                return 0xFF0000FF;
            default:
                return -1;
        }

    }

    interface OnclickListener {
        void onClickListener();
    }
}
