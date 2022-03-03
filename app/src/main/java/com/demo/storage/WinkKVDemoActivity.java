package com.demo.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.R;
import com.demo.customview.activity.OtherProcessActivity;
import com.demo.storage.utils.MMKVUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.wink.storage.winkkv.WinkKV;
import com.tencent.wink.storage.winkkv.WinkKVUtil;
import com.tencent.wink.storage.winkkv.multiprocess.WinkMPKV;

import java.util.HashMap;
import java.util.Map;

public class WinkKVDemoActivity extends AppCompatActivity {

    private static final String TAG = "WinkKVDemoActivity";

    private EditText inputEV;
    private Button saveBtn;
    private Button showBtn;
    private Button gotoOtherProcess;
    private Button getMPKVBtn;
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
        getMPKVBtn = findViewById(R.id.get_mp_kv);


        WinkKV winkKV = WinkKVUtil.getWinkKV(this);
        MMKV mmKV = MMKVUtil.getMPMMKV();


        saveBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(inputEV.getText())) {
                winkKV.putString("str_key", inputEV.getText().toString());
                mmKV.encode("str_key", inputEV.getText().toString());
            }
            cnt++;
            winkKV.putInt("int_key", cnt);
            mmKV.encode("int_key", cnt);
        });

        showBtn.setOnClickListener(view -> {
            String text = "winkKV: " + winkKV.getString("str_key") + "; mmKV: " + mmKV.decodeString("str_key");
            showTV.setText(text + "，winkKV.getAll：" + winkKV.getAll().toString());
        });

        gotoOtherProcess.setOnClickListener(view -> {
            Intent intent = new Intent(WinkKVDemoActivity.this, OtherProcessActivity.class);
            startActivity(intent);
        });

        Map<String, Object> datas = new HashMap<>();
        datas.put("mp_int_key", 123);
        datas.put("mp_long_key", 123456L);
        datas.put("mp_float_key", 123.456F);
        datas.put("mp_double_key", 123456.789D);
        datas.put("mp_string_key", "test_mp_str");

        WinkMPKV winkMPKV  = WinkKVUtil.getWinkMPKV(WinkKVDemoActivity.this);
        winkMPKV.putAll(datas);
        getMPKVBtn.setOnClickListener(view -> {
            Log.i(TAG, "winkMPKV: " + winkMPKV.getString(OtherProcessActivity.MP_STR_KEY));
            Log.i(TAG, "winkMPKV: " + winkMPKV.getAll());
        });

    }
}