package com.demo.fragment

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.demo.R
import com.demo.customview.utils.ViewUtils
import com.demo.databinding.FragmentAlbumSlideShowBinding
import com.demo.logger.MyLog
import kotlin.math.abs

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

    private var isAutoPlay = false
    private val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = Runnable { this.handlePosition() }

    private lateinit var mData: List<Int>

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
        initData()
        initViewPager()
    }

    private fun initData() {
        mData = mutableListOf(
            R.drawable.kpl_machao_01,
            R.drawable.kpl_lvbu_01,
            R.drawable.kpl_lvbu_02,
            R.drawable.kpl_libai_01,
            R.drawable.kpl_libai_02,
            R.drawable.kpl_libai_03,
            R.drawable.kpl_libai_04,
            R.drawable.kpl_zhaoyun_01,
            R.drawable.kpl_zhaoyun_02,
            R.drawable.kpl_change_01
        )
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
            adapter = GalleryAdapter().apply {
                data = mData
            }
            setPageTransformer(MarginPageTransformer(resources.getDimensionPixelOffset(R.dimen.spacer_small)))
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

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when(state) {
                        ViewPager2.SCROLL_STATE_IDLE -> resumeLoop()
                        ViewPager2.SCROLL_STATE_DRAGGING -> pauseLoop()
                        ViewPager2.SCROLL_STATE_SETTLING -> {}
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
                addItemDecoration(SpaceItemDecoration())
            }
            adapter = IndicatorAdapter().apply {
                data = mData
                pageClickListener = object : OnPageClickListener {
                    override fun onPageClick(clickedView: View?, position: Int) {
                        itemClick(position)
                    }
                }
            }
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

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when(state) {
                        ViewPager2.SCROLL_STATE_IDLE -> resumeLoop()
                        ViewPager2.SCROLL_STATE_DRAGGING -> pauseLoop()
                        ViewPager2.SCROLL_STATE_SETTLING -> {}
                    }
                }
            })
        }

    }

    private fun startLoop() {
        isAutoPlay = true
        resumeLoop()
    }

    private fun pauseLoop() {
        if (isAutoPlay) {
            mHandler.removeCallbacks(mRunnable)
        }
    }

    private fun resumeLoop() {
        if (isAutoPlay) {
            mHandler.removeCallbacks(mRunnable)
            mHandler.postDelayed(mRunnable, INTERNAL)
        }
    }

    private fun stopLoop() {
        pauseLoop()
        isAutoPlay = false
    }

    private fun handlePosition() {
        if (isAutoPlay) {
            if (galleryVP.currentItem < mData.size - 1) {
                // 模拟其中一个viewpager滑动即可
                selectedByIndicator = false
                selectedByGallery = true
                galleryVP.setCurrentItem(galleryVP.currentItem + 1, true)
            }
            startLoop()
        }
    }

    override fun onStart() {
        super.onStart()
        startLoop()
    }

    override fun onStop() {
        super.onStop()
        stopLoop()
    }

    interface OnPageClickListener {
        fun onPageClick(clickedView: View?, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(parent: ViewGroup, layoutId: Int)
                : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
    }

    class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var data: List<Int> = mutableListOf()

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slide_gallery_pages, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tag = position
            holder.itemView.findViewById<ImageView>(R.id.gallery_iv).setImageResource(data[position])
        }
    }

    class IndicatorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var pageClickListener: OnPageClickListener? = null

        var data: List<Int> = mutableListOf()

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slide_indicator, parent, false)
            val viewHolder = ViewHolder(itemView)
            itemView.setOnClickListener { v ->
                pageClickListener?.onPageClick(v, viewHolder.adapterPosition)
            }
            return viewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tag = position
            holder.itemView.findViewById<ImageView>(R.id.indicator_iv).setImageResource(data[position])
        }
    }

    class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
//            MyLog.i(TAG, "[transformPage] position: $position")
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = MIN_ALPHA
                        scaleX = MIN_SCALE
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well.
                        val scaleFactor = Math.max(MIN_SCALE, MAX_SCALE - abs(position)) // [1, 1.2]
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
//                        translationX = if (position < 0) {
//                            horzMargin - vertMargin / 2
//                        } else {
//                            horzMargin + vertMargin / 2
//                        }

                        // Scale the page down (between MIN_SCALE and 1).
                        scaleX = scaleFactor

                        // Fade the page relative to its size.
                        alpha = MIN_ALPHA + ((scaleFactor - MIN_SCALE) / (MAX_SCALE - MIN_SCALE)) * (1 - MIN_ALPHA) // [0.5, 1]
                        MyLog.i(TAG, "[transformPage] scaleX: $scaleX, alpha: $alpha")
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = MIN_ALPHA
                        scaleX = MIN_SCALE
                    }
                }
            }
        }
    }

    class SpaceItemDecoration : ItemDecoration {

        private val leftSpace: Int
        private val topSpace: Int
        private val rightSpace: Int
        private val bottomSpace: Int

        private val defaultSpace = ViewUtils.dpToPx(2f)

        constructor() {
            leftSpace = defaultSpace
            topSpace = ViewUtils.dpToPx(0f)
            rightSpace = defaultSpace
            bottomSpace = ViewUtils.dpToPx(0f)
        }

        constructor(space: Int) {
            this.leftSpace = space
            this.topSpace = space
            this.rightSpace = space
            this.bottomSpace = space
        }

        constructor(leftSpace: Int, topSpace: Int, rightSpace: Int, bottomSpace: Int) {
            this.leftSpace = leftSpace
            this.topSpace = topSpace
            this.rightSpace = rightSpace
            this.bottomSpace = bottomSpace
        }

        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
        }
    }

    private fun itemClick(position: Int) {
        if (position != indicatorVP.currentItem) {
            indicatorVP.setCurrentItem(position, true)
        }
    }

    companion object {

        private const val TAG = "AlbumSlideShowFragment"

        private const val MIN_SCALE = 1f
        private const val MAX_SCALE = 1.2f
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