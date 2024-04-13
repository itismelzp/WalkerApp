package com.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.databinding.FragmentMainFilterItemBinding

/**
 * Created by lizhiping on 2023/1/17.
 * <p>
 * description
 */
class FilterViewHolder(
    private val binding: FragmentMainFilterItemBinding,
    private val checkedCallback: (type: Int, isChecked: Boolean) -> Unit?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(mainButton: MainButton) {
        binding.filterCb.isChecked = !mainButton.isHide
        binding.filterCb.setBackgroundColor(mainButton.color)
        binding.filterCb.setOnCheckedChangeListener { _, isChecked ->
            mainButton.isHide = !isChecked
            checkedCallback(mainButton.type, isChecked)
        }
        binding.filterTitle.text = mainButton.typeName
    }

    companion object {
        fun create(
            parent: ViewGroup,
            checkedCallback: (type: Int, isChecked: Boolean) -> Unit?
        ): FilterViewHolder = FilterViewHolder(
            FragmentMainFilterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), checkedCallback
        )
    }

}