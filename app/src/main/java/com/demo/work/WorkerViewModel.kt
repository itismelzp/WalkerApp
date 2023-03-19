package com.demo.work

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.demo.constant.Constant

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

}