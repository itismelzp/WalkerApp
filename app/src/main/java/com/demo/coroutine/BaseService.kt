package com.demo.coroutine

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseService : Service() {

    private val normalScope = NormalScope()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        super.onDestroy()
        normalScope.cancel()
    }

    protected fun requestMain(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ) {
        normalScope.launch {
            block.invoke(this)
        }
    }

    protected fun requestIO(
        errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return normalScope.launch(
            Dispatchers.IO
                    + GlobalCoroutineExceptionHandler(errCode, errMsg, report)
        ) {
            block.invoke(this)
        }
    }

    protected fun delayMain(
        delayTime: Long, errCode: Int = -1, errMsg: String = "", report: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ) {
        normalScope.launch(GlobalCoroutineExceptionHandler(errCode, errMsg, report)) {
            withContext(Dispatchers.IO) {
                delay(delayTime)
            }
            block.invoke(this)
        }
    }

    public fun NormalScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

}