package com.demo.apt.autel

import com.google.protobuf.GeneratedMessageV3

/**
 * @date 2022/9/21.
 * @author maowei
 * @description 空类型
 */
class AutelEmptyConvert : IAutelConverter<Void, GeneratedMessageV3>() {

    override fun setValueFromBean(bean: Void) {
    }


    override fun getBeanFromMessage(bean: GeneratedMessageV3): Void? {
        return null
    }

//    override fun getParameterType(): DroneBaseParamModelOuterClass.DroneParameterType {
//        return DroneBaseParamModelOuterClass.DroneParameterType.UNKOWN
//    }
}