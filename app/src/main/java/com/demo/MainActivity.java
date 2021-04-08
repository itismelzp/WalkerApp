package com.demo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.customview.R;
import com.demo.customview.activity.CustomMatrixActivity;
import com.demo.customview.activity.CustomShaderActivity;
import com.demo.customview.activity.ListViewDemoActivity;
import com.demo.customview.activity.OtherProcessActivity;
import com.demo.customview.aige.activity.AigeActivity;
import com.demo.customview.sloop.activity.CustomSloopMenuActivity;
import com.demo.customview.zhy.activity.CustomViewActivity;
import com.demo.widget.activity.RecyclerViewActivity;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton(R.id.btn_custom_view, CustomViewActivity.class);
        initButton(R.id.btn_custom_drawable, CustomShaderActivity.class);
        initButton(R.id.btn_custom_sloop_menu, CustomSloopMenuActivity.class);
        initButton(R.id.btn_custom_matrix_view, CustomMatrixActivity.class);
        initButton(R.id.btn_aige_custom_view, AigeActivity.class);
        initButton(R.id.btn_list_view_demo, ListViewDemoActivity.class);
        initButton(R.id.test_other_process, OtherProcessActivity.class);
        initButton(R.id.btn_test_recycle_view, RecyclerViewActivity.class);

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());

//        startTMGame();
    }

    private void startTMGame() {

        try {
            getAppInfo(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class<?> clazz = Class.forName("com.demo.customview.activity");
            Method methodMain = clazz.getMethod("startTMGame", String[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String customPkgName = System.getenv("CUSTOM_PACKAGE_NAME");
        Log.d(TAG, "getenv: " + customPkgName);
        Set<ProviderInfo> sets = new HashSet<>();
        List<ProviderInfo> providerInfos = getPackageManager().queryContentProviders(null, 0,0);
        for (ProviderInfo providerInfo : providerInfos) {
            Log.d(TAG, "providerInfo.authority: " + providerInfo.authority);
            if (!TextUtils.isEmpty(providerInfo.authority)) {
                if (providerInfo.authority.contains("com.tencent.tmgp.sgame")) {
                    String type = getContentResolver().getType(Uri.parse(providerInfo.authority));
                    Log.d(TAG, "getContentResolver.type: " + type);
                }
            }
        }
    }

    private void getAppInfo(Context context) throws Exception{
        PackageManager packageManager = context.getPackageManager();
        //获取所有安装的app
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for(PackageInfo info : installedPackages){
            String packageName = info.packageName;//app包名
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            String appName = (String) packageManager.getApplicationLabel(ai);//获取应用名称
            Log.d(TAG + "getAppInfo", "packageName: " + packageName + ", appName: " + appName);
        }
    }

    private void initButton(int resId, final Class<?> clz) {
        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(clz);
            }
        });
    }

    private void startActivity(Class<?> clz) {
        Intent intent = new Intent(MainActivity.this, clz);
        intent.putExtra("TAG", "MainActivity");
        startActivity(intent);
    }

}
