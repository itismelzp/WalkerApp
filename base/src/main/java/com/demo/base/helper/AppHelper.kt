package com.demo.base.helper

import android.app.Application

/**
 * Created by lizhiping on 2024/1/23.
 * <p>
 * description
 */
object AppHelper {

    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    @JvmStatic
    fun getApp() = app

    @JvmStatic
    fun isDebug() = isDebug
}