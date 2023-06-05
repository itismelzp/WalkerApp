package com.demo.coroutine

import com.demo.base.log.MyLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * Created by lizhiping on 2023/3/19.
 *
 * @param errCode 错误码
 * @param errMsg 简要错误信息
 * @param report 是否需要上报
 */
class GlobalCoroutineExceptionHandler(
    private val errCode: Int,
    private val errMsg: String = "",
    private val report: Boolean = false
) : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val msg = exception.stackTraceToString()
        MyLog.e("$errCode", "GlobalCoroutineExceptionHandler:${msg}")
    }
}