package com.demo.syscomponent

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.databinding.FragmentProviderBinding
import com.demo.fragment.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ProviderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProviderFragment : BaseFragment<FragmentProviderBinding>() {

    private var textStr: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentProviderBinding.inflate(inflater, container, attachToRoot)

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        initResolverData()
    }

    private fun initResolverData() {
        val uriUse = Uri.parse("${MyProvider.SCHEME}${MyProvider.AUTHORITY}/${DBHelper.USER_TABLE_NAME}")
        val values = ContentValues()
        values.put("_id", 3)
        values.put("name", "Iverson")
        val resolver = requireContext().contentResolver
        resolver.insert(uriUse, values)
        val cursor = resolver.query(uriUse, arrayOf("_id", "name"), null, null, null)
        cursor?.let {
            while (it.moveToNext()) {
                val query = "query name: ${it.getInt(0)}, ${it.getString(1)}"
                println(query)
                textStr = "$textStr\n$query"
            }
            it.close()
        }

        val uriJob = Uri.parse("${MyProvider.SCHEME}${MyProvider.AUTHORITY}/${DBHelper.JOB_TABLE_NAME}")
        val valuesJob = ContentValues()
        valuesJob.put("_id", 3)
        valuesJob.put("job", "NBA Player")
        val jobResolver = requireContext().contentResolver
        jobResolver.insert(uriJob, valuesJob)
        val jobCursor = jobResolver.query(uriJob, arrayOf("_id", "job"), null, null, null)
        jobCursor?.let {
            while (it.moveToNext()) {
                val query = "query job: ${it.getInt(0)}, ${it.getString(1)}"
                println(query)
                textStr = "$textStr\n$query"
            }
            it.close()
        }
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        binding.printTv.apply {
            text = textStr
        }
    }

    override fun createFragment(arg1: String, arg2: String): BaseFragment<FragmentProviderBinding> {
        return createFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProviderFragment()
    }
}