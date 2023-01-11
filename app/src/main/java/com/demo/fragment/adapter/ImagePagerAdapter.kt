package com.demo.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.demo.fragment.ImageFragment
import com.demo.fragment.adapter.ImageData.IMAGE_DRAWABLES

/**
 * Created by lizhiping on 2023/1/10.
 * <p>
 * description
 */
class ImagePagerAdapter(fragment: Fragment) : FragmentStatePagerAdapter(
    fragment.childFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int {
        return IMAGE_DRAWABLES.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(IMAGE_DRAWABLES[position])
    }
}