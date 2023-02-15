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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ImagePagerFragment : BaseFragment<FragmentImagePagerBinding>() {

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
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

                // TODO::改造成viewpager2后，这里(instantiateItem)的替换方案是个问题！！！
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

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImagePagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentImagePagerBinding.inflate(inflater, container, attachToRoot)

    override fun createFragment(): BaseFragment<FragmentImagePagerBinding> {
        return newInstance()
    }

    override fun createFragment(arg1: String, arg2: String): BaseFragment<FragmentImagePagerBinding> {
        return newInstance(arg1, arg2)
    }

}