package com.demo.syscomponent

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Configuration
import com.demo.coroutine.BaseJobService
import com.demo.logger.MyLog
import com.demo.utils.KeyguardManagerUtils
import com.demo.utils.PowerManagerUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UploadService : BaseJobService() {

    init {
        val builder = Configuration.Builder()
        builder.setJobSchedulerJobIdRange(0, 1000)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        requestMain {
            withContext(Dispatchers.IO) {
                for (i in 1..1000) {
                    Thread.sleep(100L)
                    MyLog.i(TAG, "[uploadImages] i: $i")
                }
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return !isScreenOnAndKeyguardUnlocked
    }

    companion object {

        private const val TAG = "UploadService"
        fun scheduleJob(context: Context) {
            val jobScheduler = getSystemService(context, JobScheduler::class.java)
            val componentName = ComponentName(context, UploadService::class.java)
            val builder = JobInfo.Builder(1000, componentName)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//            builder.setMinimumLatency(2_1000.toLong())
//            builder.setOverrideDeadline(2_1000.toLong())
            builder.setBackoffCriteria( 5_1000, JobInfo.BACKOFF_POLICY_LINEAR)
            jobScheduler?.schedule(builder.build())
            MyLog.d(TAG, "scheduleJob...")
        }

        @JvmStatic
        val isScreenOnAndKeyguardUnlocked: Boolean
            get() {
                if (PowerManagerUtils.isInteractive() && !KeyguardManagerUtils.isKeyguardLocked()) {
                    MyLog.w(
                        TAG,
                        "isScreenOnAndKeyguardUnlocked, condition unmet. Screen on and keyguard is unlocked."
                    )
                    return true
                }
                return false
            }
    }
}