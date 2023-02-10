package com.demo.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.demo.R
import com.demo.SpaceItemDecoration
import com.demo.databinding.FragmentAlbumSlideShowBinding
import com.demo.logger.MyLog
import com.demo.viewpager.CustomGsyVideo
import com.demo.viewpager.MediaType
import com.demo.viewpager.VideoBean
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlin.math.abs


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumSlideShowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumSlideShowFragment : BaseFragment<FragmentAlbumSlideShowBinding>(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var galleryVP: ViewPager2
    private lateinit var indicatorVP: ViewPager2
    private lateinit var galleryLayoutManager: LinearLayoutManager

    private var selectedByGallery = false
    private var selectedByIndicator = false

    private var isGalleryAutoPlay = false
    private var isGalleryPlaying = true
    private val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = Runnable { this.handlePosition() }

    private lateinit var mData: List<VideoBean>

    private var position = 0

    override fun createFragment(arg1: String, arg2: String): BaseFragment<FragmentAlbumSlideShowBinding> {
        return newInstance("", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAlbumSlideShowBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initGallery()
        initIndicator()

//        binding.controlPlayBtn.setOnClickListener(this)
        binding.slideShowPlayIv.setOnClickListener(this)
    }

    private fun initData() {
        mData = mutableListOf(
            VideoBean(R.drawable.kpl_machao_01, ""),
            VideoBean(R.mipmap.img1, RawResourceDataSource.buildRawResourceUri(R.raw.video1).toString(), MediaType.VIDEO),
            VideoBean(R.drawable.kpl_lvbu_01, ""),
            VideoBean(R.mipmap.img2, RawResourceDataSource.buildRawResourceUri(R.raw.video2).toString(), MediaType.VIDEO),
            VideoBean(R.drawable.kpl_lvbu_02, ""),
            VideoBean(R.mipmap.img3, RawResourceDataSource.buildRawResourceUri(R.raw.video3).toString(), MediaType.VIDEO),
            VideoBean(R.drawable.kpl_libai_01, ""),
            VideoBean(R.drawable.kpl_libai_02, ""),
            VideoBean(R.drawable.kpl_libai_03, ""),
            VideoBean(R.drawable.kpl_libai_04, ""),
            VideoBean(R.drawable.kpl_zhaoyun_01, ""),
            VideoBean(R.drawable.kpl_zhaoyun_02, ""),
            VideoBean(R.drawable.kpl_change_01, "")
        )
    }

    private fun initGallery() {
        binding.galleryViewPager.apply {
            galleryVP = this
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 1
            (getChildAt(0) as RecyclerView).apply {
                clipToPadding = false
                this@AlbumSlideShowFragment.galleryLayoutManager = layoutManager as LinearLayoutManager
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
                    this@AlbumSlideShowFragment.position = position
                    GSYVideoManager.releaseAllVideos()
                    startVideo()
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

    private fun initIndicator() {
        binding.indicatorViewPager.apply {
            indicatorVP = this
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 7
            (getChildAt(0) as RecyclerView).apply {
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

    fun startVideo() {

        if (mData[position].mediaType == MediaType.IMAGE) {
            return
        }

        GSYVideoManager.releaseAllVideos()
        Handler(Looper.getMainLooper()).postDelayed({
            galleryLayoutManager.findViewByPosition(position)?.findViewById<CustomGsyVideo>(R.id.gsyVideo)
                ?.apply {
                    isLooping = false
                    startPlayLogic()
                    requestFocus()
                    Log.i(TAG, "position: $position, la: $galleryLayoutManager, url: $url")
                }
        }, 100)
    }

    private fun startLoop() {
        isGalleryAutoPlay = true
        isGalleryPlaying = true
        resumeLoop()
    }

    private fun pauseLoop() {
        if (isGalleryAutoPlay) {
            mHandler.removeCallbacks(mRunnable)
        }
        isGalleryPlaying = false
    }

    private fun resumeLoop() {
        if (isGalleryAutoPlay) {
            mHandler.removeCallbacks(mRunnable)
            mHandler.postDelayed(mRunnable, INTERNAL)
        }
        isGalleryPlaying = true
    }

    private fun stopLoop() {
        pauseLoop()
        isGalleryAutoPlay = false
    }

    private fun handlePosition() {
        if (isGalleryAutoPlay) {
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
//        if (this::galleryLayoutManager.isInitialized) {
//            startVideo()
//        }
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onStop() {
        super.onStop()
        GSYVideoManager.releaseAllVideos()
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

        var data: List<VideoBean> = mutableListOf()

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
            val bean = data[position]
            val imageView = holder.itemView.findViewById<ImageView>(R.id.gallery_iv)
            val videoView = holder.itemView.findViewById<CustomGsyVideo>(R.id.gsyVideo)
            if (bean.mediaType == MediaType.IMAGE) {
                videoView.visibility = View.GONE
                imageView.apply {
                    visibility = View.VISIBLE
                    setImageResource(data[position].thumb)
                }
            } else {
                imageView.visibility = View.GONE
                val cache = !data[position].videoPath.contains("rawresource")
                        && !data[position].videoPath.startsWith("android")
                videoView.apply {
                    visibility = View.VISIBLE
                    setUp(data[position].videoPath, cache, "")
                    thumbImageView = ImageView(context).apply {
                        setImageResource(data[position].thumb)
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                    playPosition = position
                    isLooping = false
                }
            }
        }
    }

    class IndicatorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var pageClickListener: OnPageClickListener? = null

        var data: List<VideoBean> = mutableListOf()

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slide_indicator, parent, false)
            val viewHolder = ViewHolder(itemView)
            itemView.setOnClickListener { v ->
                pageClickListener?.onPageClick(v, viewHolder.bindingAdapterPosition)
            }
            return viewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.tag = position
            holder.itemView.findViewById<ImageView>(R.id.indicator_iv)
                .setImageResource(data[position].thumb)
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
                        val scaleFactor = MAX_SCALE - (MAX_SCALE - MIN_SCALE) * abs(position) // [1, 1.2]
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
                        MyLog.i(TAG, "[transformPage] pos: $position, scaleX: $scaleX, alpha: $alpha")
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

    private fun itemClick(position: Int) {
        if (position != indicatorVP.currentItem) {
            indicatorVP.setCurrentItem(position, true)
        }
    }

    companion object {

        private const val TAG = "AlbumSlideShowFragment"

        private const val MIN_SCALE = 1f
        private const val MAX_SCALE = 1.2f
        private const val MIN_ALPHA = 0.65f

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

    override fun onClick(v: View) {
        if (v.id == R.id.slide_show_play_iv) {
            if (isGalleryPlaying) {
                pauseLoop()
                binding.slideShowPlayIv.setImageResource(R.drawable.slide_show_clock)
            } else {
                startLoop()
                binding.slideShowPlayIv.setImageDrawable(getRotateBitmap(R.drawable.slide_show_clock, 90f))
            }
        }
    }

    private fun getRotateBitmap(@DrawableRes resId: Int, degree: Float): BitmapDrawable {
        val bitmapOrg: Bitmap = BitmapFactory.decodeResource(resources, resId)
        val width: Int = bitmapOrg.width
        val height: Int = bitmapOrg.height
        val matrix = Matrix()
        matrix.postRotate(degree)
        val resizedBitmap: Bitmap = Bitmap.createBitmap(
            bitmapOrg, 0, 0,
            width, height, matrix, true
        )
        return BitmapDrawable(resources, resizedBitmap)
    }

}