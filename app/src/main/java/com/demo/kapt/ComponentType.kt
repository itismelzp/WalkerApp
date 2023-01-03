package com.demo.kapt

/**
 * @Description:模块类型
 * @Copyright Powered By Autel ROBOTICS
 * @date:  2022-09-13 8:36
 */
enum class ComponentType(val value: Int) {

    UNKNOWN(0),//未知
    COMMON(1),//通用接口
    MISSION(2),//飞行任务管理
    AI_SERVICE(3),//AIService
    CAMERA(4),//相机
    RADAR_VISION_FUSION(5),//
    FLIGHT_CONTROLLER(6),//飞行控制
    FLIGHT_PARAMS(7),//飞行参数读取和设置
    BATTERY(8),//电池
    GIMBAL(9),//云台
    VISION(10),//视觉
    REMOTE_CONTROLLER(11),//遥控器
    ALINK(12);//Alink 图传频段

    companion object {
        fun find(value: Int): ComponentType {
            for (type in values()) {
                if (type.value == value) return type
            }
            return UNKNOWN
        }
    }
}

