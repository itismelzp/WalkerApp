package com.demo.album

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView

class CornerImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun setRoundCorner(radius: Int) {
        ViewStyleSetter.applyRoundCorner(this, radius.toFloat())
    }
}
