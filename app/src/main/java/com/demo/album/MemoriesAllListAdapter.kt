package com.demo.album

import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.customview.utils.ViewUtils
import com.demo.base.log.MyLog
import com.demo.databinding.ExploreMemoriesChoiceNessBlockBinding

/**
 * Created by lizhiping on 2023/2/7.
 * <p>
 * description
 */
class MemoriesAllListAdapter(diffCallback: DiffUtil.ItemCallback<AlbumBindingData>) :
    ListAdapter<AlbumBindingData, MemoriesAllListAdapter.MemoriesViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemoriesViewHolder.create(parent)

    override fun onBindViewHolder(holder: MemoriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MemoriesViewHolder(binding: ExploreMemoriesChoiceNessBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var blockImage: CornerImageView = binding.mainExplorerAlbumSetItemImage
        private var titleText: TextView = binding.mainExplorerAlbumSetItemTitleText
        private var subTitleText: TextView = binding.mainExplorerAlbumSetItemSubTitleText

        companion object {

            fun create(parent: ViewGroup): MemoriesViewHolder = MemoriesViewHolder(
                ExploreMemoriesChoiceNessBlockBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(data: AlbumBindingData) {
            blockImage.setImageDrawable(data.drawable)
            blockImage.setRoundCorner(ViewUtils.dpToPx(16f))
            titleText.text = data.title
            subTitleText.text = data.subTitle
        }
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int, // 列数
        private val spacing: Int, // 间隔
        private var includeEdge: Boolean // 是否包含边缘
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            // 这里是关键，需要根据你有几列来判断
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    class MainDiffItemCallback : DiffUtil.ItemCallback<AlbumBindingData>() {

        companion object {
            private const val TAG = "MemoriesAllListAdapter"
        }

        override fun areItemsTheSame(
            oldItem: AlbumBindingData,
            newItem: AlbumBindingData
        ): Boolean {
            val areItemsTheSame = oldItem === newItem
            MyLog.i(TAG, "areItemsTheSame: $areItemsTheSame")
            return areItemsTheSame
        }

        override fun areContentsTheSame(
            oldItem: AlbumBindingData,
            newItem: AlbumBindingData
        ): Boolean {
            val areContentsTheSame = TextUtils.equals(oldItem.title, newItem.title) &&
                    TextUtils.equals(oldItem.subTitle, newItem.subTitle)
            MyLog.i(TAG, "areContentsTheSame: $areContentsTheSame")
            return areContentsTheSame
        }

    }

}