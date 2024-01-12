/********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - GalleryScanUtils.java
 ** Description:
 **
 **
 ** Version: 1.0
 ** Date:2016-11-23
 ** Author:xiuhua.ke
 ** TAG: OPLUS_ARCH_EXTENDS
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 **  xiuhua.ke                      2016-11-23     1.0          OPLUS_ARCH_EXTENDS
 ********************************************************************************/
package com.oplus.gallery.framework.abilities.scan.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class GalleryScanUtils {
    public static final boolean NOTIFY_INSERT = false;
    // last scan time
    public static final String PREF_LAST_SCAN_TIME_KEY = "pref_last_scan_time_key";
    public static final String PREF_LAST_LABEL_SCAN_TIME_KEY = "pref_last_label_scan_time_key";
    public static final String PREF_LAST_OCR_SCAN_TIME_KEY = "pref_last_ocr_scan_time_key";
    public static final String PREF_LAST_MEMORIES_SCAN_TIME_KEY = "pref_last_memories_scan_time_key";
    public static final String PREF_LAST_MEDIA_SCAN_TIME_KEY = "pref_last_media_scan_time_key";

    public static final int GROUP_NUM_FACE_THRESHOLD = 4;
    public static final int GROUP_ID_0 = 0; //had not group yet
    public static final int GROUP_ID_1 = 1; //only one face will group to this, we will regroup them
    public static final int GROUP_ID_2 = 2; //can not sure the face is exactness
    public static final int GROUP_ID_3 = 3; //normal group base

    public static final int VIRTUAL_BASE_GROUP_ID_FOR_SINGLE_FACE = (Integer.MAX_VALUE / 2);//see GROUP_ID_1

    // 记录扫描task触发时间以及人物、标签的扫描进展情况
    public static final String PREF_SCAN_TASK_START_TIME_KEY = "pref_scan_task_start_time_key";
    public static final String PREF_FACE_SCAN_WENT_WELL_KEY = "pref_face_scan_went_well_key";
    public static final String PREF_LABEL_SCAN_WENT_WELL_KEY = "pref_label_scan_went_well_key";

    // number of scanned media and scan time in one day if no charging
    public static final String MEDIA_SCAN_COUNT_24h_KEY = "media_scan_count_24h";
    public static final String FACE_SCAN_COUNT_24h_KEY = "face_scan_count_24h";
    public static final String LABEL_SCAN_COUNT_24h_KEY = "label_scan_count_24h";
    public static final String MEMORIES_SCAN_COUNT_24h_KEY = "memories_scan_count_24h";
    public static final String OCR_SCAN_COUNT_24h_KEY = "ocr_scan_count_24h_key";
    public static final String SCAN_START_TIME_24h_KEY = "scan_start_time_24h";
    public static final String SCAN_COST_TIME_24h_KEY = "scan_cost_time_24h_key";

    //all scan count to recode scan info
    public static final String FACE_SCAN_COUNT_ALL_KEY = "face_scan_count_all";
    public static final String LABEL_SCAN_COUNT_ALL_KEY = "label_scan_count_all";
    public static final String MEMORIES_SCAN_COUNT_ALL_KEY = "memories_scan_count_all";
    public static final String OCR_SCAN_COUNT_ALL_KEY = "ocr_scan_count__all";
    private static final String PREF_FIRST_SCAN_KEY_SUFFIX = "_first_scan";
    private static final String PREF_PREFIX = "pref_scan_type_";

    //these parameters are used for hiding face albums, which all faces can not to show.
    public static final int LIMIT_FACE_COUNT_OF_GROUP_PHOTO = 5;
    public static final float SINGLE_FACE_DIVIDE_THUMB_AREA_RATIO = 0.0f;
    public static final float GROUP_FACE_DIVIDE_THUMB_AREA_RATIO = 0.05f;
    public static final long NATIVE_HEAP_FREE_SIZE_INCREASE_MAX = 20 * 1024 * 1024;
    public static final long DEFAULT_SCAN_TIME = -1;
    public static final int DEFAULT_SCAN_COUNT = 0;
    public static final float SINGLE_FACE_RATIO = 0;
    public static final float IS_SAME_FACE_RATIO = 0.7f;
    public static final long INIT_FAILED_MAX_INTERVAL_TIME = AlarmManager.INTERVAL_DAY * 15;
    //for sample cluster

    protected static final String SCALL_EXTRA_DATA_KEY = "media_id";
    protected static final int GROUP_FIRST_SHOW_NUM_MIN = 10;
    protected static final int SET_THUMBNAIL_OF_FEATURE_NUM_MIN = 3;
    protected static final int FILE_ABORT_COUNT_THRESHOLD = 3;
    protected static final int MOST_LIKELY_IMAGE_COUNT = 100;
    private static final String TAG = "GalleryScanUtils";
    private static final String RECORDE_SCAN_INFO_FILE = "scan_info.csv";
    private static final String COMMA = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String CRASH_CACHED_FILE = "crash.cb";
    private static final String PREF_ALL_SCAN_KEY = "pref_all_scan_key";
    private static final String PREF_FIRST_GROUP_KEY = "pref_first_group_key";
    private static final String PREF_FIRST_LABEL_SCAN_KEY = "pref_first_label_scan_key";
    private static final String PREF_FIRST_OCR_SCAN_KEY = "pref_first_ocr_scan_key";
    private static final String PREF_FIRST_MEDIA_SCAN_KEY = "pref_first_media_scan_key";
    private static final String PREF_FIRST_BLOB_SCAN_KEY = "pref_first_blob_scan_key";
    private static final String PREF_FACE_SCORE_UPDATE = "pref_face_score_update";
    private static final String PREF_LAST_MANUAL_GROUP_TIME_KEY = "pref_last_manual_group_time_key";
    private static final String PREF_LAST_ENTER_FACE_ALBUM_SET_TIME_KEY = "pref_last_enter_face_album_set_time_key";
    private static final String PREF_CV_FACE_VERSION_KEY = "cv_face_version_key";
    private static final String PREF_CURRENT_OCR_SCAN_VERSION = "pref_current_ocr_scan_version";
    private static final String RESPONSE_REMOTE_RESULT_TYPE = "response_remote_result_type";

    // test mode, set max scan cost time

    public static String getCrashFileCached(Context context) {
        String result = null;
        result = context.getFilesDir().getAbsolutePath() + "/" + CRASH_CACHED_FILE;
        return result;
    }

    public static String readAbortFile(Context context) {
        String filePath = getCrashFileCached(context);
        StringBuilder crashFile = new StringBuilder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = fis.read(buffer)) > 0) {
                crashFile.append(new String(buffer, 0, count, "utf-8"));
            }
            Log.d(TAG, "readAbortFile: " + crashFile);
        } catch (FileNotFoundException e) {
            //TODO: do nothing, cause maybe file is no exist
        } catch (Exception e) {
            Log.w(TAG, e);
        } finally {
            File file = new File(filePath);
            if (file.exists()) {
                boolean ans = file.delete();
                Log.d(TAG, "readAbortFile file.delete:" + ans);
            }
        }
        return crashFile.toString();
    }

}
