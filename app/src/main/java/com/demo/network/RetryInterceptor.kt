package com.demo.network

import com.demo.logger.MyLog
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by lizhiping on 2023/3/22.
 *
 *
 * description
 */
class RetryInterceptor(private val retryCount: Int, private val retryInterval: Long) :
    Interceptor {

    companion object {
        private const val TAG = "RetryInterceptor"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val response: Response = chain.proceed(chain.request())
            MyLog.i(
                TAG,
                "[intercept] response.isSuccessful: ${response.isSuccessful}, code: ${response.code()}"
            )
            response
        } catch (exception: IOException) {
            retry(0, exception, chain)
        }
    }

    private fun retry(count: Int, exception: IOException, chain: Interceptor.Chain): Response {
        return if (count < retryCount) {
            try {
                MyLog.i(TAG, "retry, count: $count")
                TimeUnit.MILLISECONDS.sleep(retryInterval)
                chain.proceed(chain.request())
            } catch (exception: IOException) {
                retry(count + 1, exception, chain)
            }
        } else throw exception
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

        fun build(): RetryInterceptor {
            return RetryInterceptor(mRetryCount, mRetryInterval)
        }
    }

}