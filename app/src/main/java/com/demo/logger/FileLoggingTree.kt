package com.demo.logger

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * Created by lizhiping on 2022/10/8.
 * <p>
 * description
 */
class FileLoggingTree : Timber.Tree() {

    companion object {
        private const val TAG = "Timber.FileLoggingTree"
    }

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {

        val cacheLogPath = "${Environment.getExternalStorageDirectory()}${File.separator}timber_log"
        if (TextUtils.isEmpty(cacheLogPath)) {
            return
        }

        val fileDir = File(cacheLogPath)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val file = File(cacheLogPath, "log.txt")

        Log.i(TAG, "file.path: ${file.absolutePath}")
        Log.v(TAG, "message: $message")
        val writer: FileWriter?
        var bufferedWriter: BufferedWriter? = null
        try {
            writer = FileWriter(file)
            bufferedWriter = BufferedWriter(writer)
            bufferedWriter.write(message)
            bufferedWriter.flush()
            Log.d(TAG, "存储文件成功")
        } catch (e: IOException) {
            Log.d(TAG, "存储文件失败")
            e.printStackTrace()
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}