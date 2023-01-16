package com.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.demo.R
import com.demo.databinding.FragmentImagePagerBinding
import com.demo.fragment.adapter.ImagePagerAdapter
import com.demo.logger.MyLog


/**
 * A simple [Fragment] subclass.
 * Use the [ImagePagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImagePagerFragment : Fragment() {

    private lateinit var binding: FragmentImagePagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentImagePagerBinding.inflate(inflater, container, false)
        binding.viewPager.apply {
            adapter = ImagePagerAdapter(this@ImagePagerFragment)
            currentItem = GridFragment.currentPosition
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    GridFragment.currentPosition = position
                }
            })
        }

        prepareSharedElementTransition()
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }
        MyLog.i(TAG, "[onCreateView]")
        return binding.root
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        // This will be done when entering the ImagePagerFragment.
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                // 拿到当前viewPager页的视图
                val currentFragment =
                    binding.viewPager.adapter?.instantiateItem(
                        binding.viewPager,
                        GridFragment.currentPosition
                    ) as ImageFragment
                val binding = currentFragment.binding
                sharedElements[names[0]] = binding.image
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        MyLog.i(TAG, "[onDestroy]")
    }

    companion object {

        private const val TAG = "ImagePagerFragment"

        @JvmStatic
        fun newInstance() = ImagePagerFragment().apply {
            arguments = Bundle().apply {}
        }
    }

}