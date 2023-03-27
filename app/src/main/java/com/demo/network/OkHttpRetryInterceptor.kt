package com.demo.network

import com.demo.logger.MyLog
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by lizhiping on 2023/3/22.
 *
 *
 * description
 */
class OkHttpRetryInterceptor(private val mMaxRetryCount: Int, private val mRetryInterval: Long) :
    Interceptor {

    companion object {
        private const val TAG = "OkHttpRetryInterceptor"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        var response: Response? = null
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            MyLog.e(TAG, "[intercept] chain.proceed: ", e)
        }

        MyLog.i(TAG, "[intercept] doRequest.")
        var retryNum = 1
        while ((response == null || !response.isSuccessful) && retryNum <= mMaxRetryCount) {
            try {
                Thread.sleep(mRetryInterval)
            } catch (e: InterruptedException) {
                MyLog.e(TAG, "[intercept] error: $retryNum", e)
            }
            retryNum++

            try {
                response = chain.proceed(request)
            } catch (e: IOException) {
                MyLog.e(TAG, "[intercept] chain.proceed: ", e)
            }
            MyLog.i(TAG, "[intercept] retryNum: $retryNum")
        }

        if (response == null) {
            throw IOException()
        }
        return response
    }

    class Builder {

        private var mRetryCount = 3
        private var mRetryInterval = 500L

        fun buildRetryCount(retryCount: Int): Builder {
            mRetryCount = retryCount
            return this
        }

        fun buildRetryInterval(retryInterval: Long): Builder {
            mRetryInterval = retryInterval
            return this
        }

        fun build(): OkHttpRetryInterceptor {
            return OkHttpRetryInterceptor(mRetryCount, mRetryInterval)
        }
    }
}