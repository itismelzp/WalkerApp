package com.demo.syscomponent

import android.content.ContentResolver
import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.demo.databinding.FragmentProviderBinding
import com.demo.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * A simple [Fragment] subclass.
 * Use the [ProviderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProviderFragment : BaseFragment<FragmentProviderBinding>() {

    private var textStr: String = ""

    private val textLiveData = MutableLiveData<String>()

    private lateinit var resolver: ContentResolver

    private val uriUse: Uri = Uri.parse("${MyProvider.BASE_URI}/${DBHelper.USER_TABLE_NAME}")
    private val uriJob: Uri = Uri.parse("${MyProvider.BASE_URI}/${DBHelper.JOB_TABLE_NAME}")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentProviderBinding.inflate(inflater, container, attachToRoot)

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        initResolverData()
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        queryUserData()
        queryJobData()
        binding.insertProviderDataBtn.apply {
            setOnClickListener { insertUse() }
        }
        initObserver()
    }

    override fun createFragment(arg1: String, arg2: String) = createFragment()

    private fun initObserver() {
        textLiveData.observe(viewLifecycleOwner) {
            binding.printTv.text = it
        }
        resolver.registerContentObserver(uriUse, true, mContentObserver)
    }

    private val mContentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            Toast.makeText(
                requireContext(),
                "ContentObserver [onChange1]: $selfChange",
                Toast.LENGTH_SHORT
            ).show()
            queryUserData()
        }
    }

    private fun initResolverData() {
        resolver = requireContext().contentResolver
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val values = ContentValues()
                values.put("_id", 3)
                values.put("name", "Iverson")
                resolver.insert(uriUse, values)
                val valuesJob = ContentValues()
                valuesJob.put("_id", 3)
                valuesJob.put("job", "NBA Player")
                resolver.insert(uriJob, valuesJob)
            }
        }
    }

    private fun queryUserData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cursor = resolver.query(uriUse, arrayOf("_id", "name"), null, null, null)
                var textStr = ""
                cursor?.let {
                    while (it.moveToNext()) {
                        val query = "query name: ${it.getInt(0)}, ${it.getString(1)}"
                        println(query)
                        textStr = "$textStr\n$query"
                    }
                    it.close()
                }
                textLiveData.postValue(textStr)
            }
        }
    }

    private fun queryJobData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val jobCursor = resolver.query(uriJob, arrayOf("_id", "job"), null, null, null)
                jobCursor?.let {
                    while (it.moveToNext()) {
                        val query = "query job: ${it.getInt(0)}, ${it.getString(1)}"
                        println(query)
                        textStr = "$textStr\n$query"
                    }
                    it.close()
                }
                textLiveData.postValue(textStr)
            }
        }
    }

    private fun insertUse() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val values = ContentValues()
                values.put("name", "walker" + UUID.randomUUID())
                resolver.insert(uriUse, values)
            }
        }
    }

    private fun getOption(flags: Int): String {
        return when (flags) {
            ContentResolver.NOTIFY_INSERT -> {
                "insert"
            }
            ContentResolver.NOTIFY_DELETE -> {
                "delete"
            }
            ContentResolver.NOTIFY_UPDATE -> {
                "update"
            }
            else -> {
                "other: $flags"
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProviderFragment()
    }
}