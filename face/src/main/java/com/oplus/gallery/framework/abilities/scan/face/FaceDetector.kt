/***********************************************************
 * Copyright (C), 2010-2020, OPLUS Mobile Comm Corp., Ltd.
 * VENDOR_EDIT
 * File:  - FaceDetector
 * Description:
 * Version: 1.0
 * Date : 2020/10/02
 * Author: Jun.Cheng@Apps.Gallery3D
 *
 * ---------------------Revision History: ---------------------
 * <author>	<data> 	  <version>	   <desc>
 * Jun.Cheng       2020/10/02    1.0     build this module
 */
package com.oplus.gallery.framework.abilities.scan.face

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.oplus.faceapi.FaceDetect
import com.oplus.faceapi.model.CvPixelFormat
import com.oplus.faceapi.model.FaceConfig
import com.oplus.faceapi.model.FaceInfo
import com.oplus.faceapi.model.FaceOrientation
import com.oplus.faceapi.utils.ColorConvertUtil

class FaceDetector : IFaceDetect {

    companion object {
        private const val PIXEL_BYTE_SIZE = 4
        private const val TAG = "FaceDetector"
    }

    private var mFaceDetect: FaceDetect? = null

    override fun init(): Boolean {
        CvFaceEngine.loadExistedNativeLibs()
        mFaceDetect = FaceDetect(
            null,
            FaceConfig.FaceImageResize.DEFAULT_CONFIG,
            FaceConfig.FaceKeyPointCount.POINT_COUNT_106
        )
        return true
    }

    override fun detect(bitmap: Bitmap): List<Rect>? {
        if (mFaceDetect == null) {
            Log.e(TAG, "[detect] failed! mFaceDetect is null")
            return null
        } else {

            try {
                val width = bitmap.width
                val height = bitmap.height
                val stride = width * PIXEL_BYTE_SIZE
                val bgr = ByteArray(stride * height)
                ColorConvertUtil.getBGRFromBitmap(bitmap, bgr)
                val faceInfos: Array<FaceInfo>? = mFaceDetect!!.detect(
                    bgr, CvPixelFormat.BGR888,
                    width, height, stride, FaceOrientation.UP
                )

                return if (faceInfos.isNullOrEmpty()) {
                    Log.w(TAG, "[detect] no face")
                    null
                } else {
                    val resultList = ArrayList<Rect>()
                    faceInfos.forEach { resultList.add(it.faceRect) }
                    resultList
                }
            } catch (e: Exception) {
                Log.e(TAG, "[detect] failed! exception=$e")
                return null
            }
        }
    }

    override fun release() {
        mFaceDetect?.release()
        mFaceDetect = null
    }
}

