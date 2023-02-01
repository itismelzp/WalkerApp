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
 * Use the [SlideShowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SlideShowFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewPager2: ViewPager2
    private var lastPosition = 0 //记录轮播图最后所在的位置
    private val colors = mutableListOf<Int>() //轮播图的颜色
    lateinit var indicatorContainer: LinearLayout //填充指示点的容器
    private val mHandler: Handler = Handler()

    private var _binding: FragmentSlideShowBinding? = null
    private val binding get() = _binding!!

    override fun createFragment(): BaseFragment {
        return newInstance("", "")
    }

    override fun createFragment(arg1: String, arg2: String): BaseFragment {
        return newInstance(arg1, arg2)
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
        _binding = FragmentSlideShowBinding.inflate(inflater, container, false)

        MyLog.i(TAG, "[onCreateView]")
        return binding.root
    }

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
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    //轮播时，改变指示点
                    val current = position % 4
                    val last = lastPosition % 4
                    indicatorContainer.getChildAt(current)
                        .setBackgroundResource(R.drawable.shape_dot_selected)
                    indicatorContainer.getChildAt(last).setBackgroundResource(R.drawable.shape_dot)
                    lastPosition = position
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
        mHandler.postDelayed(runnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(runnable);
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

        const val TAG = "SlideShowFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SlideShowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}