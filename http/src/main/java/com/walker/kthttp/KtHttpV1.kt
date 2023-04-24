package com.walker.kthttp

import com.google.gson.Gson
import com.walker.kthttp.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by lizhiping on 2023/4/23.
 * <p>
 * description
 */

interface ApiService {
    @GET("/repo")
    fun repos(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): RepoList
}

data class RepoList(
    var count: Int?,
    var items: List<Repo>?,
    var msg: String?
)

data class Repo(
    var add_starts: String?,
    var avatars: List<String>?,
    var desc: String?,
    var forks: String?,
    var lang: String?,
    var repo: String?,
    var repo_link: String?,
    var starts: String?
)

object KtHttpV1 {
    private var okHttpClient: OkHttpClient = OkHttpClient()
    private var gson: Gson = Gson()
//    var baseUrl = "https://trendings.herokuapp.com"

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf(service)
        ) { _, method, args->
            val annotations = method.annotations
            for (annotation in annotations) {
                if (annotation is GET) {
                    val url = BASE_URL + annotation.value
                    return@newProxyInstance invoke(url, method, args!!)
                }
            }
            return@newProxyInstance null
        } as T
    }

    private fun invoke(path: String, method: Method, args: Array<Any>): Any? {
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
        val response = okHttpClient.newCall(request).execute()

        val genericReturnType = method.genericReturnType
        val body = response.body
        val json = body?.string()
        return gson.fromJson<Any?>(json, genericReturnType)
    }

}

fun main() {
    val api: ApiService = KtHttpV1.create(ApiService::class.java)
    val data: RepoList = api.repos(lang = "Kotlin", since = "weekly")
    println(data)
}