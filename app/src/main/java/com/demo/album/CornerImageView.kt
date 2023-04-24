package com.demo.album

import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

import androidx.appcompat.widget.AppCompatImageView


/**
 * Created by lizhiping on 2023/4/20.
 * <p>
 * description
 */
class CornerImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun setRoundCorner(radius: Int) {
        clipToOutline = true // 用outline裁剪内容区域
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val selfRect = Rect(0, 0, view.width, view.height)
                outline.setRoundRect(selfRect, radius.toFloat())
            }
        }
    }
}
