package com.demo.coroutine

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class BaseService : Service() {

    private val normalScope = NormalScope()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        super.onDestroy()
        normalScope.cancel()
    }

    protected fun requestMain() {

    }

    @Suppress("FunctionName")
    public fun NormalScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

}