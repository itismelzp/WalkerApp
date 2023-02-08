/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - ExplorerLabelMemoriesLiveBindingData.kt
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

package com.demo.album

import androidx.lifecycle.MutableLiveData

data class ExplorerCardLiveBindingData(
    val count: MutableLiveData<Int> = MutableLiveData(0),
    val datas: MutableLiveData<List<AlbumBindingData>> = MutableLiveData(emptyList())
)