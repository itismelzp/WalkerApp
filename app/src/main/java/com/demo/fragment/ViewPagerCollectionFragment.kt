package com.demo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.album.GalleryFragment
import com.demo.base.BaseFragment
import com.demo.databinding.FragmentViewPager2DemoBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerCollectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerCollectionFragment : BaseFragment<FragmentViewPager2DemoBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var pageList = mutableListOf(
        AlbumSlideShowFragment.newInstance(),
        NormalViewPagerFragment.newInstance(),
        GalleryFragment.newInstance()
    )

    override fun createFragment(arg1: String, arg2: String) = newInstance("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) =  FragmentViewPager2DemoBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager.apply {
            adapter = DemoCollectionAdapter(this@ViewPagerCollectionFragment, pageList)
            isSaveEnabled = false // 解决Fragment嵌套崩溃问题
        }
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${position + 1}"
        }.attach()
    }

    class DemoCollectionAdapter(
        fragment: Fragment,
        private val pageList: List<BaseFragment<out ViewBinding>>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = pageList.size

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            val fragment = pageList[position]
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }

    companion object {

        private const val ARG_OBJECT = "object"

        @JvmStatic
        fun newInstance() = ViewPagerCollectionFragment().apply {
            arguments = Bundle().apply {}
        }

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewPagerCollectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}