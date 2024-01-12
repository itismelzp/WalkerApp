/***********************************************************
 * Copyright (C), 2010-2020, OPLUS Mobile Comm Corp., Ltd.
 * VENDOR_EDIT
 * File:  - IFaceDetect
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

interface IFaceDetect {

    fun init(): Boolean
    fun detect(bitmap: Bitmap): List<Rect>?
    fun release()
}