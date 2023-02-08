package com.demo.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.R
import com.demo.album.binding.ExplorerItemDataBinding
import com.demo.customview.utils.ViewUtils
import com.demo.databinding.ExploreMainFragmentLayoutBinding
import com.demo.fragment.BaseFragment

class OppoGalleryFragment : BaseFragment<ExploreMainFragmentLayoutBinding>() {

    private lateinit var explorerViewModel: ExplorerViewModel
    private lateinit var labelContainer: ExplorerCardLayout
    private lateinit var memoriesContainer: ExplorerBlockLayout

    companion object {

        const val INVALID_LABEL_ID = -1

        @JvmStatic
        fun newInstance() = OppoGalleryFragment()
    }

    private lateinit var viewModel: OppoGalleryViewModel

    override fun createFragment(arg1: String, arg2: String): BaseFragment<ExploreMainFragmentLayoutBinding> {
        return newInstance()
    }

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        explorerViewModel = ViewModelProvider(this)[ExplorerViewModel::class.java]
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        initLabelContainer()
        initMemoriesContainer()
        explorerViewModel.loadData()
    }

    private fun initLabelContainer() {

        labelContainer = binding.explorerLabelContainer
        labelContainer.tvRightTitle.setOnClickListener {

        }
        labelContainer.slSingleLine.also {
            it.onItemClick = rtn@{ index ->
                val context = activity ?: return@rtn
//                val viewData = explorerViewModel.queryLabelViewData(index) ?: return@rtn
//                    viewData.title?.let { title ->
//                        var labelId = INVALID_LABEL_ID
//                        runCatching {
//                            labelId = TextUtil.getPathSuffix(viewData.id)?.toInt() ?: INVALID_LABEL_ID
//                        }
//                    }
            }
        }
        ExplorerCardLiveBinding(
            labelContainer,
            ExplorerItemDataBinding::class.java,
            {
                LayoutInflater.from(it.context)
                    .inflate(R.layout.main_explorer_album_set_item, it, false)
            },
            { labelContainer.slSingleLine.maxCount }
        ).bind(this, explorerViewModel.labelBindingData)
    }

    private lateinit var mAdapter: MemoriesListAdapter
    private fun initMemoriesContainer() {
        memoriesContainer = binding.explorerMemoriesContainer
        memoriesContainer.tvRightTitle.setOnClickListener {

        }
        memoriesContainer.slSingleLine.onItemClick = rtn@{ index ->
//            val viewData = explorerViewModel.queryMemoryViewData(index) ?: return@rtn
        }
        memoriesContainer.rvGallery.apply {
            adapter = MemoriesListAdapter(MemoriesListAdapter.MainDiffItemCallback()).apply {
                mAdapter = this
                addItemDecoration(
                    MemoriesListAdapter.SpaceItemDecoration(
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(0f),
                        ViewUtils.dpToPx(4f),
                        ViewUtils.dpToPx(0f)
                    )
                )
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        explorerViewModel.multiBlockLiveData.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }

//        ExplorerBlockLiveBinding(
//            memoriesContainer,
//            ExplorerBlockDataBinding::class.java,
//            {
//                (LayoutInflater.from(it.context).inflate(R.layout.explore_memories_album_set_item, it, false) as ConstraintLayout).apply {
////                    updateMemoriesHeight(this)
//                }
//            },
//            { 0 }
//        ).apply {
////            bind(this@OppoGalleryFragment, explorerViewModel.memoriesBindingData)
//            bind(viewLifecycleOwner, explorerViewModel.memoriesBindingData)
//        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ExploreMainFragmentLayoutBinding.inflate(inflater, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[OppoGalleryViewModel::class.java]

    }

}