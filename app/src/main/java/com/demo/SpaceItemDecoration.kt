package com.demo

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.customview.utils.ViewUtils

/**
 * Created by lizhiping on 2023/2/8.
 * <p>
 * description
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    private val leftSpace: Int
    private val topSpace: Int
    private val rightSpace: Int
    private val bottomSpace: Int

    private val defaultSpace = ViewUtils.dpToPx(2f)

    constructor() {
        leftSpace = defaultSpace
        topSpace = ViewUtils.dpToPx(0f)
        rightSpace = defaultSpace
        bottomSpace = ViewUtils.dpToPx(0f)
    }

    constructor(space: Int) {
        this.leftSpace = space
        this.topSpace = space
        this.rightSpace = space
        this.bottomSpace = space
    }

    constructor(leftSpace: Int, topSpace: Int, rightSpace: Int, bottomSpace: Int) {
        this.leftSpace = leftSpace
        this.topSpace = topSpace
        this.rightSpace = rightSpace
        this.bottomSpace = bottomSpace
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
    }

}