package com.demo.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.R;
import com.walker.storage.winkkv.WinkKV;

public class WinkKVDemoActivity extends AppCompatActivity {


    private EditText inputEV;
    private Button saveBtn;
    private Button showBtn;
    private TextView showTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wink_k_v_demo);


        inputEV = findViewById(R.id.wink_kv_et);
        saveBtn = findViewById(R.id.wink_kv_save_value_btn);
        showBtn = findViewById(R.id.wink_kv_shwo_value_btn);
        showTV = findViewById(R.id.wink_kv_show_tv);


        String NAME = "common_store";
        String path = getFilesDir().getAbsolutePath() + "/wink_kv";
        WinkKV kv = new WinkKV.Builder(path, NAME).build();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!kv.getBoolean("flag")) {
                    kv.putBoolean("flag", true);
                }

                if (!TextUtils.isEmpty(inputEV.getText())) {
                    kv.putString("key", inputEV.getText().toString());
                }
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTV.setText(kv.getString("key"));
            }
        });

    }
}