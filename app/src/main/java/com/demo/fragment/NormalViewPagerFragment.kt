package com.demo.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.demo.R
import com.demo.databinding.FragmentSlideShowBinding
import com.demo.fragment.adapter.SlideShowAdapter
import com.demo.logger.MyLog


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NormalViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NormalViewPagerFragment : BaseFragment<FragmentSlideShowBinding>() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewPager2: ViewPager2
    private var lastPosition = 0 //记录轮播图最后所在的位置
    private val colors = mutableListOf<Int>() //轮播图的颜色
    lateinit var indicatorContainer: LinearLayout //填充指示点的容器
    private val mHandler: Handler = Handler()

    override fun createFragment(): BaseFragment<FragmentSlideShowBinding> {
        return newInstance("", "")
    }

    override fun createFragment(arg1: String, arg2: String): BaseFragment<FragmentSlideShowBinding> {
        return newInstance(arg1, arg2)
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
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentSlideShowBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initColors()

        //初始化指示点
        indicatorContainer = binding.containerIndicator
        initIndicatorDots()

        //最后所在的位置设置为500
        lastPosition = 500
        //初始化组件
        binding.slideShowViewpager2.apply {
            viewPager2 = this
            //添加适配器
            adapter = SlideShowAdapter(colors)
            //设置轮播图初始位置在500,以保证可以手动前翻
            currentItem = 500
            //注册轮播图的滚动事件监听器
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                // 每次滑动只回调1次（位置在滑动中间，即：动画 + onPageSelected + 动画）
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    MyLog.i(TAG, "[onPageSelected] position: $position")
                    //轮播时，改变指示点
                    val current = position % 4
                    val last = lastPosition % 4
                    indicatorContainer.getChildAt(current)
                        .setBackgroundResource(R.drawable.shape_dot_selected)
                    indicatorContainer.getChildAt(last).setBackgroundResource(R.drawable.shape_dot)
                    lastPosition = position
                }

                // 每次滑动只回调3次（滑动开始+onPageSelected回调前+滑动结束，即：1 -> 2 -> 0）
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        // 开始滑动
                        ViewPager2.SCROLL_STATE_DRAGGING -> MyLog.i(
                            TAG,
                            "[onPageScrollStateChanged] state: SCROLL_STATE_DRAGGING"
                        )
                        // 滑动到最终位置，即时动画还未结束
                        ViewPager2.SCROLL_STATE_SETTLING -> MyLog.i(
                            TAG,
                            "[onPageScrollStateChanged] state: SCROLL_STATE_SETTLING"
                        )
                        // 滑动到最终位置，并且动画结束
                        ViewPager2.SCROLL_STATE_IDLE -> MyLog.i(
                            TAG,
                            "[onPageScrollStateChanged] state: SCROLL_STATE_IDLE"
                        )
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) { // 每次滑动回调N次（穿插在onPageScrollStateChanged之间）
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    MyLog.i(TAG, "[onPageScrolled] position: $position, positionOffset: $positionOffset, positionOffsetPixels: $positionOffsetPixels")
                }
            })
        }
    }

    private fun initColors() {
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.GREEN)
        colors.add(Color.RED)
    }

    /**
     * 初始化指示点
     */
    private fun initIndicatorDots() {
        for (i in colors.indices) {
            ImageView(context).apply {
                if (i == 0) {
                    setBackgroundResource(R.drawable.shape_dot_selected)
                } else {
                    setBackgroundResource(R.drawable.shape_dot)
                }
                //为指示点添加间距
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 4
                }
                //将指示点添加进容器
                indicatorContainer.addView(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        mHandler.postDelayed(runnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(runnable)
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            //获得轮播图当前的位置
            var currentPosition = viewPager2.currentItem
            currentPosition++
            viewPager2.setCurrentItem(currentPosition, true)
            mHandler.postDelayed(this, 5000)
        }
    }

    companion object {

        const val TAG = "NormalViewPagerFragment"

        @JvmStatic
        fun newInstance() = NormalViewPagerFragment()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NormalViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}