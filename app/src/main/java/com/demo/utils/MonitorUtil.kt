package com.demo.utils

import android.os.Looper
import android.util.Log
import android.view.Choreographer

/**
 * Created by lizhiping on 2023/1/18.
 * <p>
 * description
 */
class MonitorUtil {

    companion object {

        private const val TAG = "MonitorUtil"
        private var mLastFrameNanos: Long = 0L
        private const val NANO_UNIT = 1000000L

        fun initMonitor() {

            // 1) Looper方案--BlockCanary
            Looper.getMainLooper().setMessageLogging { s: String ->
                // >>>>> Dispatching to
                // <<<<< Finished to
                Log.d(TAG, "[println] s: $s")
            }

            // 2) Choreographer方案--ArgusAPM、LogMonitor
            Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
                override fun doFrame(frameTimeNanos: Long) {
                    if (mLastFrameNanos == 0L) {
                        mLastFrameNanos = frameTimeNanos
                    }
                    if (frameTimeNanos - mLastFrameNanos > 100) {
                        //
                    }
                    Log.i(
                        TAG,
                        "[doFrame] time gap: " + (frameTimeNanos - mLastFrameNanos).toFloat() / NANO_UNIT + "ms"
                    )
                    mLastFrameNanos = frameTimeNanos
                    Choreographer.getInstance().postFrameCallback(this)
                }
            })

            // 1) + 2)--Matrix
        }
    }

}