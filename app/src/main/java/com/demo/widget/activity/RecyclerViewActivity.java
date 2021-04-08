package com.demo.widget.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.customview.R;
import com.demo.widget.bean.Fruit;
import com.demo.widget.utils.FruitAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RecyclerViewActivity";

    private FruitAdapter adapter;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    static int cnt = 0;
    DefaultItemAnimator defaultItemAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

//        layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this, 1);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new FruitAdapter();
        recyclerView.setAdapter(adapter);

        defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(500);
        defaultItemAnimator.setChangeDuration(500);
//        defaultItemAnimator.setRemoveDuration(500);
        recyclerView.setItemAnimator(defaultItemAnimator);

        findViewById(R.id.switch_layout).setOnClickListener(this);
        findViewById(R.id.add_item).setOnClickListener(this);
        findViewById(R.id.change_item).setOnClickListener(this);
        findViewById(R.id.remove_item).setOnClickListener(this);
        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick getId: " + v.getId());
        switch (v.getId()) {
            case R.id.switch_layout:
                if (layoutManager.getSpanCount() == 1) {
                    layoutManager.setSpanCount(3);
                } else {
                    layoutManager.setSpanCount(1);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.add_item:
                Toast.makeText(this, "Apple", Toast.LENGTH_SHORT).show();
                adapter.add(3, new Fruit("Apple", R.drawable.apple_pic));
                recyclerView.scrollToPosition(3);
                break;
            case R.id.change_item:
                Toast.makeText(this, "Apple Apple", Toast.LENGTH_SHORT).show();
                adapter.change(1, new Fruit((cnt++ & 0x1) == 1 ? "other Apple":"Apple", R.drawable.apple_pic));
                break;
            case R.id.remove_item:
                adapter.remove(1);
                break;
            default:
                break;
        }
    }
}