package com.demo.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.demo.R
import com.demo.databinding.FragmentAlbumSlideShowBinding
import com.demo.logger.MyLog

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumSlideShowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumSlideShowFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAlbumSlideShowBinding
    private lateinit var galleryVP: ViewPager2
    private lateinit var indicatorVP: ViewPager2

    private var selectedByGallery = false
    private var selectedByIndicator = false

    private val isAutoPlay = true
    private val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = Runnable { this.handlePosition() }

    override fun createFragment(arg1: String, arg2: String): BaseFragment {
        return newInstance("", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlbumSlideShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        binding.galleryViewPager.apply {
            galleryVP = this
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                clipToPadding = false
            }
            adapter = Adapter(R.layout.item_slide_gallery_pages)
            setPageTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.page_margin)))
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!selectedByIndicator) { // 1）gallery的主动滑动
                        selectedByGallery = true
                        indicatorVP.setCurrentItem(position, true)
                    } else { // 2) gallery的被动滑动
                        selectedByGallery = false
                        selectedByIndicator = false
                    }
                }
            })
        }

        binding.indicatorViewPager.apply {
            indicatorVP = this
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 7
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = resources.getDimensionPixelOffset(R.dimen.halfPageMargin) +
                        resources.getDimensionPixelOffset(R.dimen.peekOffset) * 3
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
            adapter = Adapter(R.layout.item_slide_indicator)
            setPageTransformer(ZoomOutPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!selectedByGallery) { // 1）indicator的主动滑动
                        selectedByIndicator = true
                        galleryVP.setCurrentItem(position, true)
                    } else { // 2) indicator的被动滑动
                        selectedByIndicator = false
                        selectedByGallery = false
                    }
                }
            })
        }

        startLoop()
    }

    private fun startLoop() {
        if (isAutoPlay) {
            mHandler.postDelayed(mRunnable, INTERNAL)
        }
    }

    private fun handlePosition() {
        if (isAutoPlay && galleryVP.currentItem < 9) {
            // 模拟其中一个viewpager滑动即可
            selectedByIndicator = false
            selectedByGallery = true
            galleryVP.setCurrentItem(galleryVP.currentItem + 1, true)
        }
        mHandler.postDelayed(mRunnable, INTERNAL)
    }

    class ViewHolder(parent: ViewGroup, layoutId: Int) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    )

    class Adapter(private val layoutId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            return 10
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(parent, layoutId)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tag = position
        }
    }

    class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            MyLog.i(TAG, "[transformPage] position: $position")
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = .5f
                        scaleX = MIN_SCALE
                        scaleY = MIN_SCALE
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = .5f
                        scaleX = MIN_SCALE
                        scaleY = MIN_SCALE
                    }
                }
            }
        }
    }

    companion object {

        private const val TAG = "AlbumSlideShowFragment"

        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
        private const val INTERNAL = 4000L

        @JvmStatic
        fun newInstance() = AlbumSlideShowFragment().apply {}

        @JvmStatic
        fun newInstance(param1: String, param2: String) = AlbumSlideShowFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}