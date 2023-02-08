/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - AutoImageView.kt
 * Description: 响应maxHeight，maxHeight不得超过控件的宽度
 *
 * Version: 1.0
 * Date: 2022/03/22
 * Author: hucanhua@Apps.Gallery
 * TAG: OPLUS_ARCH_EXTENDS
 * ------------------------------- Revision History: ----------------------------
 * <author>						<date>			<version>		<desc>
 * ------------------------------------------------------------------------------
 * hucanhua@Apps.Gallery		2022/03/22		1.0			OPLUS_ARCH_EXTENDS
 ******************************************************************************/

package com.demo.album

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

class MaxHeightRectImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val realHeightSpec = MeasureSpec.makeMeasureSpec(
            min(maxHeight, MeasureSpec.getSize(widthMeasureSpec)),
            MeasureSpec.getMode(widthMeasureSpec)
        )
        super.onMeasure(widthMeasureSpec, realHeightSpec)
    }
}