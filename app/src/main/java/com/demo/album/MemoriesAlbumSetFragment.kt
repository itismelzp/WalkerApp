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

class MemoriesAlbumSetFragment : BaseFragment<ExploreMemoriesAlbumSetList2Binding>() {

    private lateinit var explorerViewModel: ExplorerViewModel
    private lateinit var mChoiceNessMemoriesAdapter: MemoriesListAdapter
    private lateinit var mAllMemoriesAdapter: MemoriesListAdapter

    companion object {
        fun newInstance() = MemoriesAlbumSetFragment()
    }

    private lateinit var viewModel: MemoriesAlbumSetViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ExploreMemoriesAlbumSetList2Binding.inflate(inflater, container, false)

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
            adapter = MemoriesListAdapter(MemoriesListAdapter.MainDiffItemCallback()).apply {
                mChoiceNessMemoriesAdapter = this
                addItemDecoration(
                    MemoriesListAdapter.SpaceItemDecoration(
                        ViewUtils.dpToPx(0f),
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(0f),
                        ViewUtils.dpToPx(4f)
                    )
                )
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        explorerViewModel.multiBlockLiveData.observe(viewLifecycleOwner) {
            mChoiceNessMemoriesAdapter.submitList(it)
        }
    }

    private fun initAllMemoriesRV() {
        binding.memoriesAllRv.apply {
            adapter = MemoriesListAdapter(MemoriesListAdapter.MainDiffItemCallback()).apply {
                mAllMemoriesAdapter = this
                addItemDecoration(
                    MemoriesListAdapter.SpaceItemDecoration(
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(4f)
                    )
                )
            }
            layoutManager = GridLayoutManager(context, 2)
        }

        explorerViewModel.multiAllBlockLiveData.observe(viewLifecycleOwner) {
            mAllMemoriesAdapter.submitList(it)
        }

    }

}