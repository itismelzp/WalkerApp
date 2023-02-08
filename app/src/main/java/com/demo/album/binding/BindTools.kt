/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - BindTools.kt
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

package com.demo.album.binding
import androidx.annotation.MainThread
import androidx.collection.LruCache

object BindTools {
    private const val MAX_CACHE_SIZE = 20
    private val cache = LruCache<Class<out IDataBinding<*, *>>, IDataBinding<*, *>>(MAX_CACHE_SIZE)

    @Suppress("UNCHECKED_CAST")
    @MainThread
    fun <T : IDataBinding<*, *>> getDataBinding(clazz: Class<T>): T {
        return (cache.get(clazz) ?: clazz.newInstance().apply { cache.put(clazz, this) }) as T
    }
}