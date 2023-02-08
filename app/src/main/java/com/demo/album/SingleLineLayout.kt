/*******************************************************************************
 * Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 * All rights reserved.
 *
 * File: - SingleLineLayout.kt
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
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import androidx.core.view.forEach
import kotlin.math.min

class SingleLineLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    var gap = 0f
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }
    var maxCount = 0
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    @OrientationMode
    var orientation = HORIZONTAL
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }

    var onItemClick: ((Int) -> Unit)? = null

    private val onClickListener = OnClickListener {
        onItemClick?.invoke(indexOfChild(it))
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.setOnClickListener(onClickListener)
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        child?.setOnClickListener(null)
    }

    override fun addView(child: View?) {
        super.addView(child)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        if (childCount <= 0) {
            setMeasuredDimension(w, paddingTop + paddingBottom)
            return
        }
        if (orientation == HORIZONTAL) {
            val h = MeasureSpec.getSize(heightMeasureSpec)
            val hMode = MeasureSpec.getMode(heightMeasureSpec)
            val childWidthSpec = MeasureSpec.makeMeasureSpec(
                ((w - (maxCount - 1) * gap) / maxCount).toInt(),
                MeasureSpec.EXACTLY
            )
            val childHeightSpec = MeasureSpec.makeMeasureSpec(h, hMode)
            var maxHeight = 0
            for (index in 0 until min(childCount, maxCount)) {
                val child = getChildAt(index)
                measureChildWithMargins(child, childWidthSpec, paddingStart + paddingEnd, childHeightSpec, paddingTop + paddingBottom)
                maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
            }
            setMeasuredDimension(w, maxHeight)
        } else {
            var totalHeight = paddingTop + paddingBottom + ((childCount - 1) * gap).toInt()
            for (index in 0 until min(childCount, maxCount)) {
                val child = getChildAt(index)
                val childWidthSpec = MeasureSpec.makeMeasureSpec(w, wMode)
                measureChildWithMargins(child, childWidthSpec, paddingStart + paddingEnd, heightMeasureSpec, paddingTop + paddingBottom)
                totalHeight += child.measuredHeight
            }
            setMeasuredDimension(w, totalHeight)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (maxCount <= 0) return
        val w = right - left
        val h = bottom - top
        val isLayoutRtl: Boolean = layoutDirection == LayoutDirection.RTL
        if (orientation == HORIZONTAL) {
            val childTop = 0
            val childWidth = ((w - paddingStart - paddingEnd - (maxCount - 1) * gap) / maxCount).toInt()
            var childStart = (if (isLayoutRtl) w - paddingRight else paddingLeft).toFloat()
            forEach { child ->
                if (isLayoutRtl) {
                    child.layout((childStart - childWidth).toInt(), childTop, childStart.toInt(), childTop + h)
                    childStart -= childWidth + gap
                } else {
                    child.layout(childStart.toInt(), childTop, (childStart + childWidth).toInt(), childTop + h)
                    childStart += childWidth + gap
                }
            }
        } else {
            var childTop = 0f
            val childStart = paddingStart
            val childRight = w - childStart - paddingEnd
            forEach { child ->
                val childHeight = child.measuredHeight
                child.layout(childStart, childTop.toInt(), childRight, (childTop + childHeight).toInt())
                childTop += childHeight + gap
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    @IntDef(HORIZONTAL, VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OrientationMode

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}