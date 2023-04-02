package com.demo.utils

import android.content.ContentResolver.SCHEME_FILE
import android.content.Context
import android.content.Intent.normalizeMimeType
import android.net.Uri
import android.webkit.MimeTypeMap


object MimeTypeUtils {
    const val MIME_TYPE_IMAGE_PREFIX = "image/"
    const val MIME_TYPE_VIDEO_PREFIX = "video/"
    const val MIME_TYPE_AUDIO_PREFIX = "audio/"

    const val MIME_TYPE_ANY = "*/*"
    const val MIME_TYPE_IMAGE_ANY = "image/*"
    const val MIME_TYPE_VIDEO_ANY = "video/*"
    const val MIME_TYPE_AUDIO_ANY = "audio/*"
    const val MIME_TYPE_IMAGE_JPEG = "image/jpeg"
    const val MIME_TYPE_IMAGE_PNG = "image/png"
    const val MIME_TYPE_IMAGE_WEBP = "image/webp"
    const val MIME_TYPE_IMAGE_GIF = "image/gif"
    const val MIME_TYPE_IMAGE_HEIF = "image/heif"
    const val MIME_TYPE_IMAGE_HEIC = "image/heic"
    const val MIME_TYPE_IMAGE_RAW = "image/x-adobe-dng"
    const val MIME_TYPE_IMAGE_ICON = "image/x-icon"
    const val MIME_TYPE_IMAGE_BMP = "image/bmp"
    const val MIME_TYPE_IMAGE_MICROSOFT_BMP = "image/x-ms-bmp"
    const val MIME_TYPE_IMAGE_TIFF = "image/tiff"
    const val MIME_TYPE_IMAGE_AVIF = "image/avif"
    const val MIME_TYPE_VIDEO_MP4 = "video/mp4"
    const val MIME_TYPE_IMAGE_GIF_SUFFIX = "gif"
    const val MIME_TYPE_IMAGE_DNG_SUFFIX = "dng"

    @JvmStatic
    fun isImage(mimeType: String?): Boolean = mimeType?.startsWith(MIME_TYPE_IMAGE_PREFIX, ignoreCase = true) ?: false

    @JvmStatic
    fun isVideo(mimeType: String?): Boolean = mimeType?.startsWith(MIME_TYPE_VIDEO_PREFIX, ignoreCase = true) ?: false

    @JvmStatic
    fun isAudio(mimeType: String?): Boolean = mimeType?.startsWith(MIME_TYPE_AUDIO_PREFIX, ignoreCase = true) ?: false

    @JvmStatic
    fun isJpeg(mimeType: String?): Boolean = MIME_TYPE_IMAGE_JPEG.equals(mimeType, true)

    @JvmStatic
    fun isPng(mimeType: String?): Boolean = MIME_TYPE_IMAGE_PNG.equals(mimeType, true)

    @JvmStatic
    fun isWebp(mimeType: String?): Boolean = MIME_TYPE_IMAGE_WEBP.equals(mimeType, true)

    @JvmStatic
    fun isGif(mimeType: String?): Boolean = MIME_TYPE_IMAGE_GIF.equals(mimeType, true)

    @JvmStatic
    fun isHeifOrHeic(mimeType: String?): Boolean =
        MIME_TYPE_IMAGE_HEIC.equals(mimeType, true) or MIME_TYPE_IMAGE_HEIF.equals(mimeType, true)

    @JvmStatic
    fun isHeic(mimeType: String?): Boolean = MIME_TYPE_IMAGE_HEIC.equals(mimeType, true)

    @JvmStatic
    fun isHeif(mimeType: String?): Boolean = MIME_TYPE_IMAGE_HEIF.equals(mimeType, true)

    @JvmStatic
    fun isRaw(mimeType: String?): Boolean = MIME_TYPE_IMAGE_RAW.equals(mimeType, true) || isRawImageMimeType(mimeType)

    @JvmStatic
    fun isIcon(mimeType: String?): Boolean = MIME_TYPE_IMAGE_ICON.equals(mimeType, true)

    @JvmStatic
    fun isMp4(mimeType: String?): Boolean = MIME_TYPE_VIDEO_MP4.equals(mimeType, true)

    @JvmStatic
    fun isBmp(mimeType: String?): Boolean = MIME_TYPE_IMAGE_BMP.equals(mimeType, true)
            || MIME_TYPE_IMAGE_MICROSOFT_BMP.equals(mimeType, true)

    @JvmStatic
    fun isTiff(mimeType: String?): Boolean = MIME_TYPE_IMAGE_TIFF.equals(mimeType, true)

    @JvmStatic
    fun endsWidthSuffix(maybeSuffix: String?, suffix: String): Boolean = maybeSuffix?.endsWith(suffix) ?: false

    @JvmStatic
    fun getMimeType(context: Context,uri: Uri?): String? {
        uri ?: return null
        if (SCHEME_FILE == uri.scheme) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension.toLowerCase()
            )
            if (type != null) {
                return type
            }
        }
        // Assume the type is image if the type cannot be resolved
        // This could happen for "http" URI.
        return context.contentResolver.getType(uri) ?: MIME_TYPE_IMAGE_ANY
    }

    private fun isRawImageMimeType(mimeType: String?): Boolean {
        return when (normalizeMimeType(mimeType)) {
            "image/x-adobe-dng",
            "image/tiff",
            "image/x-canon-cr2",
            "image/x-nikon-nrw",
            "image/x-sony-arw",
            "image/x-panasonic-rw2",
            "image/x-olympus-orf",
            "image/x-pentax-pef",
            "image/x-samsung-srw",
            "image/x-nikon-nef" -> true
            else -> false
        }
    }
}