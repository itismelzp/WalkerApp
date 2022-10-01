package com.demo.apt.autel

/**
 * @date 2022/9/23.
 * @author maowei
 * @description 协议层Message_Type转换类
 */
object MessageTypeConvert {

    fun getConvertType(name: String): Int {
        return when (name) {
            //航点任务
            MessageTypeConstant.MISSION_WAYPOINT_ENTER_MSG -> MessageType.MISSION_WAYPOINT_ENTER_MSG

            // 云台
            else -> -1
        }
    }

}