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

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.demo.album.binding.BindTools
import com.demo.album.binding.IDataBinding
import com.demo.album.binding.ILiveDataBinding
import com.demo.album.binding.MultiLiveData

//import com.oplus.gallery.main_lib.binding.BindTools
//import com.oplus.gallery.main_lib.binding.IDataBinding
//import com.oplus.gallery.main_lib.binding.ILiveDataBinding
//import com.oplus.gallery.main_lib.binding.MultiLiveData
//import com.oplus.gallery.explorepage.bindingdata.AlbumBindingData
//import com.oplus.gallery.explorepage.bindingdata.ExplorerCardLiveBindingData
//import com.oplus.gallery.explorepage.view.ExplorerCardLayout
//import com.oplus.gallery.foundation.util.debug.GLog
//import com.oplus.gallery.foundation.util.debug.GTrace

class ExplorerCardLiveBinding(
    private val view: ExplorerCardLayout,
    private val bindingClass: Class<out IDataBinding<View, AlbumBindingData>>,
    private val childInflate: (parent: ViewGroup) -> View,
    private val rightTitleCondition: () -> Int
) : ILiveDataBinding<ExplorerCardLiveBindingData> {
    private val multiLiveData: MultiLiveData = MultiLiveData()

    override fun bind(lifecycleOwner: LifecycleOwner, data: ExplorerCardLiveBindingData) {
        multiLiveData.removeAll()
        multiLiveData.addSource(data.count) {
//            GLog.d(TAG, "${view.tvLeftTitle.text}, count: $it")
            view.visibility = if (it > 0) View.VISIBLE else View.GONE
            view.tvRightTitle.visibility = if (it > rightTitleCondition()) View.VISIBLE else View.GONE
        }
        multiLiveData.addSource(data.datas) {
//            GTrace.traceBegin("$TAG.${view.tvLeftTitle.text}")
//            GLog.d(TAG, "${view.tvLeftTitle.text}, datas.size: ${it.size}")
            view.slSingleLine.also { singleLine ->
                if ((it.size < singleLine.childCount) || (singleLine.childCount > singleLine.maxCount)) {
                    val start = minOf(singleLine.maxCount, it.size)
                    singleLine.removeViews(start, singleLine.childCount - start)
                } else {
                    for (index in minOf(singleLine.childCount, singleLine.maxCount) until minOf(it.size, singleLine.maxCount)) {
                        singleLine.addView(childInflate(singleLine))
                    }
                }
                for (index in 0 until minOf(singleLine.childCount, singleLine.maxCount)) {
                    BindTools.getDataBinding(bindingClass).bind(singleLine.getChildAt(index), it[index])
                }
            }
//            GTrace.traceEnd()
        }
        multiLiveData.observe(lifecycleOwner)
    }

    companion object {
        private const val TAG = "ExplorerCardLiveBinding"
    }

    override fun unbind() {
        multiLiveData.removeAll()
    }
}