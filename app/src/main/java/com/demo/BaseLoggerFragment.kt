package com.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.databinding.FragmentBaseLoggerLayoutBinding
import com.demo.base.BaseFragment

open class BaseLoggerFragment : BaseFragment<FragmentBaseLoggerLayoutBinding>() {

    companion object {
        private const val TAG = "BaseLoggerFragment"

        @JvmStatic
        fun newInstance(): BaseLoggerFragment = BaseLoggerFragment()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentBaseLoggerLayoutBinding.inflate(layoutInflater, container, attachToRoot)

    override fun createFragment(
        arg1: String,
        arg2: String
    ): BaseFragment<FragmentBaseLoggerLayoutBinding> = createFragment()

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        binding.rvOperator.apply {
        }

    }

    private fun appendLine(sb: StringBuffer, line: String) {
        sb.append("$line\n")
    }

}