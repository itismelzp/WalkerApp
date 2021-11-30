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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.R;
import com.demo.storage.WordListAdapter;
import com.tencent.mmkv.MMKV;
import com.walker.storage.room.model.Word;
import com.walker.storage.room.viewmodel.WordViewModel;
import com.walker.storage.winkkv.WinkKV;

import java.util.List;

public class OtherProcessActivity extends AppCompatActivity {

    private static final String TAG = "OtherProcessActivity";

    private WordViewModel mWordViewModel;
    private TextView mTextView;
    private RecyclerView mRecyclerView;

    private Button kvTestBtn;
    private TextView kvTestTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);
        mTextView = findViewById(R.id.text_other_process);
        mRecyclerView = findViewById(R.id.word_list);
        kvTestBtn = findViewById(R.id.kv_show_btn);
        kvTestTV = findViewById(R.id.kv_test_tv);

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


        String NAME = "common_store";
        String path = getFilesDir().getAbsolutePath() + "/wink_kv";
        WinkKV winkKV = new WinkKV.Builder(path, NAME).build();
        MMKV mmKV = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);

        kvTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kvTestTV.setText("winkKV: " + winkKV.getString("key") + "; mmKV: " + mmKV.decodeString("mmkv_key"));
            }
        });

    }
}