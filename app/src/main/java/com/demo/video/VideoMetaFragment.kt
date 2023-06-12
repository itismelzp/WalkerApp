package com.demo.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.databinding.FragmentVideoMetaBinding
import com.demo.base.BaseFragment
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [VideoMetaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoMetaFragment : BaseFragment<FragmentVideoMetaBinding>() {

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        getFirstFrame()
    }

    private fun getFirstFrame() {
        val mmr = MediaMetadataRetriever()
        val file = File("/storage/sdcard/screen-recording-1680254483342.mp4")
        if (file.exists()) {
            mmr.setDataSource(file.absolutePath)
            val bitmap: Bitmap? = mmr.frameAtTime
            if (bitmap != null) {
                binding.ivFirstFrame.setImageBitmap(bitmap)
                toast("获取视频缩略图成功")
            } else {
                toast("获取视频缩略图失败")
            }
        } else {
            toast("文件不存在")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment VideoMetaFragment.
         */
        @JvmStatic
        fun newInstance() = VideoMetaFragment()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentVideoMetaBinding.inflate(inflater, container, attachToRoot)

    override fun createFragment(
        arg1: String,
        arg2: String
    ) = newInstance()
}