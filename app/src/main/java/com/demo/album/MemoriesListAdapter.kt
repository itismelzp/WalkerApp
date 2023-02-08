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
import com.demo.R
import com.demo.customview.utils.ViewUtils

/**
 * Created by lizhiping on 2023/2/7.
 * <p>
 * description
 */
class MemoriesListAdapter(diffCallback: DiffUtil.ItemCallback<AlbumBindingData>) :
    ListAdapter<AlbumBindingData, MemoriesListAdapter.MemoriesViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemoriesViewHolder.create(parent)

    override fun onBindViewHolder(holder: MemoriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MemoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var blockImage: CornerImageView
        private var titleText: TextView
        private var subTitleText: TextView

        init {
            blockImage = itemView.findViewById(R.id.main_explorer_album_set_item_image)
            titleText = itemView.findViewById(R.id.main_explorer_album_set_item_title_text)
            subTitleText = itemView.findViewById(R.id.main_explorer_album_set_item_sub_title_text)
        }

        companion object {
            fun create(parent: ViewGroup): MemoriesViewHolder =
                MemoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.explore_memories_album_set_block, parent, false))
        }

        fun bind(data: AlbumBindingData) {
            blockImage.setImageDrawable(data.drawable)
            blockImage.setRoundCorner(ViewUtils.dpToPx(24f))
            titleText.text = data.title
            subTitleText.text = data.subTitle
        }
    }

    class MainDiffItemCallback : DiffUtil.ItemCallback<AlbumBindingData>() {

        override fun areItemsTheSame(oldItem: AlbumBindingData, newItem: AlbumBindingData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AlbumBindingData, newItem: AlbumBindingData): Boolean {
            return TextUtils.equals(oldItem.title, newItem.title) &&
                    TextUtils.equals(oldItem.subTitle, newItem.subTitle)
        }

    }

    class SpaceItemDecoration : RecyclerView.ItemDecoration {

        private val leftSpace: Int
        private val topSpace: Int
        private val rightSpace: Int
        private val bottomSpace: Int

        private val defaultSpace = ViewUtils.dpToPx(2f)

        constructor() {
            leftSpace = defaultSpace
            topSpace = ViewUtils.dpToPx(0f)
            rightSpace = defaultSpace
            bottomSpace = ViewUtils.dpToPx(0f)
        }

        constructor(space: Int) {
            this.leftSpace = space
            this.topSpace = space
            this.rightSpace = space
            this.bottomSpace = space
        }

        constructor(leftSpace: Int, topSpace: Int, rightSpace: Int, bottomSpace: Int) {
            this.leftSpace = leftSpace
            this.topSpace = topSpace
            this.rightSpace = rightSpace
            this.bottomSpace = bottomSpace
        }

        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = ViewUtils.dpToPx(24)
            }
            if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                outRect.right = ViewUtils.dpToPx(24)
            }
        }

    }

}