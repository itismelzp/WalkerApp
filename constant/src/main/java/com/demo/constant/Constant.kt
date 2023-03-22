package com.demo.constant

/**
 * Created by lizhiping on 2023/1/4.
 * <p>
 * description
 */
object Constant {
    const val FROM_ID_NOOP = 1000L
    const val FROM_ID_START_ACTIVITY = 1011L
    const val FROM_ID_START_SERVICE = 1012L


    const val UPLOAD_WORK_NAME = "UPLOAD_WORK_NAME"

    @JvmField
    val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
        "Verbose WorkManager Notifications"
    const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
        "Shows notifications whenever work starts"

    @JvmField
    val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
    const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1

}