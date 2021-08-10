package com.demo.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;


/**
 * Created by walkerzpli on 2021/8/6.
 */
class WordViewHolder extends RecyclerView.ViewHolder{
    private final TextView wordItemView;

    private WordViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        wordItemView.setText(text);
    }

    static WordViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_recyclerview_item, parent, false);
        return new WordViewHolder(view);
    }
}
