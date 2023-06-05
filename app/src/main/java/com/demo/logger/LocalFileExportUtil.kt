package com.demo.logger

import android.content.Context
import android.os.Environment
import com.demo.base.log.MyLog
import java.io.File

/**
 * Created by lizhiping on 2023/5/10.
 * <p>
 * description
 */
object LocalFileExportUtil {

    private const val TAG = "LocalFileDownLoader"

    @JvmStatic
    val DATABASE_BAK = "${Environment.getExternalStorageDirectory()}${File.separator}Download${File.separator}database_bak"

    fun getDatabaseFiles(context: Context): List<String> {
        return context.databaseList().toList().map {
            context.getDatabasePath(it)
        }.map {
            it.path
        }
    }

    fun exportFile(
        src: File,
        dest: File = File("$DATABASE_BAK${File.separator}${src.name}"),
        force: Boolean = true
    ): Boolean = runCatching {
        if (dest.exists() && force) {
            dest.delete()
        }
        dest.takeUnless { dest.exists() }?.let {
            src.copyTo(it)
            true
        } ?: false
    }.getOrElse {
        MyLog.e(TAG, "[exportFile] failed: $dest", it)
        false
    }
}


