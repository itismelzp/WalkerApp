package com.demo.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.R;
import com.demo.customview.activity.OtherProcessActivity;
import com.demo.storage.utils.MMKVUtil;
import com.demo.storage.utils.WinkKVUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.wink.storage.winkkv.WinkKV;

public class WinkKVDemoActivity extends AppCompatActivity {


    private EditText inputEV;
    private Button saveBtn;
    private Button showBtn;
    private Button gotoOtherProcess;
    private TextView showTV;

    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wink_k_v_demo);


        inputEV = findViewById(R.id.wink_kv_et);
        saveBtn = findViewById(R.id.wink_kv_save_value_btn);
        showBtn = findViewById(R.id.wink_kv_shwo_value_btn);
        showTV = findViewById(R.id.wink_kv_show_tv);
        gotoOtherProcess = findViewById(R.id.goto_other_process);


        WinkKV winkKV = WinkKVUtil.getWinkKV(this);
        MMKV mmKV = MMKVUtil.getMultiProcessMMKV();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(inputEV.getText())) {
                    winkKV.putString("str_key", inputEV.getText().toString());
                    mmKV.encode("str_key", inputEV.getText().toString());
                }
                cnt++;
                winkKV.putInt("int_key", cnt);
                mmKV.encode("int_key", cnt);
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "winkKV: " + winkKV.getString("str_key") + "; mmKV: " + mmKV.decodeString("str_key");
                showTV.setText(text + "，winkKV.getAll：" + winkKV.getAll().toString());
            }
        });

        gotoOtherProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinkKVDemoActivity.this, OtherProcessActivity.class);
                startActivity(intent);
            }
        });

    }
}