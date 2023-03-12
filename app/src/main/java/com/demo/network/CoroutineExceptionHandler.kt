package com.demo.network

import com.demo.logger.MyLog
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.net.ssl.SSLHandshakeException
import javax.security.cert.CertificateException
import kotlin.coroutines.CoroutineContext

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
class CoroutineExceptionHandlerImpl : CoroutineExceptionHandler {

    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, e: Throwable) {
        when (e) {
            is ApiErrorException -> {
                // 处理业务错误
            }
            is JsonParseException -> {
                // 数据解析异常
            }
            is CertificateException, is SSLHandshakeException -> {
                // 证书异常
            }
            else -> {
                MyLog.e("CoroutineExceptionHandler", "CoroutineExceptionHandler error ", e)
            }
        }
    }
}