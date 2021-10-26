package com.demo.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by walkerzpli on 2021/8/6.
 */
public class WordViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;
    private final TextView createTimeTV;

    private WordViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.textView);
        createTimeTV = itemView.findViewById(R.id.createTimeTV);
    }

    public void bind(String text, long createTime) {
        wordItemView.setText(text);
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        createTimeTV.setText(ft.format(new Date(createTime)));
    }

    static WordViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_recyclerview_item, parent, false);
        return new WordViewHolder(view);
    }
}
