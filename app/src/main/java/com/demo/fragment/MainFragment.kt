package com.demo.fragment

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.*
import com.demo.MainListAdapter.MainDiff
import com.demo.MainListAdapter.SpaceItemDecoration
import com.demo.logger.MyLog
import com.tencent.wink.apt.library.BindButtonTools

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
        return contentView
            ?: inflater.inflate(R.layout.fragment_main, container, false).apply {
                this@MainFragment.contentView = this
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setContentView(R.layout.activity_main)
        MyLog.d(TAG, "onViewCreated")
        BindButtonTools.bind(activity)
//        MainActivity$ViewBinding binding = new MainActivity$ViewBinding();
//        binding.bind(this);
        //        MainActivity$ViewBinding binding = new MainActivity$ViewBinding();
//        binding.bind(this);
        initViewModel()

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().name)

        recyclerView = contentView?.findViewById(R.id.main_rv)
        mainListAdapter = MainListAdapter(MainDiff())
        recyclerView?.adapter = mainListAdapter
        recyclerView?.layoutManager = GridLayoutManager(context, 2)

//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView?.addItemDecoration(SpaceItemDecoration())

        preLoadSubProcess(context)

//        initMonitor(); // 卡顿监控
//        initMonitor(); // 卡顿监控
        MainButtonModel.initData(mainButtonViewModel, activity as AppCompatActivity)
    }

    private fun initViewModel() {
        mainButtonViewModel = ViewModelProvider(this).get(
            MainButtonViewModel::class.java
        )
        mainButtonViewModel.mainButtonList.observe(
            viewLifecycleOwner
        ) { mainButtons: List<MainButton?>? ->
            mainListAdapter!!.submitList(
                mainButtons
            )
        }
    }

    private fun preLoadSubProcess(context: Context?) {
        if (context == null || isSubAlive(context)) {
            return
        }
        Thread {
            val preLoader = Intent()
            preLoader.action = "com.demo.ipc.SubPreLoadService"
            preLoader.setPackage("com.demo")
            try {
                context.startService(preLoader)
            } catch (e: Exception) {
                Log.e(TAG, "[preLoadSub] preLoadSub failed.")
            }
            Log.e(TAG, "[preLoadSub] preLoadSub...")
        }.start()
    }

    private fun isSubAlive(context: Context): Boolean {
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val processInfos = am.runningAppProcesses
            for (info in processInfos) {
                val processName = info.processName
                if ("com.demo:sub" == processName) {
                    Log.e(TAG, "[isSubAlive] isSubAlive == true")
                    return true
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "get process info fail.")
        }
        Log.e(TAG, "[isSubAlive] isSubAlive == false")
        return false
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


    override fun onDestroyView() {
        super.onDestroyView()
        MyLog.d(TAG, "onDestroyView")
        val preLoader = Intent()
        preLoader.action = "com.demo.ipc.SubPreLoadService"
        preLoader.setPackage("com.demo")
        context?.stopService(preLoader)
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