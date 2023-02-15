package com.demo.album

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.customview.utils.ViewUtils

/**
 * Created by lizhiping on 2023/2/15.
 * <p>
 * description
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    companion object {
        private const val UN_SET_BOTH_END = -1
    }

    private var firstSpace: Int = UN_SET_BOTH_END
    private var lastSpace: Int = UN_SET_BOTH_END
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

    constructor(
        space: Int,
        firstSpace: Int = UN_SET_BOTH_END,
        lastSpace: Int = UN_SET_BOTH_END
    ) {
        this.leftSpace = space
        this.topSpace = space
        this.rightSpace = space
        this.bottomSpace = space
        this.firstSpace = firstSpace
        this.lastSpace = lastSpace
    }

    constructor(
        leftSpace: Int, topSpace: Int, rightSpace: Int, bottomSpace: Int,
        firstSpace: Int = UN_SET_BOTH_END,
        lastSpace: Int = UN_SET_BOTH_END
    ) {
        this.leftSpace = leftSpace
        this.topSpace = topSpace
        this.rightSpace = rightSpace
        this.bottomSpace = bottomSpace
        this.firstSpace = firstSpace
        this.lastSpace = lastSpace
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
        if (parent.getChildAdapterPosition(view) == 0 && firstSpace != UN_SET_BOTH_END) {
            outRect.left = firstSpace
        }
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1 && firstSpace != UN_SET_BOTH_END) {
            outRect.right = lastSpace
        }
    }

}