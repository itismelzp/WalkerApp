package com.walker.kthttp

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`.getRawType
import com.walker.kthttp.Constant.BASE_URL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy

/**
 * Created by lizhiping on 2023/4/24.
 * <p>
 * description
 */

interface ApiServiceV5 {

    @GET("/repo")
    fun repos(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): KtCall<RepoList>

    @GET("/repo")
    fun reposSync(
        @Field("lange") lang: String,
        @Field("since") since: String
    ): RepoList

    @GET("/repo")
    fun reposFlow(
        @Field("lange") lang: String,
        @Field("since") since: String
    ): Flow<RepoList>
}

object KtHttpV5 {

    private var okHttpClient: OkHttpClient = OkHttpClient()
    private var gson: Gson = Gson()

    fun <T : Any> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            val annotations = method.annotations
            for (annotation in annotations) {
                if (annotation is GET) {
                    val url = BASE_URL + annotation.value
                    return@newProxyInstance invoke<T>(url, method, args!!)
                }
            }
            return@newProxyInstance null
        } as T
    }

    private fun <T : Any> invoke(path: String, method: Method, args: Array<Any>): Any? {
        if (method.parameterAnnotations.size != args.size) return null

        var url = path
        val parameterAnnotations = method.parameterAnnotations
        for (i in parameterAnnotations.indices) {
            for (parameterAnnotation in parameterAnnotations[i]) {
                if (parameterAnnotation is Field) {
                    val key = parameterAnnotation.value
                    val value = args[i].toString()
                    url += if (!url.contains("?")) {
                        "?$key=$value"
                    } else {
                        "&$key=$value"
                    }
                }
            }
        }

        val request = Request.Builder()
            .url(url)
            .build()

        val call = okHttpClient.newCall(request)

        return if (isKtCallReturn(method)) {
            val genericReturnType = getTypeArgument(method)
            KtCall<T>(call, gson, genericReturnType)
        } else {
            val response = okHttpClient.newCall(request).execute()

            val genericReturnType = method.genericReturnType
            val json = response.body?.string()
            gson.fromJson<Any?>(json, genericReturnType)
        }
    }

    private fun getTypeArgument(method: Method) =
        (method.genericReturnType as ParameterizedType).actualTypeArguments[0]

    private fun isKtCallReturn(method: Method) =
        getRawType(method.genericReturnType) == KtCall::class.java
}

fun <T : Any> KtCall<T>.asFlow1(): Flow<T> = callbackFlow {

    val job = launch {
        print("Coroutine start")
        delay(3000L)
        println("Coroutine end")
    }

    job.invokeOnCompletion {
        println("Coroutine completed $it")
    }

    val call = call(object : Callback<T> {
        override fun onSuccess(data: T) {
            trySendBlocking(data)
                .onSuccess { close() }
                .onFailure {
                    cancel(CancellationException("Send channel fail!", it))
                }
        }

        override fun onFail(throwable: Throwable) {
            cancel(CancellationException("Send channel fail!", throwable))
        }
    })

    awaitClose {
        call.cancel()
    }
}

fun <T : Any> KtCall<T>.asFlow(): Flow<T> = callbackFlow {
    val call = call(object : Callback<T> {
        override fun onSuccess(data: T) {
            trySendBlocking(data)
                .onSuccess { close() }
                .onFailure {
                    cancel(CancellationException("Req"))
                }
        }

        override fun onFail(throwable: Throwable) {
            cancel(CancellationException("Request fail!", throwable))
        }
    })

    awaitClose {
        call.cancel()
    }
}

fun main() = runBlocking {
    testAsFlow()
}

private suspend fun testAsFlow() =
    KtHttpV5.create(ApiServiceV5::class.java)
        .repos(lang = "Kotlin", since = "weekly")
        .asFlow1()
        .catch { println("Catch: $it") }
        .collect {
            println(it)
        }

