package com.demo.customview.ryg;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.customview.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Locale;

public class ViewEventDispatchDemoActivity extends AppCompatActivity {

    private static final String TAG = "ViewEventDispatchDemo";

    private static final int TOUCH_SLOP = ViewUtils.getTouchSlop();

    private DispatchView mView;

    private OuterInterceptHorizontalScrollViewEx mOuterInterceptContainer;
    private InnerInterceptHorizontalScrollViewEx mInnerInterceptContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_dispatch_demo);

        mView = findViewById(R.id.test_id);

        initOuterInterceptView();
        initInnerInterceptView();

    }

    private void initOuterInterceptView() {
        LayoutInflater inflater = getLayoutInflater();
        mOuterInterceptContainer = findViewById(R.id.outer_intercept_horizontal_scroll_view);
        final int screenWidth = ViewUtils.getScreenWidth();
        final int screenHeight = ViewUtils.getScreenHeight();
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    R.layout.outer_intercept_case_content_layout, mOuterInterceptContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createOuterList(layout);
            mOuterInterceptContainer.addView(layout);
        }
    }

    private void createOuterList(ViewGroup layout) {
        ListView listView = layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.intercept_case_content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ViewEventDispatchDemoActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initInnerInterceptView() {
        LayoutInflater inflater = getLayoutInflater();
        mInnerInterceptContainer = findViewById(R.id.inner_intercept_horizontal_scroll_view);
        final int screenWidth = ViewUtils.getScreenWidth();
        final int screenHeight = ViewUtils.getScreenHeight();
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    R.layout.inner_intercept_case_content_layout, mInnerInterceptContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color
                    .rgb(255 / (i + 1), 255 / (i + 1), 0));
            createInnerList(layout);
            mInnerInterceptContainer.addView(layout);
        }
    }

    private void createInnerList(ViewGroup layout) {
        InnerInterceptListViewEx listView = layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.intercept_case_content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setHorizontalScrollViewEx2(mInnerInterceptContainer);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ViewEventDispatchDemoActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.post(new Runnable() {
            @Override
            public void run() {
                String log = String.format(Locale.getDefault(),
                        "getDensity: %f, getX: %f, getY: %f " +
                                "left: %d, top: %d, right: %d, bottom: %d, width: %d, height: %d",
                        ViewUtils.getDensity(),
                        mView.getX(), mView.getY(),
                        mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom(),
                        mView.getWidth(), ViewUtils.pxTosp(mView.getHeight())
                );
                Log.d(TAG, log);
            }
        });
    }
}