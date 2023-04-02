
package com.demo.utils

import android.media.ExifInterface


object AppConstants {

    object Package {
        const val PACKAGE_NAME_OCLOUD = "com.heytap.cloud"

        const val PACKAGE_NAME_MEDIA_STORE = "com.android.providers.media"

        const val PACKAGE_NAME_GDPR_SETTING = "com.android.phone"

        const val PACKAGE_NAME_LAUNCHER = "com.android.launcher"

        const val PACKAGE_OPLUS_MARKET = "com.heytap.market"

        const val PACKAGE_NAME_SETTINGS = "com.android.settings"

        const val PACKAGE_NAME_BLUETOOTH = "com.android.bluetooth"

        /**
         * 个人版wps
         */
        const val PACKAGE_NAME_PERSONAL_WPS = "cn.wps.moffice_eng"

        /**
         * 定制版wps
         */
        const val PACKAGE_NAME_LITE_WPS = "cn.wps.moffice.lite"
    }

    object Action {
        const val ACTION_ANDROID_NET_WIFI_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK"

        const val ACTION_REQUEST_VIDEO_THUMBNAIL = "com.oplus.gallery.action.request.single.thumbnail"

        const val ACTION_MEMORIES = "oplus.intent.action.view.memories"

        const val LOCAL_ACTION_MEMORIES_ALBUM = "com.oplus.gallery.action.memoriesalbum"

        const val LOCAL_ACTION_ALBUM_SET = "com.oplus.gallery.action.albumset"

        const val ACTION_SHARE_ATLAS_MOMENTS = "oplus.intent.action.SHARE_ATLAS_MOMENTS"

        const val LOCAL_ACTION_CLOUD_FUNC_CHANGE = "com.oplus.gallery.cloudsync.function.change"

        const val LOCAL_ACTION_SCAN_SERVICE_JOB = "com.oplus.gallery.framework.abilities.scan.job"

        const val ACTION_SCAN_ALARM = "com.oplus.gallery.action.trigger.detect"

        /**
         * for Settings.SIM
         */
        const val ACTION_GDPR_SETTING = "com.android.settings.MULTI_SIM_SETTINGS"

        /**
         * 授予系统权限通知
         */
        const val PERMISSION_GRANTED = "com.oplus.gallery.permission_granted"
    }

    object Service {
        @JvmStatic
        val service_HORAE by lazy {
            "horae"
        }
    }

//    object Permission {
//        const val SAFE_AUTHENTICATE_PERMISSION = "com.oplus.permission.safe.AUTHENTICATE"
//
//        @JvmStatic
//        val PERMISSION_COMPONENT_SAFE by lazy {
//            chooseBySystemVersion(ContextGetter.context.getStringArraySafe(R.array.permission_component_safe))
//        }
//    }

    object MagicCode {
        const val HASH_CODE_MAGIC = 31
    }

    object Path {
        @JvmStatic
        val PATH_CONVERT_SECURITY_DIR by lazy {
            "/DCIM/.convert_security_files/"
        }

        @JvmStatic
        val PATH_CONVERT_HEIF_DIR by lazy {
            "/DCIM/.convert_tmp_files/"
        }

        @JvmStatic
        val PATH_NO_MEDIA by lazy {
            ".nomedia"
        }
    }

    object SyncConstants {
        // receiver extra keys-values
        const val EXTRA_SYNC_DATA = "DATA"
        const val SYNC_DATA_ALL = "ALL"
        const val SYNC_DATA_RECYCLE = "RECYCLE"
        const val SYNC_DATA_INCR = "INCR"

        // receiver extra keys-values
        const val EXTRA_SYNC_TYPE = "TYPE"
        const val SYNC_TYPE_START_ALBUM = "sync_type_start_album"
        const val SYNC_TYPE_AUTO_BACKUP = "sync_type_auto_backup"
        const val SYNC_TYPE_NEW_PHOTO_AND_VIDEO = "sync_type_new_photo_and_video"
        const val SYNC_TYPE_SCREEN_SHOT = "sync_type_screen_shot"
        const val SYNC_TYPE_SAFE_BOX_START_FILE_SAFE = "sync_type_start_file_safe"
        const val SYNC_TYPE_GALLERY_NEW_ALBUMSET = "sync_type_gallery_new_albumset"
        const val SYNC_TYPE_GALLERY_SYNC_FILES = "sync_type_gallery_sync_files"
        const val SYNC_TYPE_PHONE_CLONE_RESTORE_END = "sync_type_phone_clone_restore_end"
        const val SYNC_TYPE_EDIT_PHOTO = "sync_type_edit_photo"
        const val SYNC_TYPE_JIGSAW_PHOTO = "sync_type_jigsaw_photo"
        const val SYNC_TYPE_DELETE_PHOTO = "sync_type_delete_photo"
        const val SYNC_TYPE_DELETE_RECYCLE_ALL = "sync_type_delete_recycle_all"
        const val SYNC_TYPE_DELETE_RECYCLE_PHOTO = "sync_type_delete_recycle_photo"
        const val SYNC_TYPE_RESTORE_PHOTO = "sync_type_restore_photo"
        const val SYNC_TYPE_RESTORE_ALL_PHOTO = "sync_type_restore_all_photo"
        const val SYNC_TYPE_START_FILE_SAFE = "sync_type_start_file_safe"
        const val SYNC_TYPE_EDIT_VIDEO = "sync_type_edit_video"
    }

    object Keys {
        const val KEY_INTENT = "intent"
        const val DEFAULT_TAG: String = "default_tag"
        const val CLOUD_FUNC_ENABLE = "cloud_func_enable"
    }

    object Preferences {
        const val PREFERENCE_USE_NETWORK_REMIND = "use_network_remind"
    }

    //表示来自哪个页面，内部
    object FromInternalPage {
        const val KEY_FROM_PAGE = "from_page"

        const val FROM_PAGE_UNKNOWN = -1

        const val FROM_PHOTO_PAGE = 1

        const val FROM_SETTING_PAGE = 2
    }

    /**
     * 存储容量常量
     */
    object Capacity {
        const val UNIT_KB2B     = 1024L
        const val UNIT_MB2KB    = 1024L
        const val UNIT_MB2B     = 1024 * 1024L
        const val UNIT_GB2MB    = 1024L
        const val UNIT_GB2KB    = 1024 * 1024L
        const val UNIT_GB2B     = 1024 * 1024 * 1024L

        const val MEM_5M        = UNIT_MB2B * 5L
        const val MEM_25M       = UNIT_MB2B * 25L

        const val MEM_1G        = UNIT_GB2B
        const val MEM_2G        = UNIT_GB2B * 2L
        const val MEM_3G        = UNIT_GB2B * 3L
        const val MEM_4G        = UNIT_GB2B * 4L
        const val MEM_5G        = UNIT_GB2B * 5L
        const val MEM_6G        = UNIT_GB2B * 6L
    }

    object Number {
        const val NUMBER_MINUS_1 = -1

        const val NUMBER_0 = 0
        const val NUMBER_1 = 1
        const val NUMBER_2 = 2
        const val NUMBER_3 = 3
        const val NUMBER_4 = 4
        const val NUMBER_5 = 5
        const val NUMBER_6 = 6
        const val NUMBER_7 = 7
        const val NUMBER_8 = 8
        const val NUMBER_9 = 9
        const val NUMBER_10 = 10
        const val NUMBER_11 = 11
        const val NUMBER_12 = 12
        const val NUMBER_13 = 13
        const val NUMBER_16 = 16
        const val NUMBER_24 = 24
        const val NUMBER_30 = 30
        const val NUMBER_31 = 31
        const val NUMBER_60 = 60
        const val NUMBER_100 = 100
        const val NUMBER_256 = 256

        const val NUMBER_0f = 0.0f
        const val NUMBER_1f = 1.0f
        const val NUMBER_2f = 2.0f
        const val NUMBER_3f = 3.0f
        const val NUMBER_4f = 4.0f
        const val NUMBER_5f = 5.0f
        const val NUMBER_6f = 6.0f
        const val NUMBER_7f = 7.0f
        const val NUMBER_8f = 8.0f
        const val NUMBER_9f = 9.0f
        const val NUMBER_10f = 10.0f
        const val NUMBER_12f = 12.0f

        const val NUMBER_0xFF = 0xFF
    }

    /**
     * 图片或视频的旋转角度
     */
    object Degree {
        const val DEGREE_0 = 0
        const val DEGREE_90 = 90
        const val DEGREE_180 = 180
        const val DEGREE_270 = 270
        const val DEGREE_360 = 360

        /**
         * 数据库存储的角度是[0， 360]，对入参进行校验，将其转换成数据库存储范围内的非负整数
         * (degrees % DEGREE_360 + DEGREE_360) % DEGREE_360
         */
        fun floorModeByDegree360(degrees: Int) = Math.floorMod(degrees, DEGREE_360)

        /**
         * 旋转角度必须是90度的整数倍
         */
        @JvmStatic
        fun isValidDegreeForRotate(degrees: Int): Boolean {
            return (degrees % DEGREE_90 == 0)
        }

        /**
         * @param degree 角度
         * @return 返回方向
         */
        @JvmStatic
        fun degreeToOrientation(degree: Int): String {
            return degreeToOrientationAsInt(degree).toString()
        }

        /**
         * @param degree 角度
         * @return 返回方向
         */
        @JvmStatic
        private fun degreeToOrientationAsInt(degree: Int): Int {
            var degrees = degree
            degrees %= DEGREE_360
            if (degrees < DEGREE_0) {
                degrees += DEGREE_360
            }
            return when {
                degrees < DEGREE_90 -> {
                    ExifInterface.ORIENTATION_NORMAL // 0 degrees 1
                }
                degrees < DEGREE_180 -> {
                    ExifInterface.ORIENTATION_ROTATE_90 // 90 degrees cw 6
                }
                degrees < DEGREE_270 -> {
                    ExifInterface.ORIENTATION_ROTATE_180 // 180 degrees 3
                }
                else -> {
                    ExifInterface.ORIENTATION_ROTATE_270 // 270 degrees cw 8
                }
            }
        }

        /**
         * @param degreeOrOrientation 角度或者方向
         * @return 返回方向
         */
        @JvmStatic
        fun getOrientationValueForRotation(degreeOrOrientation: Int): Int {
            var degree = degreeOrOrientation
            degree %= DEGREE_360
            if (degree < 0) {
                degree += DEGREE_360
            }
            return when (degree) {
                ExifInterface.ORIENTATION_NORMAL,
                ExifInterface.ORIENTATION_ROTATE_90,
                ExifInterface.ORIENTATION_ROTATE_180,
                ExifInterface.ORIENTATION_ROTATE_270 -> degree
                else -> degreeToOrientationAsInt(degree)
            }
        }

        /**
         * 图片的方向是否与原始方向垂直的，即旋转90度或者-90度
         * @param  degrees 角度
         * @return false/true
         */
        @JvmStatic
        fun isVertical(degrees: Int): Boolean {
            return (degrees + DEGREE_90) % DEGREE_180 == 0
        }
    }
}