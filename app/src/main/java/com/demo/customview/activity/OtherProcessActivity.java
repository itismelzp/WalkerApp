package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.demo.R;
import com.demo.storage.WordListAdapter;
import com.walker.storage.room.model.Word;
import com.walker.storage.room.viewmodel.WordViewModel;

import java.util.List;

public class OtherProcessActivity extends AppCompatActivity {

    private static final String TAG = "OtherProcessActivity";

    private WordViewModel mWordViewModel;
    private TextView mTextView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);
        mTextView = findViewById(R.id.text_other_process);
        mRecyclerView = findViewById(R.id.word_list);

        Intent intent = getIntent();
        if (intent != null) {
            String text = intent.getStringExtra("TAG");
            if (!TextUtils.isEmpty(text)) {
                mTextView.setText(text);
            }
        }

        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff(), mWordViewModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                Log.d(TAG, "words: " + words);
                adapter.submitList(words);
            }
        });

    }
}