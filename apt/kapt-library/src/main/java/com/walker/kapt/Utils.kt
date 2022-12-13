package com.walker.kapt

import android.app.Activity
import android.view.View

object Utils {

    fun <T : View> findViewById(activity: Activity?, viewId: Int): T? {
        return activity?.findViewById(viewId) as T?
    }
}