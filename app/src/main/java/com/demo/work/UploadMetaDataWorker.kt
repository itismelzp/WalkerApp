package com.demo.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.demo.logger.MyLog
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by lizhiping on 2023/3/17.
 * <p>
 * description
 */
class UploadMetaDataWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val TAG = "UploadMetaDataWorker"
        val ID = UUID.randomUUID()
        const val PROGRESS = "PROGRESS"
    }

    override fun doWork(): Result {
        return try {
            for (i in 1..10) {
                MyLog.i(TAG, "[doWork] i: $i")
                setProgressAsync(workDataOf(PROGRESS to i))
                TimeUnit.MILLISECONDS.sleep(250L)
            }
            Result.success()
        } catch (t: Throwable) {
            Result.failure()
        }
    }

}