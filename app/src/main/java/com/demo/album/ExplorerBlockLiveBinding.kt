/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - ExplorerLabelLiveBinding.kt
 * Description:
 *
 * Version: 1.0
 * Date: 2022/03/09
 * Author: hucanhua@Apps.Gallery
 * TAG: OPLUS_ARCH_EXTENDS
 * ------------------------------- Revision History: ----------------------------
 * <author>						<date>			<version>		<desc>
 * ------------------------------------------------------------------------------
 * hucanhua@Apps.Gallery		2022/03/09		1.0			OPLUS_ARCH_EXTENDS
 ******************************************************************************/

package com.demo.album

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.demo.MainButton
import com.demo.album.binding.IDataBinding
import com.demo.album.binding.ILiveDataBinding
import com.demo.album.binding.MultiLiveData

class ExplorerBlockLiveBinding(
    private val view: ExplorerBlockLayout,
    private val bindingClass: Class<out IDataBinding<View, AlbumBindingData>>,
    private val childInflate: (parent: ViewGroup) -> View,
    private val rightTitleCondition: () -> Int
) : ILiveDataBinding<ExplorerBlockLiveBindingData> {
    private val multiLiveData: MultiLiveData = MultiLiveData()


    override fun bind(lifecycleOwner: LifecycleOwner, data: ExplorerBlockLiveBindingData) {
        multiLiveData.removeAll()
        multiLiveData.addSource(data.count) {
            view.visibility = if (it > 0) View.VISIBLE else View.GONE
            view.tvRightTitle.visibility = if (it > rightTitleCondition()) View.VISIBLE else View.GONE
        }
        multiLiveData.addSource(data.datas) {
            Log.i(TAG, "view.slSingleLine: ${data.datas}")
            view.slSingleLine.also { singleLine ->
                if ((it.size < singleLine.childCount) || (singleLine.childCount > singleLine.maxCount)) {
                    val start = minOf(singleLine.maxCount, it.size)
                    singleLine.removeViews(start, singleLine.childCount - start)
                } else {
                    for (index in minOf(singleLine.childCount, singleLine.maxCount) until minOf(it.size, singleLine.maxCount)) {
                        singleLine.addView(childInflate(singleLine))
                    }
                }
//                for (index in 0 until minOf(singleLine.childCount, singleLine.maxCount)) {
//                    BindTools.getDataBinding(bindingClass).bind(singleLine.getChildAt(index), it[index])
//                }
            }
        }
        multiLiveData.addSource(data.datas) {
            Log.i(TAG, "view.rvGallery: ${data.datas}")
            view.rvGallery.also { singleLine ->
                (singleLine.adapter as MemoriesListAdapter).submitList(it)
            }
        }
        multiLiveData.observe(lifecycleOwner)

//        multiBlockLiveData.observe(lifecycleOwner) {
//            view.rvGallery.also { recyclerView ->
//                (recyclerView.adapter as MemoriesListAdapter).submitList(it)
//            }
//        }
    }

    companion object {
        private const val TAG = "ExplorerItemLayoutLiveBinding"
    }

    override fun unbind() {
        multiLiveData.removeAll()
    }
}