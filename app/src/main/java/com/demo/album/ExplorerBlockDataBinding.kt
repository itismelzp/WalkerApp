/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - AlbumViewBinding.kt
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
import android.widget.ImageView
import android.widget.TextView
import com.demo.R
import com.demo.album.binding.IDataBinding

class ExplorerBlockDataBinding : IDataBinding<View, AlbumBindingData> {
    override fun bind(view: View, data: AlbumBindingData) {
        val ivImage = view.findViewById<ImageView>(R.id.main_explorer_album_set_item_image)
        val tvTitle = view.findViewById<TextView>(R.id.main_explorer_album_set_item_title_text)
        val tvSubTitle = view.findViewById<TextView>(R.id.main_explorer_album_set_item_sub_title_text)
        ivImage.setImageDrawable(data.drawable)
        tvTitle.text = data.title
        tvSubTitle.text = data.subTitle
    }
}