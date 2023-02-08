/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - LabelLayout.kt
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

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.demo.R
import com.demo.album.SingleLineLayout.Companion.HORIZONTAL

class ExplorerBlockLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    val tvLeftTitle: TextView
    val tvRightTitle: TextView
    val slSingleLine: SingleLineLayout
    val rvGallery: RecyclerView
    val titleContainer: ViewGroup
    private val orientationId: Int
    private val gapId: Int
    private val maxCountId: Int

    init {
        LayoutInflater.from(context).inflate(R.layout.explore_block_layout, this, true)
        titleContainer = findViewById(R.id.main_single_column_container_title)
        tvLeftTitle = findViewById(R.id.main_left_title)
        tvRightTitle = findViewById(R.id.main_right_title)
        slSingleLine = findViewById(R.id.main_single_line)
        rvGallery = findViewById(R.id.rv_gallery)
        val a = context.obtainStyledAttributes(attrs, R.styleable.ExplorerBlockLayout, defStyleAttr, defStyleRes)
        val leftTitle = a.getText(R.styleable.ExplorerBlockLayout_leftText)
        val rightTitle = a.getText(R.styleable.ExplorerBlockLayout_rightText)
        val rightTitleVisibility = a.getInt(R.styleable.ExplorerBlockLayout_rightTextVisibility, 0)
        gapId = a.getResourceId(R.styleable.ExplorerBlockLayout_itemGap, Resources.ID_NULL)
        maxCountId = a.getResourceId(R.styleable.ExplorerBlockLayout_maxCnt, Resources.ID_NULL)
        orientationId = a.getResourceId(R.styleable.ExplorerBlockLayout_android_orientation, Resources.ID_NULL)
        if (orientationId == Resources.ID_NULL) {
            slSingleLine.orientation = a.getInt(R.styleable.ExplorerBlockLayout_android_orientation, HORIZONTAL)
        }
        a.recycle()

        tvLeftTitle.text = leftTitle
        tvRightTitle.text = rightTitle
        tvRightTitle.visibility = VISIBILITY_FLAGS[rightTitleVisibility]
        refreshSingleLine()
    }

    private fun refreshSingleLine() {
        if (gapId != Resources.ID_NULL) {
            slSingleLine.gap = resources.getDimension(gapId)
        }
        if (maxCountId != Resources.ID_NULL) {
            slSingleLine.maxCount = resources.getInteger(maxCountId)
        }
        if (orientationId != Resources.ID_NULL) {
            slSingleLine.orientation = resources.getInteger(orientationId)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        refreshSingleLine()
    }

    companion object {
        private val VISIBILITY_FLAGS = intArrayOf(VISIBLE, INVISIBLE, GONE)
    }
}