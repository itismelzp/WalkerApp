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
    val DATABASE_BAK =
        "${Environment.getExternalStorageDirectory()}${File.separator}Download${File.separator}database_bak"

    @JvmStatic
    val FILES_BAK =
        "${Environment.getExternalStorageDirectory()}${File.separator}Download${File.separator}files_bak"

    fun getDatabaseFiles(context: Context): List<String> = context.databaseList().toList().map {
        context.getDatabasePath(it)
    }.map {
        it.path
    }

    fun getFiles(context: Context, subDir: String): List<String> {
        val parent = File("${context.filesDir.path}${File.separator}${subDir}")
        val fileNames = mutableListOf<String>()
        val fileTree: FileTreeWalk = parent.walk()
        fileTree.maxDepth(3) //需遍历的目录层次为1，即无须检查子目录
            .filter { it.isFile } //只挑选文件，不处理文件夹
//            .filter { it.extension in listOf("txt","mp4") }
            .forEach { fileNames.add(it.absolutePath) }
        return fileNames
    }

    fun getFileDir(context: Context, subDir: String): File =
        File("${context.filesDir.path}${File.separator}${subDir}")

    fun exportFile(
        src: File,
        dest: File = File("$DATABASE_BAK${File.separator}${src.name}"),
        force: Boolean = true,
        copyRecursively: Boolean = true
    ): Boolean = runCatching {
        if (dest.exists() && force) {
            val delete = dest.delete()
            MyLog.i(TAG, "[exportFile] delete success: $delete, file: $dest")
        }
        dest.takeUnless { dest.exists() }?.let {
            if (copyRecursively) src.copyRecursively(it) else src.copyTo(it)
            true
        } ?: false
    }.getOrElse {
        MyLog.e(TAG, "[exportFile] failed: $dest", it)
        false
    }

    fun copy(srcPath: String, dest: String): File? = File(srcPath).let {
        if (it.exists()) it.copyTo(File(dest), true) else null
    }

}


