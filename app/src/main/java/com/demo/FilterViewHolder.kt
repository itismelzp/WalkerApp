package com.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
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

    private val checkBox = itemView.findViewById<CheckBox>(R.id.filter_cb)
    private val filterTitle = itemView.findViewById<TextView>(R.id.filter_title)

    fun bind(mainButton: MainButton) {
        checkBox.isChecked = !mainButton.isHide
        checkBox.setBackgroundColor(mainButton.color)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainButton.isHide = !isChecked
            checkedCallback(mainButton.type, isChecked)
        }
        filterTitle.text = mainButton.typeName
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