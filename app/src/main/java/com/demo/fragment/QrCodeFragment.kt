package com.demo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.demo.base.BaseFragment
import com.demo.databinding.FragmentQrCodeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [QrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrCodeFragment : BaseFragment<FragmentQrCodeBinding>() {

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
//        IQRCodeGenerate()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QrCodeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun createFragment(arg1: String, arg2: String): BaseFragment<FragmentQrCodeBinding> =
        newInstance()
}