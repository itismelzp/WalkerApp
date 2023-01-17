package com.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by lizhiping on 2023/1/17.
 * <p>
 * description
 */
class FilterViewHolder(
    itemView: View,
    private val checkedCallback: (type: Int, isChecked: Boolean) -> Unit?
) : RecyclerView.ViewHolder(itemView) {

    private val checkBox: CheckBox

    init {
        checkBox = itemView.findViewById(R.id.filter_cb)
    }

    fun bind(mainButton: MainButton) {
        checkBox.isChecked = !mainButton.isHide
        checkBox.setBackgroundColor(mainButton.color)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainButton.isHide = !isChecked
            checkedCallback(mainButton.type, isChecked)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            checkedCallback: (type: Int, isChecked: Boolean) -> Unit?
        ): FilterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_filter_item, parent, false)
            return FilterViewHolder(view, checkedCallback)
        }
    }

}