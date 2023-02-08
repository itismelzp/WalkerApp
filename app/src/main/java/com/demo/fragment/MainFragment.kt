package com.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.*
import com.demo.MainListAdapter.MainDiffItemCallback
import com.demo.customview.utils.ViewUtils
import com.demo.databinding.FragmentMainBinding
import com.demo.ipc.ProcessUtil
import com.demo.logger.MyLog
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

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var mainListAdapter: MainListAdapter? = null
    private var filterAdapter: FilterListAdapter? = null
    private lateinit var mainButtonViewModel: MainButtonViewModel
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var mainButtonModel: MainButtonModel

    private var isViewCreate = false

    private val decoration = SpaceItemDecoration(ViewUtils.dpToPx(2))

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyLog.d(TAG, "[onCreate]")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)

        mainButtonModel = MainButtonModel(this@MainFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MyLog.d(TAG, "[onCreateView] fragment: $this, binding: $_binding")
        return _binding?.let {
            isViewCreate = true
            it.root
        } ?: FragmentMainBinding.inflate(inflater, container, false).let {
            _binding = it
            isViewCreate = false
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (isViewCreate) {
//            return
//        }
//        BindButtonTools.bind(activity) // or ```val binding = MainActivity$ViewBinding() binding.bind(this)```
        initViewModel()
        initView()
//        MonitorUtil.initMonitor() // 卡顿监控
        preLoadSubProcess() // 预加载子进程
        MyLog.d(TAG, "[onViewCreated] currentThread: " + Thread.currentThread().name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ProcessUtil.stopSubProcessService(context)
        MyLog.d(TAG, "[onDestroyView]")
    }

    private fun initViewModel() {

        ViewModelProvider(this)[MainButtonViewModel::class.java].apply {
            mainButtonViewModel = this
            mainButtonList.apply {
                observe(viewLifecycleOwner) {
                    mainListAdapter?.submitList(it.toMutableList())
                }
                postValue(mainButtonModel.buttons)
            }
        }
        ViewModelProvider(this)[FilterViewModel::class.java].apply {
            filterViewModel = this
            filterList.apply {
                observe(viewLifecycleOwner) {
                    filterAdapter?.submitList(it)
                }
                postValue(mainButtonModel.diffButtons)
            }
        }
    }

    private fun initView() {
        binding.filterRecyclerView.apply {
            adapter =
                FilterListAdapter(FilterListAdapter.ButtonFilterItemCallback()) { type, isCheck ->
                    mainButtonModel.operateData(type, isCheck)
                    MyLog.i(TAG, "this@MainFragment: ${this@MainFragment}, viewLifecycleOwner: $viewLifecycleOwner")
                    mainButtonViewModel.mainButtonList.postValue(mainButtonModel.buttons)
                }.apply {
                    filterAdapter = this
                }
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            removeItemDecoration(decoration)
            addItemDecoration(decoration)
        }

        binding.mainRecyclerView.apply {
            adapter = MainListAdapter(MainDiffItemCallback()).apply {
                mainListAdapter = this
            }
            layoutManager = GridLayoutManager(context, 2)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            removeItemDecoration(decoration)
            addItemDecoration(decoration)
        }
    }

    private fun preLoadSubProcess() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (context == null || ProcessUtil.isSubProcessAlive(context)) {
                return@launch
            }
            ProcessUtil.loadSubProcessService(context)
        }
    }

    companion object {

        private const val TAG = "MainFragment"

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