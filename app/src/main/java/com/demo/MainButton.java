package com.demo;

import com.demo.utils.COLORS;

import java.util.HashMap;
import java.util.Map;

public class MainButton {

    public String name;

    public int type = MainButtonType.TYPE_OTHER;

    public Class<?> jumpClass;

    public OnclickListener onclickListener;

    private static final Map<Integer, Integer> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_VIEW, COLORS.FIRST_PAGE_ONE);
        COLOR_MAP.put(MainButtonType.TYPE_CUSTOM_VIEW, COLORS.FIRST_PAGE_TWO);
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_COMPONENT, COLORS.FIRST_PAGE_THREE);
        COLOR_MAP.put(MainButtonType.TYPE_COMPILE, COLORS.FIRST_PAGE_FOUR);
        COLOR_MAP.put(MainButtonType.TYPE_STORAGE, COLORS.FIRST_PAGE_FIVE);
        COLOR_MAP.put(MainButtonType.TYPE_OTHER, COLORS.THIRD_PAGE_ONE);
    }

    public MainButton(String name, Class<?> jumpClass) {
        this.name = name;
        this.jumpClass = jumpClass;
    }

    public MainButton(String name, int type, Class<?> jumpClass) {
        this.name = name;
        this.type = type;
        this.jumpClass = jumpClass;
    }

    public MainButton(String name, OnclickListener onclickListener) {
        this.name = name;
        this.onclickListener = onclickListener;
    }

    public MainButton(String name, int type, OnclickListener onclickListener) {
        this.name = name;
        this.type = type;
        this.onclickListener = onclickListener;
    }

    public int getColor() {
        Integer color = COLOR_MAP.get(type);
        if (color == null) {
            return COLORS.THIRD_PAGE_ONE;
        }
        return color;
    }

    interface OnclickListener {
        void onClickListener();
    }
}
