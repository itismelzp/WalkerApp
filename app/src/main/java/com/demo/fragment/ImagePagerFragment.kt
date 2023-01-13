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
        return binding.root
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val currentFragment =
                    binding.viewPager.adapter?.instantiateItem(
                        binding.viewPager,
                        GridFragment.currentPosition
                    ) as Fragment
                val view = currentFragment.view
                view?.let {
                    sharedElements[names[0]] = view.findViewById(R.id.image)
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() = ImagePagerFragment().apply {
            arguments = Bundle().apply {}
        }
    }

}