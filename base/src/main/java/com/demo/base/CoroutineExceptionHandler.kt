package com.demo.base

import com.demo.base.log.MyLog
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException
import javax.security.cert.CertificateException
import kotlin.coroutines.CoroutineContext

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
class CoroutineExceptionHandlerImpl : CoroutineExceptionHandler {

    companion object {
        private const val TAG = "CoroutineExceptionHandl"
    }

    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, e: Throwable) {
        when (e) {
//            is ApiErrorException -> {
                // 处理业务错误
//            }
//            is JsonParseException -> {
                // 数据解析异常
//            }
            is CertificateException, is SSLHandshakeException -> {
                // 证书异常
            }
            is SocketTimeoutException -> {
                // 超时
                MyLog.e(TAG, "SocketTimeoutException error: ", e)
            }
            else -> {
                MyLog.e(TAG, "CoroutineExceptionHandler error: ", e)
            }
        }
    }
}