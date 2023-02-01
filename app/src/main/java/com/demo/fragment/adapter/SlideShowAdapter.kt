package com.demo.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.R


/**
 * Created by lizhiping on 2023/1/30.
 * <p>
 * description
 */
class SlideShowAdapter(private val colors: List<Int>) :
    RecyclerView.Adapter<SlideShowAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.slide_show_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = position % 4
        holder.titleTv.text = "item $i"
        holder.container.setBackgroundColor(colors[i])
    }

    override fun getItemCount(): Int {
        //实现无限轮播
        return Int.MAX_VALUE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: RelativeLayout
        var titleTv: TextView

        init {
            container = itemView.findViewById(R.id.container)
            titleTv = itemView.findViewById(R.id.tv_title)
        }
    }
}