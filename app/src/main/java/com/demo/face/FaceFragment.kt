package com.demo.face

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import com.demo.R
import com.demo.base.BaseFragment
import com.demo.base.log.MyLog
import com.demo.databinding.FragmentFaceBinding
import com.oplus.gallery.framework.abilities.scan.face.FaceDetector

class FaceFragment : BaseFragment<FragmentFaceBinding>() {

    private val faceDetector = FaceDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceDetector.init()
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        binding.faceImage.setImageResource(R.drawable.face_1)
        binding.singleFaceBtn.setOnClickListener {
            binding.faceImage.setImageResource(R.drawable.face_1)
            val faceBitmap = binding.faceImage.drawable.toBitmap()
            val detectRect: List<Rect>? = faceDetector.detect(faceBitmap)
            Log.d(TAG, "[setOnClickListener] faceBitmap width: ${faceBitmap.width}, height: ${faceBitmap.height}, detectRect: $detectRect")
            binding.faceImage.setFaceRectList(detectRect, faceBitmap.width, faceBitmap.height)
        }
        binding.doubleFaceBtn.setOnClickListener {
            binding.faceImage.setImageResource(R.drawable.face_2)
            val faceBitmap = binding.faceImage.drawable.toBitmap()
            val detectRect: List<Rect>? = faceDetector.detect(faceBitmap)
            Log.d(TAG, "[setOnClickListener] faceBitmap width: ${faceBitmap.width}, height: ${faceBitmap.height}, detectRect: $detectRect")
            binding.faceImage.setFaceRectList(detectRect, faceBitmap.width, faceBitmap.height)
        }
        MyLog.i(TAG, "[initBaseViews]")
    }

    override fun onDestroy() {
        super.onDestroy()
        MyLog.i(TAG, "[onDestroy]")
    }

    companion object {

        private const val TAG = "FaceFragment"

        @JvmStatic
        fun newInstance() = FaceFragment().apply {
            arguments = Bundle().apply {}
        }

        @JvmStatic
        fun newInstance(param1: String, param2: String) = FaceFragment()
    }

    override fun createFragment() = newInstance()

    override fun createFragment(arg1: String, arg2: String) = newInstance(arg1, arg2)

}