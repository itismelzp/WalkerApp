package com.demo.kapt

import com.walker.annotations.AutelKey
import com.walker.annotations.ComponentType

object MessageTypeConstant {

    const val GET = "get"
    const val SET = "set"

    // 航点任务
//    @AutelKey(
//        keyType = "WaypointMissionKey",
//        keyName = "keyEnter",
//        componentType = ComponentType.MISSION,
//        canGet = true,
//        canSet = true,
//        paramBean = MissionWaypointGUIDBean::class,
//        resultBean = Void::class,
//    )
    const val MISSION_WAYPOINT_ENTER_MSG                  = "enterMission"     //进入航点任务功能，对应消息结构：无

//    @AutelKey(
//        keyType = "WaypointMissionKey",
//        keyName = "keyExit",
//        componentType = ComponentType.MISSION,
//        canGet = true,
//        canAction = true,
//        paramBean = Void::class,
//        resultBean = MissionWaypointGUIDBean::class,
//    )
    const val MISSION_WAYPOINT_EXIT_MSG                   = "exitMission"     //退出航点任务，对应消息结构：无

//    @AutelKey(
//        keyType = "OtherMissionKey",
//        keyName = "startMission",
//        componentType = ComponentType.MISSION,
//        canGet = true,
//        canAction = true,
//        paramBean = Void::class,
//        resultBean = MissionWaypointStatusReportNtfyBean::class,
//    )
    const val MISSION_WAYPOINT_START_MSG                  = "startMission"     //开始航点任务，对应消息结构：MissionWaypointGUID

//    @AutelKey(
//        keyType = "OtherMissionKey",
//        keyName = "pauseMission",
//        componentType = ComponentType.UNKNOWN,
//        canGet = true,
//        canAction = true,
//        paramBean = Void::class,
//        resultBean = MissionWaypointStatusReportNtfyBean::class,
//    )
    const val MISSION_WAYPOINT_PAUSE_MSG                  = "pauseMission"     //暂停航点任务，对应消息结构：无
}