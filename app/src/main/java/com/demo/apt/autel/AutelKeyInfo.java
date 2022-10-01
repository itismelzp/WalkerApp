package com.demo.apt.autel;

import androidx.annotation.Nullable;

import com.google.protobuf.GeneratedMessageV3;

/**
 * @Description:
 * @Copyright Powered By Autel ROBOTICS
 * @date: 2022-09-12 19:08
 */
public class AutelKeyInfo<T> {
    /**
     * 模块类型
     */
    public int componentType;
    public String keyName;
    private IAutelConverter<T, ? extends GeneratedMessageV3> converter;
    public boolean canGet;
    public boolean canSet;
    public boolean canListen;
    public boolean canPerformAction;
    public boolean mIsEvent;
    public String identifier;
    public String innerIdentifier;

    public AutelKeyInfo(int componentType, String keyName, IAutelConverter<T, ? extends GeneratedMessageV3> valueConverter) {

        this.componentType = componentType;
        this.keyName = keyName;
        converter = valueConverter;
    }

    public AutelKeyInfo() {
    }

    public AutelKeyInfo<T> canGet(boolean canGet) {
        this.canGet = canGet;
        return this;
    }

    public AutelKeyInfo<T> canSet(boolean canSet) {
        this.canSet = canSet;
        return this;
    }

    public AutelKeyInfo<T> canListen(boolean canListen) {
        this.canListen = canListen;
        return this;
    }

    public AutelKeyInfo<T> setIsEvent(boolean isEvent) {
        mIsEvent = isEvent;
        return this;
    }

    public AutelKeyInfo<T> canPerformAction(boolean canPerform) {
        canPerformAction = canPerform;
        return this;
    }

    public AutelKeyInfo<T> setInnerIdentifier(String id) {
        innerIdentifier = id;
        return this;
    }

    public boolean isCanGet() {
        return canGet;
    }

    public boolean isCanSet() {
        return canSet;
    }

    public boolean isCanListen() {
        return canListen;
    }

    public boolean isCanPerformAction() {
        return canPerformAction;
    }

    public boolean isEvent() {
        return mIsEvent;
    }

    public String getIdentifier() {
        return identifier == null ? "" : identifier;
    }

    public String getInnerIdentifier() {
        return innerIdentifier;
    }

    public int getComponentType() {
        return componentType;
    }

    public IAutelConverter<T,?> getTypeConverter() {
        return converter;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "";
    }
}
