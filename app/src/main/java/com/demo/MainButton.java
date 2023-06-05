package com.demo;

import android.app.Activity;

import com.demo.constant.ButtonTypeName;
import com.demo.constant.Colors;

import java.util.HashMap;
import java.util.Map;

public class MainButton implements Comparable<MainButton> {

    public String name;

    public int type = MainButtonType.TYPE_OTHER;

    public String typeName;

    public Class<?> jumpClass;

    public OnClickListener clickListener;

    public boolean isHide;

    private static final Map<Integer, Integer> COLOR_MAP = new HashMap<>();
    private static final Map<Integer, String> TYPE_NAME_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_VIEW, Colors.FIRST_PAGE_ONE);
        COLOR_MAP.put(MainButtonType.TYPE_CUSTOM_VIEW, Colors.FIRST_PAGE_TWO);
        COLOR_MAP.put(MainButtonType.TYPE_SYSTEM_COMPONENT, Colors.FIRST_PAGE_THREE);
        COLOR_MAP.put(MainButtonType.TYPE_COMPILE, Colors.FIRST_PAGE_FOUR);
        COLOR_MAP.put(MainButtonType.TYPE_STORAGE, Colors.FIRST_PAGE_FIVE);
        COLOR_MAP.put(MainButtonType.TYPE_OTHER, Colors.THIRD_PAGE_ONE);

        TYPE_NAME_MAP.put(MainButtonType.TYPE_SYSTEM_VIEW, ButtonTypeName.SYSTEM_VIEW);
        TYPE_NAME_MAP.put(MainButtonType.TYPE_CUSTOM_VIEW, ButtonTypeName.CUSTOM_VIEW);
        TYPE_NAME_MAP.put(MainButtonType.TYPE_SYSTEM_COMPONENT, ButtonTypeName.SYSTEM_COMPONENT);
        TYPE_NAME_MAP.put(MainButtonType.TYPE_COMPILE, ButtonTypeName.COMPILE);
        TYPE_NAME_MAP.put(MainButtonType.TYPE_STORAGE, ButtonTypeName.STORAGE);
        TYPE_NAME_MAP.put(MainButtonType.TYPE_OTHER, ButtonTypeName.OTHER);
    }

    public MainButton(String name, Class<? extends Activity> jumpClass) {
        this(name, MainButtonType.TYPE_OTHER, jumpClass);
    }

    public MainButton(String name, int type, Class<? extends Activity> jumpClass) {
        this.name = name;
        this.type = type;
        this.jumpClass = jumpClass;
        this.typeName = TYPE_NAME_MAP.get(type);
    }

    public MainButton(String name, OnClickListener clickListener) {
        this(name, MainButtonType.TYPE_OTHER, clickListener);
    }

    public MainButton(String name, int type, OnClickListener clickListener) {
        this.name = name;
        this.type = type;
        this.clickListener = clickListener;
        this.typeName = TYPE_NAME_MAP.get(type);
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

    public static class Builder {

        private String name;

        private int type = MainButtonType.TYPE_OTHER;

        private Class<? extends Activity> jumpClass;

        private OnClickListener clickListener;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(@MainButtonType int type) {
            this.type = type;
            return this;
        }

        public Builder jumpClass(Class<? extends Activity> jumpClass) {
            this.jumpClass = jumpClass;
            return this;
        }

        public Builder onClickListener(OnClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public MainButton build() {
            if (jumpClass == null && clickListener == null) {
                throw new UnsupportedOperationException(
                        "at least set one of jumpClass or clickListener.");
            } else if (clickListener != null) {
                return new MainButton(name, type, clickListener);
            } else {
                return new MainButton(name, type, jumpClass);
            }
        }

    }

}
