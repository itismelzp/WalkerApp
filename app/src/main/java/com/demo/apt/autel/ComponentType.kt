package com.demo.apt.autel

/**
 * @Description:模块类型
 * @Copyright Powered By Autel ROBOTICS
 * @date:  2022-09-13 8:36
*/
enum class ComponentType(val value: Int)  {

    UNKNOWN(0),
    CAMERA(1),
    REMOTECONTROLLER(2),
    FLIGHTCONTROLLER(3),
    GIMBAL(4),
    BATTERY(5),
    WIFI(6),
    MISSION(7),
    ALINK(8)
    ;

    companion object {
        fun find(value: Int): ComponentType {
            for (type in values()) {
                if (type.value == value) return type
            }
            return UNKNOWN
        }


        fun value(): Int {
            return 0
        }

        fun equals(type: Int): Boolean {
            return false
        }
    }
}

