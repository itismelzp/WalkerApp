package com.demo;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

public class MainListAdapter extends ListAdapter<MainButton, MainButtonViewHolder> {

    public MainListAdapter(@NonNull DiffUtil.ItemCallback<MainButton> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MainButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MainButtonViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MainButtonViewHolder holder, int position) {
        MainButton mainButton = getItem(position);
        holder.bind(mainButton);
    }

    @Override
    public void onBindViewHolder(@NonNull MainButtonViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public static class MainDiffItemCallback extends DiffUtil.ItemCallback<MainButton> {

        @Override
        public boolean areItemsTheSame(@NonNull MainButton oldItem, @NonNull MainButton newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MainButton oldItem, @NonNull MainButton newItem) {
            return oldItem.name.equals(newItem.name);
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull MainButton oldItem, @NonNull MainButton newItem) {
            Bundle payload = new Bundle();
            if (!oldItem.name.equals(newItem.name)) {
                payload.putString("KEY_NAME", newItem.name);
            }
            if (payload.size() == 0) {
                return null;
            }
            return payload;
        }
    }

}
