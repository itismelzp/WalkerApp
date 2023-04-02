package com.demo.work

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.demo.constant.Constant
import java.util.concurrent.TimeUnit

/**
 * Created by lizhiping on 2023/3/19.
 * <p>
 * description
 */
class WorkerViewModel(application: Application) : AndroidViewModel(application) {

    val uploadImagesWorkInfo: LiveData<WorkInfo>
    val uploadMetaWorkInfo: LiveData<WorkInfo>
    private val workManager = WorkManager.getInstance(application)

    init {
        uploadImagesWorkInfo = workManager.getWorkInfoByIdLiveData(UploadWorker.ID)
        uploadMetaWorkInfo = workManager.getWorkInfoByIdLiveData(UploadMetaDataWorker.ID)
    }

    fun cancelWork() {
        workManager.cancelUniqueWork(Constant.UPLOAD_WORK_NAME)
    }

    fun uploadImages() {
        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>()
                .addTag(UploadWorker.TAG)
                .setId(UploadWorker.ID)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .setRequiresCharging(true)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL, 10_000L,
                    TimeUnit.MILLISECONDS
                )
                .build()

        val uploadMetaDataWorker: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadMetaDataWorker>()
                .addTag(UploadMetaDataWorker.TAG)
                .setId(UploadMetaDataWorker.ID)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .build()

        workManager
            .beginUniqueWork(
                Constant.UPLOAD_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                uploadWorkRequest
            )
            .then(uploadMetaDataWorker)
            .enqueue()
    }

}