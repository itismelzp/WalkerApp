package com.demo.coroutine

import android.app.job.JobService
import androidx.work.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseJobService : JobService() {

    val normalScope = NormalScope()

    init {
        val builder = Configuration.Builder()
        builder.setJobSchedulerJobIdRange(1000, 10000)
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