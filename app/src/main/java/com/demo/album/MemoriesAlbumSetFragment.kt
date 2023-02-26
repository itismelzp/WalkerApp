package com.demo.album

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.customview.utils.ViewUtils
import com.demo.databinding.ExploreMemoriesAlbumSetList2Binding
import com.demo.fragment.BaseFragment
import com.demo.logger.MyLog

class MemoriesAlbumSetFragment : BaseFragment<ExploreMemoriesAlbumSetList2Binding>() {

    private lateinit var explorerViewModel: ExplorerViewModel

    private lateinit var mChoiceNessMemoriesAdapter: MemoriesChoisenessListAdapter
    private lateinit var mAllMemoriesAdapter: MemoriesAllListAdapter

    companion object {

        private const val TAG = "MemoriesAlbumSetFragmen"

        const val SPAN_COUNT = 2
        fun newInstance() = MemoriesAlbumSetFragment()
    }

    private lateinit var viewModel: MemoriesAlbumSetViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = ExploreMemoriesAlbumSetList2Binding.inflate(inflater, container, attachToRoot)

    override fun createFragment(
        arg1: String,
        arg2: String
    ): BaseFragment<ExploreMemoriesAlbumSetList2Binding> {
        return newInstance()
    }

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        viewModel = ViewModelProvider(this)[MemoriesAlbumSetViewModel::class.java]
        explorerViewModel = ViewModelProvider(this)[ExplorerViewModel::class.java]
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        initChoiceNessMemoriesRV()
        initAllMemoriesRV()

        explorerViewModel.loadData()
    }

    private fun initChoiceNessMemoriesRV() {
        binding.memoriesChoicenessRv.apply {
            adapter = MemoriesChoisenessListAdapter(MemoriesChoisenessListAdapter.MainDiffItemCallback()).apply {
                mChoiceNessMemoriesAdapter = this
                addItemDecoration(
                    SpaceItemDecoration(
                        ViewUtils.dpToPx(0f),
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(0f),
                        ViewUtils.dpToPx(4f)
                    )
                )
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        explorerViewModel.multiChoiceNessBlockLiveData.observe(viewLifecycleOwner) {
            MyLog.i(TAG, "[initChoiceNessMemoriesRV] mChoiceNessMemoriesAdapter.size: ${it.size}")
            mChoiceNessMemoriesAdapter.submitList(it)
        }
    }

    private fun initAllMemoriesRV() {
        binding.memoriesAllRv.apply {
            adapter = MemoriesAllListAdapter(MemoriesAllListAdapter.MainDiffItemCallback()).apply {
                mAllMemoriesAdapter = this
                addItemDecoration(
                    MemoriesAllListAdapter.GridSpacingItemDecoration(SPAN_COUNT, ViewUtils.dpToPx(8f), false)
                )
            }
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }

        explorerViewModel.multiAllBlockLiveData.observe(viewLifecycleOwner) {
            MyLog.i(TAG, "[initAllMemoriesRV] multiAllBlockLiveData.size: ${it.size}")
            mAllMemoriesAdapter.submitList(it)
        }

    }

}