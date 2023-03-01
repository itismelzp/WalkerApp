package com.demo.syscomponent

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.demo.ipc.ProcessUtil
import com.demo.logger.MyLog

class MyProvider : ContentProvider() {

    companion object {

        private const val TAG = "MyProvider"

        private const val SCHEME = "content://"
        const val AUTHORITY = "com.demo.syscomponent.myprovider"
        const val BASE_URI = "$SCHEME$AUTHORITY"
        const val USER_CODE = 1
        const val JOB_CODE = 2
    }

    private lateinit var mContext: Context
    private lateinit var mDBHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private var mMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        mMatcher.addURI(AUTHORITY, DBHelper.USER_TABLE_NAME, USER_CODE)
        mMatcher.addURI(AUTHORITY, DBHelper.JOB_TABLE_NAME, JOB_CODE)
    }

    override fun onCreate(): Boolean {

        mContext = context!!
        mDBHelper = DBHelper(mContext)
        db = mDBHelper.writableDatabase

        // 初始化两个表的数据(先清空两个表,再各加入一个记录)
        db.execSQL("delete from ${DBHelper.USER_TABLE_NAME}")
        db.execSQL("insert into ${DBHelper.USER_TABLE_NAME} values(1,'Carson');")
        db.execSQL("insert into ${DBHelper.USER_TABLE_NAME} values(2,'Kobe');")

        db.execSQL("delete from ${DBHelper.JOB_TABLE_NAME}")
        db.execSQL("insert into ${DBHelper.JOB_TABLE_NAME} values(1,'Android');")
        db.execSQL("insert into ${DBHelper.JOB_TABLE_NAME} values(2,'iOS');")

        MyLog.i(TAG, "[onCreate] ${ProcessUtil.getCurProcessLog()}")

        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return db.delete(getTableName(uri), selection, selectionArgs).also {
            notify(uri)
        }
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val tableName = getTableName(uri)
        db.replace(tableName, null, values)
        notify(uri)

//        val parseId = ContentUris.parseId(uri)
//        println("parseId: $parseId")
        return uri
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val tableName = getTableName(uri)
        return db.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder,
            null
        )
    }

    override fun update(
        uri: Uri, values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val cnt = db.update(getTableName(uri), values, selection, selectionArgs)
        notify(uri)
        return cnt
    }

    private fun getTableName(uri: Uri): String {
        return when (mMatcher.match(uri)) {
            USER_CODE -> DBHelper.USER_TABLE_NAME
            JOB_CODE -> DBHelper.JOB_TABLE_NAME
            else -> ""
        }
    }

    private fun notify(uri: Uri) {
        mContext.contentResolver.notifyChange(uri, null)
    }
}