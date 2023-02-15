package com.demo.album

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

}