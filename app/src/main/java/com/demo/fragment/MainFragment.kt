package com.demo.fragment

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.MainButtonModel
import com.demo.MainButtonViewModel
import com.demo.MainListAdapter
import com.demo.MainListAdapter.MainDiff
import com.demo.MainListAdapter.SpaceItemDecoration
import com.demo.R
import com.demo.ipc.ProcessUtil
import com.demo.logger.MyLog
import com.tencent.wink.apt.library.BindButtonTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var contentView: View? = null

    private var recyclerView: RecyclerView? = null
    private var mainListAdapter: MainListAdapter? = null
    private lateinit var mainButtonViewModel: MainButtonViewModel
    
    private var mLastFrameNanos: Long = 0
    private var isViewCreate = false


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d(TAG, "onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MyLog.d(TAG, "onCreateView: $contentView")
        // Inflate the layout for this fragment
        return contentView?.apply {
            isViewCreate = true
        } ?: inflater.inflate(R.layout.fragment_main, container, false).apply {
            this@MainFragment.contentView = this
            isViewCreate = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isViewCreate) {
            return
        }
        BindButtonTools.bind(activity) // or ```val binding = MainActivity$ViewBinding() binding.bind(this)```
        initViewModel()
        initView()
        preLoadSubProcess(context) // 预加载子进程
//        initMonitor() // 卡顿监控
        MainButtonModel.initData(mainButtonViewModel, activity as AppCompatActivity)
        MyLog.d(TAG, "onViewCreated currentThread: " + Thread.currentThread().name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ProcessUtil.stopSubProcessService(context)
        MyLog.d(TAG, "onDestroyView")
    }

    private fun initView() {
        recyclerView = contentView?.findViewById(R.id.main_rv)
        mainListAdapter = MainListAdapter(MainDiff())
        recyclerView?.adapter = mainListAdapter
        recyclerView?.layoutManager = GridLayoutManager(context, 2)
//        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView?.addItemDecoration(SpaceItemDecoration())
    }

    private fun initViewModel() {
        mainButtonViewModel = ViewModelProvider(this)[MainButtonViewModel::class.java]
        mainButtonViewModel.mainButtonList.observe(viewLifecycleOwner) {
            mainListAdapter?.submitList(it)
        }
    }

    private fun preLoadSubProcess(context: Context?) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (context == null || ProcessUtil.isSubProcessAlive(context)) {
                return@launch
            }
            ProcessUtil.loadSubProcessService(context)
        }
    }

    private fun initMonitor() {

        // 1) Looper方案--BlockCanary
        Looper.getMainLooper().setMessageLogging { s: String ->
            // >>>>> Dispatching to
            // <<<<< Finished to
            Log.d(TAG, "[println] s: $s")
        }

        // 2) Choreographer方案--ArgusAPM、LogMonitor
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (mLastFrameNanos == 0L) {
                    mLastFrameNanos = frameTimeNanos
                }
                if (frameTimeNanos - mLastFrameNanos > 100) {
                    //
                }
                Log.i(
                    TAG,
                    "[doFrame] time gap: " + (frameTimeNanos - mLastFrameNanos).toFloat() / NANO_UNIT + "ms"
                )
                mLastFrameNanos = frameTimeNanos
                Choreographer.getInstance().postFrameCallback(this)
            }
        })

        // 1) + 2)--Matrix
    }

    companion object {

        private const val TAG = "MainFragment"
        private const val NANO_UNIT = 1000000L

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}