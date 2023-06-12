package com.demo.storage_ktx.utils

import com.demo.storage_ktx.WordRoomDatabase
import com.demo.storage_ktx.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据生成类
 * Created by walkerzpli on 2022/1/23.
 */
object DataFactoryUtil {

    @JvmStatic
    fun initUsers(database: WordRoomDatabase, scope: CoroutineScope) {
        val userDao = database.userDao()

        // init users
        val user1 = User(
            null,
            "zp",
            "lee",
            listOf("baidu", "tencent"),
            listOf("lily", "wawa"),
            123456789L,
            "test"
        )
        val user2 = User(
            null,
            "walker",
            "lee",
            null,
            null,
            123456789L,
            "test"
        )
        scope.launch(Dispatchers.IO) {
            userDao.insertAll(listOf(user1, user2))
        }
    }
}