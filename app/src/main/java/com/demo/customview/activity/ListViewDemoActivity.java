package com.demo.customview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewDemoActivity extends AppCompatActivity {

    // fruitList用于存储数据
    private List<Fruit> fruitList = new ArrayList<>();
    private List<Animal> animalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_demo);


        // 先拿到数据并放在适配器上
        initFruits(); //初始化水果数据
        FruitAdapter adapter = new FruitAdapter(ListViewDemoActivity.this, R.layout.fruit_item, fruitList);
        initAnimals();
        AnimalAdapter animalAdapter = new AnimalAdapter(ListViewDemoActivity.this, R.layout.animal_item, animalList);

        final ImageView imageView = findViewById(R.id.list_icon);

        // 将适配器上的数据传递给listView
        final ListView listView = findViewById(R.id.list_view);
        final ListView animalListView = findViewById(R.id.animal_list_view);

        listView.setAdapter(adapter);
        animalListView.setAdapter(animalAdapter);

        // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(ListViewDemoActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
//                listView.addFooterView(animalListView);
                listView.addHeaderView(imageView);
            }
        });

    }

    // 初始化数据
    private void initFruits() {
        for (int i = 0; i < 1; i++) {
            Fruit a = new Fruit("a", R.drawable.a);
            fruitList.add(a);
            Fruit b = new Fruit("B", R.drawable.zhaoyun);
            fruitList.add(b);
            Fruit c = new Fruit("C", R.drawable.zhaoyun2);
            fruitList.add(c);
            Fruit d = new Fruit("D", R.drawable.circle_close_icon);
            fruitList.add(d);
        }
    }

    // 初始化数据
    private void initAnimals() {
        for (int i = 0; i < 1; i++) {
            Animal a = new Animal("a", R.drawable.a, R.drawable.a);
            animalList.add(a);
            Animal b = new Animal("B", R.drawable.zhaoyun, R.drawable.zhaoyun);
            animalList.add(b);
            Animal c = new Animal("C", R.drawable.zhaoyun2, R.drawable.zhaoyun2);
            animalList.add(c);
            Animal d = new Animal("D", R.drawable.circle_close_icon, R.drawable.circle_close_icon);
            animalList.add(d);
        }
    }

}
