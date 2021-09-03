package com.demo.storage;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.walker.storage.room.Word;
import com.walker.storage.room.WordViewModel;

/**
 * Created by walkerzpli on 2021/8/6.
 */
public class WordListAdapter extends ListAdapter<Word, WordViewHolder> {

    private WordViewModel mWordViewModel;

    public WordListAdapter(@NonNull DiffUtil.ItemCallback<Word> diffCallback) {
        super(diffCallback);
    }

    public WordListAdapter(@NonNull DiffUtil.ItemCallback<Word> diffCallback, WordViewModel wordViewModel) {
        super(diffCallback);
        this.mWordViewModel = wordViewModel;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word current = getItem(position);
        holder.bind(current.getContent());

        holder.itemView.setOnClickListener(view -> mWordViewModel.delete(current.getContent()));
    }

    public static class WordDiff extends DiffUtil.ItemCallback<Word> {

        @Override
        public boolean areItemsTheSame(@NonNull  Word oldItem, @NonNull  Word newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getContent().equals(newItem.getContent());
        }
    }

}
