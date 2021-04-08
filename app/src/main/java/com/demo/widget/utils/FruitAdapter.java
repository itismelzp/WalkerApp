package com.demo.widget.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.customview.R;
import com.demo.widget.bean.Fruit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walkerzpli on 2021/4/8.
 */
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> mFruitList = new ArrayList<>();
    public FruitAdapter() {
        initFruits();
    }

    private void initFruits() {
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Apple", R.drawable.apple_pic);
            mFruitList.add(apple);
            Fruit banana = new Fruit("Banana", R.drawable.banana_pic);
            mFruitList.add(banana);
            Fruit orange = new Fruit("Orange", R.drawable.orange_pic);
            mFruitList.add(orange);
            Fruit watermelon = new Fruit("Watermelon", R.drawable.watermelon_pic);
            mFruitList.add(watermelon);
            Fruit pear = new Fruit("Pear", R.drawable.pear_pic);
            mFruitList.add(pear);
            Fruit grape = new Fruit("Grape", R.drawable.grape_pic);
            mFruitList.add(grape);
            Fruit pineapple = new Fruit("Pineapple", R.drawable.pineapple_pic);
            mFruitList.add(pineapple);
            Fruit strawberry = new Fruit("Strawberry", R.drawable.strawberry_pic);
            mFruitList.add(strawberry);
            Fruit cherry = new Fruit("Cherry", R.drawable.cherry_pic);
            mFruitList.add(cherry);
            Fruit mango = new Fruit("Mango", R.drawable.mango_pic);
            mFruitList.add(mango);
        }
    }

    @NonNull
    @Override
    public FruitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fruit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitAdapter.ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitImage = view.findViewById(R.id.fruit_image);
            fruitName = view.findViewById(R.id.fruit_name);
        }
    }

    public void add(int pos, Fruit fruit) {
        mFruitList.add(pos, fruit);
        notifyItemInserted(pos);
    }

    public void remove(int pos) {
        if (mFruitList.size() > pos) {
            mFruitList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void change(int pos, Fruit fruit) {
        if (mFruitList.size() > pos) {
            mFruitList.set(pos, fruit);
            notifyItemChanged(pos);
        }
    }

}
