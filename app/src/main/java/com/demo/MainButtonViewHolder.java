package com.demo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainButtonViewHolder extends RecyclerView.ViewHolder {

    private final Button button;

    public MainButtonViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.main_item_button);
    }

    public static MainButtonViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false);
        return new MainButtonViewHolder(view);
    }

    public void bind(MainButton mainButton) {
        button.setText(mainButton.name);
        if (mainButton.getColor() != -1) {
            button.setBackgroundColor(mainButton.getColor());
        }

        if (mainButton.onclickListener != null) {
            button.setOnClickListener(v -> mainButton.onclickListener.onClickListener());
        } else {
            button.setOnClickListener(v -> startActivity(button.getContext(), mainButton.jumpClass));
        }
    }

    private void startActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }
}
