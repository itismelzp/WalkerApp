package com.demo.album

import android.app.Application
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.demo.MyApplication
import com.demo.R
import com.demo.datecenter.ImageDataProvider
import kotlinx.coroutines.*

class ExplorerViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    companion object {
        private const val LOCATION_THUMB_COUNT = 4
        private const val MEMORIES_THUMB_COUNT = 4
        private const val MEMORIES_ALL_THUMB_COUNT = MEMORIES_THUMB_COUNT * 3
        private const val LABEL_THUMB_COUNT = 10
    }

    private var labelViewDataArrays: Array<AlbumViewData?> = arrayOfNulls(LABEL_THUMB_COUNT)
    private var memoriesViewDataArrays: Array<AlbumViewData?> = arrayOfNulls(MEMORIES_THUMB_COUNT)

    val labelBindingData = ExplorerCardLiveBindingData()
    val memoriesBindingData = ExplorerBlockLiveBindingData()

    val multiChoiceNessBlockLiveData = MutableLiveData<MutableList<AlbumBindingData>>()
    val multiAllBlockLiveData = MutableLiveData<MutableList<AlbumBindingData>>()

    fun loadData() {
        loadLabelData()
        loadMemoryData()
    }

    private fun loadLabelData() {
        viewModelScope.launch(Dispatchers.IO) {
            // label
            loadAlbumSetModelData(
                maxReloadCount = LABEL_THUMB_COUNT,
                viewDataArrays = labelViewDataArrays,
                contentCallback = { index, viewData ->
                    if (index < 5) {
                        labelBindingData.datas.postValue(labelViewDataArrays.filterNotNull().map {
                            AlbumBindingData(
                                it.thumbnail,
                                it.title,
                                it.totalCount.toString()
                            )
                        })
                    }
                }
            )
        }
    }

    private fun loadMemoryData() {

        viewModelScope.launch(Dispatchers.IO) {
            loadChoicenessMemoryData()
            loadAllMemoryData()
//            loadMemoryData()
        }
    }

    fun queryLabelViewData(index: Int): AlbumViewData? {
        return labelViewDataArrays.getOrNull(index)
    }

    private fun loadChoicenessMemoryData() {
        val viewData =
            ImageDataProvider.getRepeatableImages(MEMORIES_THUMB_COUNT).mapIndexed { index, resId ->
                AlbumBindingData(
                    ResourcesCompat.getDrawable(
                        MyApplication.instance.resources,
                        resId,
                        null
                    ), "幸福时刻$index", "2021-2012年", false
                )
            } as MutableList<AlbumBindingData>

        multiChoiceNessBlockLiveData.postValue(viewData)
    }

    private fun loadAllMemoryData() {
        val viewData =
            ImageDataProvider.getRepeatableImages(MEMORIES_ALL_THUMB_COUNT).mapIndexed { index, resId ->
                AlbumBindingData(
                    ResourcesCompat.getDrawable(
                        MyApplication.instance.resources,
                        resId,
                        null
                    ),
                    "幸福时刻$index",
                    "2021-2012年",
                    false
                )
            } as MutableList<AlbumBindingData>
        multiAllBlockLiveData.postValue(viewData)
    }

    private fun loadMemory() {
        // memories
        loadAlbumSetModelData(
            maxReloadCount = MEMORIES_THUMB_COUNT,
            viewDataArrays = memoriesViewDataArrays,
            countGotCallback = { count ->
                memoriesBindingData.count.postValue(count)
            },
            contentCallback = { index, viewData ->
//                    viewData?.modelType = TYPE_MEMORIES_ALBUM // fixme Dandy 删除，Model层暂时没有实现这部分,所以先加在此处便于View层调用
                if (index < 5) {
                    memoriesBindingData.datas.postValue(
                        memoriesViewDataArrays.filterNotNull().map {
                            AlbumBindingData(it.thumbnail, it.title, it.subTitle, false)
                        })
                }
            },
        )
    }

    private fun loadAlbumSetModelData(
        bundle: Bundle = Bundle(),
        maxReloadCount: Int,
        viewDataArrays: Array<AlbumViewData?>,
        countGotCallback: ((count: Int) -> Unit)? = null,
        contentCallback: ((index: Int, viewData: AlbumViewData?) -> Unit)? = null,
        beforeUpdateCallback: ((count: Int) -> Unit)? = null
    ) {
        val viewData: Array<AlbumViewData?> = arrayOfNulls(MEMORIES_THUMB_COUNT)

        for (index in 0 until MEMORIES_THUMB_COUNT) {
            val data = AlbumViewData(
                id = "",
                position = 0,
                modelType = "",
                isMediaAlbum = true,
                version = 0,
                totalCount = 3,
                title = "title: $index",
                subTitle = "subtitle: $index",
                thumbnail = ResourcesCompat.getDrawable(
                    MyApplication.instance.resources,
                    R.drawable.zhaoyun,
                    null
                )
            )
            viewData[index] = data
        }

        for (index in 0 until MEMORIES_THUMB_COUNT) {
            contentCallback?.invoke(index, viewData[index])
        }

    }

}