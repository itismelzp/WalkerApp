package com.demo.base

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by lizhiping on 2023/1/30.
 * <p>
 * description
 */

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    /**
     * 这里会发现Fragment和Activity的封装方式不一样，没有用lateinit。
     * 因为binding变量只有在onCreateView与onDestroyView才是可用的，
     * 而fragment的生命周期和activity的不同，fragment可以超出其视图的生命周期，
     * 比如fragment hide的时候，如果不将这里置为空，有可能引起内存泄漏。
     * 所以我们要在onCreateView中创建，onDestroyView置空。
     */
    protected var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTransition()
    }

    open fun setTransition() {
        enterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(getEnterTransitionRes())
        exitTransition =
            TransitionInflater.from(requireContext()).inflateTransition(getExitTransitionRes())
    }

    open fun getEnterTransitionRes(): Int = R.transition.slide_right
    open fun getExitTransitionRes(): Int = R.transition.grid_exit_transition

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding(inflater, container)
        initBaseData(savedInstanceState)
        initBaseViews(savedInstanceState)
        return binding.root
    }

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean = false
    ): T

    open fun createFragment(): BaseFragment<T> {
        return createFragment("", "")
    }

    open fun initBaseData(savedInstanceState: Bundle?) {}
    open fun initBaseViews(savedInstanceState: Bundle?) {}

    abstract fun createFragment(arg1: String, arg2: String): BaseFragment<T>

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @UiThread
    protected fun toast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), text, duration).show()
    }

}