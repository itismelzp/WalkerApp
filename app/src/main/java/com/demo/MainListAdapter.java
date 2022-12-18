package com.demo;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.customview.utils.ViewUtils;

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

    public static class MainDiff extends DiffUtil.ItemCallback<MainButton> {

        @Override
        public boolean areItemsTheSame(@NonNull MainButton oldItem, @NonNull MainButton newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MainButton oldItem, @NonNull MainButton newItem) {
            return oldItem.name.equals(newItem.name);
        }
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public SpaceItemDecoration() {
            space = ViewUtils.dpToPx(2);
        }

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.set(space, space, space, space);
        }
    }

}
