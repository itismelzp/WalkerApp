package com.demo.fragment

import androidx.fragment.app.Fragment

/**
 * Created by lizhiping on 2023/1/30.
 * <p>
 * description
 */

abstract class BaseFragment : Fragment() {

    open fun createFragment() : BaseFragment {
        return createFragment("", "")
    }

    abstract fun createFragment(arg1: String, arg2: String) : BaseFragment

}