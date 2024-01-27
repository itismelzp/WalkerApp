package com.demo.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.databinding.FragmentSyncDemoBinding
import com.demo.base.BaseFragment

class SyncDemoFragment : BaseFragment<FragmentSyncDemoBinding>() {

    override fun createFragment(arg1: String, arg2: String) = SyncDemoFragment()

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        binding.btnTestSync.setOnClickListener {
            SyncManager(binding.tvTestResult, this).startTest()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SyncDemoFragment()
    }
}