package com.demo;

import com.demo.utils.Colors;

import java.util.HashMap;
import java.util.Map;

public class MainButton implements Comparable<MainButton> {

    public String name;

    public int type = MainButtonType.TYPE_OTHER;

    public Class<?> jumpClass;

    public OnClickListener onclickListener;

    public boolean isHide;

    private static final Map<Integer, Integer> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_VIEW, Colors.FIRST_PAGE_ONE);
        COLOR_MAP.put(MainButtonType.TYPE_CUSTOM_VIEW, Colors.FIRST_PAGE_TWO);
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_COMPONENT, Colors.FIRST_PAGE_THREE);
        COLOR_MAP.put(MainButtonType.TYPE_COMPILE, Colors.FIRST_PAGE_FOUR);
        COLOR_MAP.put(MainButtonType.TYPE_STORAGE, Colors.FIRST_PAGE_FIVE);
        COLOR_MAP.put(MainButtonType.TYPE_OTHER, Colors.THIRD_PAGE_ONE);
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

    public MainButton(String name, OnClickListener onclickListener) {
        this.name = name;
        this.onclickListener = onclickListener;
    }

    public MainButton(String name, int type, OnClickListener onclickListener) {
        this.name = name;
        this.type = type;
        this.onclickListener = onclickListener;
    }

    public int getColor() {
        Integer color = COLOR_MAP.get(type);
        if (color == null) {
            return Colors.THIRD_PAGE_ONE;
        }
        return color;
    }

    @Override
    public int compareTo(MainButton o) {
        return Integer.compare(this.type, o.type);
    }

    public interface OnClickListener {
        void onClickListener();
    }
}
