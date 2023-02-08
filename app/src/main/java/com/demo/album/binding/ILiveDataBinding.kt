/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - IDataBinding.kt
 * Description:
 *
 * Version: 1.0
 * Date: 2022/03/04
 * Author: hucanhua@Apps.Gallery
 * TAG: OPLUS_ARCH_EXTENDS
 * ------------------------------- Revision History: ----------------------------
 * <author>						<date>			<version>		<desc>
 * ------------------------------------------------------------------------------
 * hucanhua@Apps.Gallery		2022/03/04		1.0			OPLUS_ARCH_EXTENDS
 ******************************************************************************/

package com.demo.album.binding

import androidx.lifecycle.LifecycleOwner

interface ILiveDataBinding<T> {
    fun bind(lifecycleOwner: LifecycleOwner, data: T)

    fun unbind()
}