package com.demo.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.demo.base.log.MyLog
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Created by lizhiping on 2023/3/17.
 * <p>
 * description
 */
class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    companion object {
        const val TAG = "UploadWorker"
        val ID: UUID = UUID.randomUUID()
        const val PROGRESS = "PROGRESS"
    }

    override fun doWork(): Result {
        return try {

            for (i in 1..100) {
                MyLog.i(TAG, "[uploadImages] i: $i")
                setProgressAsync(workDataOf(PROGRESS to i))
                TimeUnit.MILLISECONDS.sleep(100L)
            }
            makeStatusNotification("upload images finished", applicationContext)
            Result.success()
        } catch (t: Throwable) {
            makeStatusNotification("upload images finished", applicationContext)
            Result.failure()
        }
    }

}