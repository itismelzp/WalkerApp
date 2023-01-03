package com.demo.kapt


/**
 * Created by lizhiping on 2022/9/24.
 * <p>
 * 航点任务状态上报通知类
 */
data class MissionWaypointStatusReportNtfyBean(

    var timeStamp: Long = 0L,                                  // 系统时间戳; uint: ms
    var missionId: Int = 0,                                    // 任务ID
    var wpSeq: Int = 0,                                        // 航点序号
    var remainDistance: Int = 0,                               // 剩余距离
    var photoNum: Int = 0,                                     // 当前拍照张数
    var remainTime: Int = 0,                                   // 剩余时间; uint:s
    var arrived: Int = 0,                                      // 航点抵达状态; 0 - arrived; 1 - Not arrived
    var actionSeq: Int = 0,                                    // 当前动作序号
    var speedSet: Int = 0,                                     // 任务设置速度; Uint: 10E-3 m/s
    var guid: Int = 0,                                         // 任务guid
    var percent: Int = 0                                       // 任务进度百分比

)
