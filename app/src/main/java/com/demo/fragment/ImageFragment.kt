package com.demo.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.demo.databinding.FragmentImageBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {

    private lateinit var bindig: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        bindig = FragmentImageBinding.inflate(inflater, container, false)

        val imageRes = arguments?.getInt(KEY_IMAGE_RES)
        bindig.image.transitionName = "$imageRes"
        Glide.with(this)
            .load(imageRes)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

            })
            .into(bindig.image)

        return bindig.root
    }

    companion object {
        private const val KEY_IMAGE_RES: String = "com.google.samples.gridtopager.key.imageRes"

        @JvmStatic
        fun newInstance(drawableRes: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_IMAGE_RES, drawableRes)
                }
            }
    }
}