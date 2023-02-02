package com.demo.kapt

import com.google.gson.Gson
import com.google.protobuf.Any
import com.google.protobuf.InvalidProtocolBufferException

/**
 * Created by lizhiping on 2022/9/26.
 * <p>
 * description
 */
class WaypointGUIDConverter : IAutelConverter<MissionWaypointGUIDBean, MissionWaypointGUID> {

    fun fromJsonStr(str: String): MissionWaypointGUIDBean? {
        return Gson().fromJson(str, MissionWaypointGUIDBean::class.java)
    }

    fun getJsonStr(): String? {
        return Gson().toJson(MissionWaypointGUIDBean(guid = 123456789))
    }

    fun setValueFromBean(bean: MissionWaypointGUIDBean) {
        val builder = MissionWaypointGUID()
        builder.guid = bean.guid
    }

    private fun getBeanFromMessage(bean: MissionWaypointGUID): MissionWaypointGUIDBean? {
        val retBean = MissionWaypointGUIDBean()
//        ReflectUtil.convertFromMessage(retBean, bean, retBean.javaClass, bean.javaClass)
        return retBean
    }

    fun unpack(message: Any?): MissionWaypointGUIDBean? {
        try {
            val msg = null
//                msg = message?.unpack(MissionWaypointGUID::class.java)
            return msg?.let { getBeanFromMessage(it) }
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
        }
        return null
    }
}