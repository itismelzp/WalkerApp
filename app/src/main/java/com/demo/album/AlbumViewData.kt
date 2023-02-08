/********************************************************************************
 ** Copyright (C), 2019-2029, OPLUS Mobile Comm Corp., Ltd
 ** All rights reserved.
 **
 ** File: AlbumView.kt
 ** Description:1.isMediaAlbum用来表示其内部是否为图集列表页还是图集页。true:图集页，false:图集列表页
 ** Version: 1.0
 ** Date : 2020/7/29
 ** Author: Huaqiao.Yin@Apps.Gallery3D
 ** TAG: OPLUS_ARCH_EXTENDS
 ** ------------------------------- Revision History: ----------------------------
 ** <author>                        <date>       <version>    <desc>
 ** ------------------------------------------------------------------------------
 **  Huaqiao.Yin@Apps.Gallery3D     2020/7/29        build this module
 ********************************************************************************/
package com.demo.album

import android.graphics.drawable.Drawable
import android.os.Bundle

open class AlbumViewData(
    id: String,
    position: Int,
    modelType: String,
    var isMediaAlbum: Boolean,
    var version: Long,
    var totalCount: Int = 0,
    var title: String? = null,
    var subTitle: String? = null,
    var description: String? = null,
    var thumbnail: Drawable? = null,
    var specifiedCount: Bundle? = null,
    var supportedAbilities: Bundle? = null
) : ViewData(id, position, modelType) {

    companion object {
        const val SUPPORT_IS_SELF_ALBUM = "support_is_self_album"
        const val SUPPORT_IS_THIRD_APP_ALBUM = "support_is_third_app_album"

        const val SUPPORT_COVER_ROTATION = "support_cover_rotation"

        const val SUPPORT_COVER_CROP_RECT = "cropRectKey"

        /**
         * 其他图集、人物封面图的Path.string
         * 非人物的图集切换封面时要么是个数变化（version会变化）要么是version有变化，都会引起缩图重新加载。
         * 但是其他图集（删除封面）、人物封面切换之后，其图集的个数、id、标题等都没有变化，所以会需要添加一个封面id用来控制该图集重新加载缩图。
         */
        const val SUPPORT_COVER_ID = "support_cover_id"

        /**
         * 用于仅有一个缩图封面的图集
         * 注意：图片换封面或者编辑功能（新增文件）都会导致封面path变化，但超级文本编辑是原图修改，文件修改时间也保持不变
         */
        const val SUPPORT_COVER_DATE_MODIFIED_IN_SEC = "support_cover_date_modified_in_sec"
        const val SUPPORT_COVER_FILE_SIZE = "support_cover_file_size"

        const val SUPPORT_EXTRA_NAMES = "support_extra_names"
        const val SUPPORT_SHOW_RED_DOT = "support_show_red_dot"

    }

    /**
     * 重写equals方法
     * <pre>
     * 不要写成fun equals(other: AlbumViewData?), 如果写成这样那就需要手动调用该方法。
     * 这样将无法使用Objects.equals方法，也无法使用kt中的==或！=，因为你这就是一个自己写的方法，只是刚好名称叫做equals
     * </pre>
     */
    override fun equals(other: Any?): Boolean {
        if (other !is AlbumViewData) return false
        return !((id != other.id) ||
            (position != other.position) ||
            (totalCount != other.totalCount) ||
            (title != other.title) ||
            (subTitle != other.subTitle) ||
            (description != other.description)
            // (specifiedCount != other.specifiedCount) ||  // need?
            // (supportedAbilities != other.supportedAbilities) ||  // need?
            )
    }

    /**
     * position和version可以不相同，但其他的都相同，我们认为两个item内容相同,此函数主要用于拖拽position变更操作判断
     */
    override fun isContentEquals(other: ViewData?): Boolean {
        if (other !is AlbumViewData) return false
        return !((id != other.id) ||
            (totalCount != other.totalCount) ||
            (title != other.title) ||
            (subTitle != other.subTitle) ||
            (description != other.description) ||
            (supportedAbilities?.get(SUPPORT_SHOW_RED_DOT) != other.supportedAbilities?.get(SUPPORT_SHOW_RED_DOT)))
    }

    /**
     * 从界面的原始数据层面（从MediaSet/MediaItem中获取的数据）判断是否需要更新UI。
     * 主要判断的是界面因素：图片路径（id）、个数、标题、子标题、描述、是否封面旋转等
     */
    override fun needUpdateUI(other: ViewData?): Boolean {
        if (other !is AlbumViewData) return true
        return ((id != other.id) ||
            (totalCount != other.totalCount) ||
            (title != other.title) ||
            (subTitle != other.subTitle) ||
            (description != other.description) ||
            (supportedAbilities?.get(SUPPORT_COVER_ROTATION) != other.supportedAbilities?.get(SUPPORT_COVER_ROTATION)) ||
            (supportedAbilities?.get(SUPPORT_COVER_CROP_RECT) != other.supportedAbilities?.get(SUPPORT_COVER_CROP_RECT)) ||
            (supportedAbilities?.get(SUPPORT_COVER_ID) != other.supportedAbilities?.get(SUPPORT_COVER_ID)) ||
            (supportedAbilities?.get(SUPPORT_COVER_DATE_MODIFIED_IN_SEC) != other.supportedAbilities?.get(SUPPORT_COVER_DATE_MODIFIED_IN_SEC)) ||
            (supportedAbilities?.get(SUPPORT_COVER_FILE_SIZE) != other.supportedAbilities?.get(SUPPORT_COVER_FILE_SIZE)) ||
            (supportedAbilities?.get(SUPPORT_SHOW_RED_DOT) != other.supportedAbilities?.get(SUPPORT_SHOW_RED_DOT))
            )
    }

    /**
     * 从对象的角度判断两个ViewData的thumbnail对象是否为同一个。
     * 虽然两个ViewData的id相同但因为涉及到thumbnail被回收或还未被初始化而得到null对象，所以需要比较一下
     */
    override fun isThumbnailSame(other: ViewData?): Boolean {
        if (other !is AlbumViewData) return false
        return true
    }

    override fun toString(): String {
        return "version: $version, pos: $position, title: $title, count: $totalCount, id: $id modelType:$modelType"
    }

    /**
     * 重写了equals之后，会需要对应的重写hashCode方法，否则代码质量编译不过关。
     * <pre>
     *     Effective Java 中对采用31做了说明：
     *     之所以使用 31， 是因为他是一个奇素数。如果乘数是偶数，并且乘法溢出的话，信息就会丢失，因为与2相乘等价于移位运算（低位补0）。
     *     使用素数的好处并不很明显，但是习惯上使用素数来计算散列结果。
     *     31 有个很好的性能，即用移位和减法来代替乘法，可以得到更好的性能： 31 * i == (i << 5）- i， 现代的 VM 可以自动完成这种优化
     * </pre>
     */
    override fun hashCode(): Int {
        var result = isMediaAlbum.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + totalCount
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (subTitle?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }

}

