package com.demo.customview.ryg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.demo.R;
import com.tencent.wink.apt.annotation.BindButton;
import com.tencent.wink.apt.library.BindButtonTools;

public class ViewDispatchDemoActivity extends AppCompatActivity {

    @BindButton(resId = R.id.view_event_dispatch_demo_btn, clazz = ViewEventDispatchDemoActivity.class)
    private Button viewEventDispatchDemoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispather_demo);

        BindButtonTools.bind(this);
    }
}