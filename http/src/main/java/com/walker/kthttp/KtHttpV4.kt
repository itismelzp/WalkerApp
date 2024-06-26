package com.walker.kthttp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by lizhiping on 2023/4/24.
 * <p>
 * description
 */

suspend fun <T : Any> KtCall<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        val call = call(object : Callback<T> {
            override fun onSuccess(data: T) {
                println("Request success!")
                continuation.resume(data)
            }

            override fun onFail(throwable: Throwable) {
                println("Request fail!: $throwable")
                continuation.resumeWithException(throwable)
            }
        })

        continuation.invokeOnCancellation {
            print("Call cancelled!")
            call.cancel()
        }
    }

fun main() = runBlocking {
    val start = System.currentTimeMillis()
    val deferred = async {
        KtHttpV3.create(ApiServiceV3::class.java)
            .repos(lang = "Kotlin", since = "weekly")
            .await()
    }

    deferred.invokeOnCompletion {
        println("invokeOnCompletion")
    }

    delay(50L)

    deferred.cancel()
    println("Time cancel: ${System.currentTimeMillis() - start}")


    try {
        println(deferred.await())
    } catch (e: Exception) {
        println("Time exception: ${System.currentTimeMillis() - start}")
        println("Catch exception: $e")
    } finally {
        println("Time total: ${System.currentTimeMillis() - start}")
    }
}



