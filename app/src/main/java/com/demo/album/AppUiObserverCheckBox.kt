/*********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - AppUiObserverCheckBox.kt
 ** Description :
 ** Version: 1.0
 ** Date: 2021/03/14
 ** Author: vanson.chung@sonicsky.net
 ** TAG: OPLUS_FEATURE_PRIVACY_POLICY_CONTENT_SCROLLVIEW
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 ** vanson.chung@sonicsky.net		2021/03/14   1.0		  build this module
 *********************************************************************************/
package com.demo.album

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

class AppUiObserverCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatCheckBox(context, attrs) {

    // 需利用 res id 来获取主题色改变后的 Drawable
    private var mButtonDrawableResId = -1


    override fun setButtonDrawable(resId: Int) {
        super.setButtonDrawable(resId)
        mButtonDrawableResId = resId
    }
}