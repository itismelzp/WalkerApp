/*********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: - ViewData.kt
 ** Description:1.id用来持有model的path，如/local/selfalbum/-***
 **             2.modelType用来持有model对应的type值如：/Local/LocalAlbum
 ** Version: 1.0
 ** Date: 2020/07/24
 ** Author: dengchukun@Apps.Gallery
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 ** dengchukun@Apps.Gallery	2020/07/24		1.0		create
 *********************************************************************************/
package com.demo.album

open class ViewData(
    var id: String,
    var position: Int,
    var modelType: String
) {

    /**
     * position和version可以不相同，但其他的都相同，我们认为两个item内容相同,此函数主要用于拖拽position变更操作判断
     */
    open fun isContentEquals(other: ViewData?): Boolean {
        other ?: return false
        return (this.id == other.id) &&
                (this.modelType == other.modelType)
    }

    /**
     * 从界面的原始数据层面（从MediaSet/MediaItem中获取的数据）判断是否需要更新UI。
     * 主要判断的是界面因素：个数、标题、子标题、描述、缩图等，具体看不同子类实现
     */
    open fun needUpdateUI(other: ViewData?): Boolean = false

    /**
     * 从对象的角度判断两个ViewData的thumbnail对象是否为同一个。
     * 虽然两个ViewData的id相同但因为涉及到thumbnail被回收或还未被初始化而得到null对象，所以需要比较一下
     */
    open fun isThumbnailSame(other: ViewData?): Boolean = false
}