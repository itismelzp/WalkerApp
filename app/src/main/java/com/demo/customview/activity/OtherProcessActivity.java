package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.storage.WordListAdapter;
import com.demo.storage.utils.MMKVUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.wink.storage.winkdb.viewmodel.WordViewModel;
import com.tencent.wink.storage.winkkv.WinkKV;
import com.tencent.wink.storage.winkkv.WinkKVUtil;
import com.tencent.wink.storage.winkkv.multiprocess.WinkMPKV;


public class OtherProcessActivity extends AppCompatActivity {

    private static final String TAG = "OtherProcessActivity";

    private long startTime;

    private WordViewModel mWordViewModel;
    private TextView mTextView;
    private RecyclerView mRecyclerView;

    private Button kvShowBtn;
    private TextView kvTestTV;
    private EditText insertET;
    private Button insertValueBtn;
    
    public static final String STR_KEY = "str_key";
    public static final String MP_STR_KEY = "mp_str_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);

        startTime = getIntent().getLongExtra("startTime", 0L);
        Log.i(TAG, "[onCreate] start time: " +  startTime);

        mTextView = findViewById(R.id.text_other_process);
        mRecyclerView = findViewById(R.id.word_list);
        kvShowBtn = findViewById(R.id.kv_show_btn);
        kvTestTV = findViewById(R.id.kv_test_tv);
        insertET = findViewById(R.id.input_value);
        insertValueBtn = findViewById(R.id.insert_value);

        WinkKV winkKV = WinkKVUtil.getWinkKV(this);
        WinkMPKV winkMPKV  = WinkKVUtil.getWinkMPKV(this);
        MMKV mmKV = MMKVUtil.getMMKV();
        MMKV mpmmkv = MMKVUtil.getMPMMKV();

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
        mWordViewModel.getAllWords().observe(this, words -> {
            Log.d(TAG, "words: " + words);
            adapter.submitList(words);
        });

        kvShowBtn.setOnClickListener(view -> {
            kvTestTV.setText(String.format("winkKV: %s, winkMPKV: %s, mmKV: %s, mpmmkv: %s;",
                    winkKV.getString(STR_KEY),
                    winkMPKV.getString(MP_STR_KEY),
                    mmKV.decodeString(STR_KEY),
                    mpmmkv.decodeString(MP_STR_KEY)
            ));
        });

        insertValueBtn.setOnClickListener(view -> {
            String value = insertET.getText().toString();
            if (!TextUtils.isEmpty(value)) {
                winkKV.putString(STR_KEY, value);
                winkMPKV.putString(MP_STR_KEY, value + "_mp");
                mmKV.encode(STR_KEY, value);
                mpmmkv.putString(MP_STR_KEY, value + "_mp");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        long endTime = System.currentTimeMillis();
        long timeCost = endTime - startTime;
        Log.i(TAG, "[onResume] end time: " +  endTime);
        Log.i(TAG, "[onResume] cost time: " +  timeCost + "ms");
        Toast.makeText(this, "start service time cost: " + timeCost + "ms", Toast.LENGTH_SHORT).show();
    }
}