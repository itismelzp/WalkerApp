/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - MutiLiveData.kt
 * Description:
 *
 * Version: 1.0
 * Date: 2022/03/08
 * Author: hucanhua@Apps.Gallery
 * TAG: OPLUS_ARCH_EXTENDS
 * ------------------------------- Revision History: ----------------------------
 * <author>						<date>			<version>		<desc>
 * ------------------------------------------------------------------------------
 * hucanhua@Apps.Gallery		2022/03/08		1.0			OPLUS_ARCH_EXTENDS
 ******************************************************************************/

package com.demo.album.binding

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class MultiLiveData {
    private val sources = mutableMapOf<LiveData<*>, Observer<*>>()

    @MainThread
    fun <S> addSource(@NonNull source: LiveData<S>, @NonNull onChanged: Observer<S>) {
        sources.putIfAbsent(source, onChanged)
    }

    @MainThread
    fun <S> removeSource(@NonNull source: LiveData<S>) {
        unObserve()
        sources.remove(source)
    }

    @Suppress("UNCHECKED_CAST")
    @MainThread
    fun removeAll() {
        sources.forEach {
            it.key.removeObserver(it.value as Observer<in Any>)
        }
        sources.clear()
    }

    @Suppress("UNCHECKED_CAST")
    @MainThread
    fun observe(owner: LifecycleOwner) {
        sources.forEach {
            it.key.observe(owner, it.value as Observer<in Any>)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @MainThread
    fun unObserve() {
        sources.forEach {
            it.key.removeObserver(it.value as Observer<in Any>)
        }
    }
}