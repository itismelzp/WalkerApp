package com.demo

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Created by lizhiping on 2023/1/17.
 * <p>
 * description
 */
class FilterListAdapter(
    diffCallback: DiffUtil.ItemCallback<MainButton>,
    private val checkedCallback: (type: Int, isChecked: Boolean) -> Unit
) : ListAdapter<MainButton, FilterViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder.create(parent, checkedCallback)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ButtonFilterItemCallback : DiffUtil.ItemCallback<MainButton>() {
        override fun areItemsTheSame(oldItem: MainButton, newItem: MainButton): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MainButton, newItem: MainButton): Boolean {
            return oldItem.type == newItem.type
        }
    }

}