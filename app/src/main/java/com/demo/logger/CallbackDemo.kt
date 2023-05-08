package com.demo.logger

import java.util.concurrent.TimeUnit

/**
 * Created by lizhiping on 2023/4/24.
 * <p>
 * description
 */
class CallbackDemo {
    fun mockDownLoadFile(path: String,  callback: DownLoadCallback) {
        TimeUnit.SECONDS.sleep(5)
        val distPath = "dist_path"
        callback.onSuccess(distPath)
    }
}

interface DownLoadCallback {
    fun onSuccess(distPath: String)
    fun onFailed(e: Throwable)
}

