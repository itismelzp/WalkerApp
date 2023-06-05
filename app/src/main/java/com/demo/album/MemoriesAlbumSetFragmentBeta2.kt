package com.demo.album

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.R
import com.demo.customview.utils.ViewUtils
import com.demo.base.log.MyLog

class MemoriesAlbumSetFragmentBeta2 : AlbumBaseFragment() {

    private lateinit var explorerViewModel: ExplorerViewModel

    private lateinit var mChoiceNessMemoriesAdapter: MemoriesChoicenessListAdapter
    private lateinit var mAllMemoriesAdapter: MemoriesAllListAdapter
    private lateinit var viewModel: MemoriesAlbumSetViewModel
    private lateinit var memoriesChoicenessRv: RecyclerView
    private lateinit var memoriesAllRv: RecyclerView

    companion object {

        private const val TAG = "MemoriesAlbumSetFragment"

        const val SPAN_COUNT = 2
        fun newInstance() = MemoriesAlbumSetFragmentBeta2()
    }

    override fun getLayoutId(): Int = R.layout.explore_memories_album_set_list2

    override fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        super.doViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MemoriesAlbumSetViewModel::class.java]
        explorerViewModel = ViewModelProvider(this)[ExplorerViewModel::class.java]

        memoriesChoicenessRv = view.findViewById(R.id.memories_choiceness_rv)
        memoriesAllRv = view.findViewById(R.id.memories_all_rv)

        initChoiceNessMemoriesRV()
        initAllMemoriesRV()
        explorerViewModel.loadData()
    }

    private fun initChoiceNessMemoriesRV() {
        memoriesChoicenessRv.apply {
            adapter = MemoriesChoicenessListAdapter(MemoriesChoicenessListAdapter.MainDiffItemCallback()).apply {
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
        memoriesAllRv.apply {
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