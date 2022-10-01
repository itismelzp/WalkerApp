package com.demo.apt.autel;

import com.google.protobuf.GeneratedMessageV3;

/**
 * @Description: 命令类Key的Key 信息
 * @Copyright Powered By Autel ROBOTICS
 * @date: 2022-09-13 8:27
 */
public class AutelActionKeyInfo<ParamType, ResultType> extends AutelKeyInfo<ParamType> {

    private IAutelConverter<ResultType, ? extends GeneratedMessageV3> resultTypeConverter;

    public AutelActionKeyInfo(int componentType, String keyName,
                              IAutelConverter<ParamType, ? extends GeneratedMessageV3> valueConverter,
                              IAutelConverter<ResultType, ? extends GeneratedMessageV3> typeConverter) {
        super(componentType, keyName, valueConverter);
        resultTypeConverter = typeConverter;
    }

    @Override
    public AutelActionKeyInfo canGet(boolean canGet) {
        this.canGet = canGet;
        return this;
    }

    @Override
    public AutelActionKeyInfo canSet(boolean canSet) {
        this.canSet = canSet;
        return this;
    }

    @Override
    public AutelActionKeyInfo canListen(boolean canListen) {
        this.canListen = canListen;
        return this;
    }

    @Override
    public AutelActionKeyInfo canPerformAction(boolean var1) {
        canPerformAction = var1;
        return this;
    }

    @Override
    public AutelActionKeyInfo setIsEvent(boolean var1) {
        mIsEvent = var1;
        return this;
    }

    @Override
    public AutelActionKeyInfo setInnerIdentifier(String id) {
        innerIdentifier = id;
        return this;
    }

    public IAutelConverter<ResultType,?> getResultTypeConverter() {
        return resultTypeConverter;
    }
}