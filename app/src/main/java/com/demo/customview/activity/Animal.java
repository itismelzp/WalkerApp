package com.demo.customview.activity;

/**
 * Created by walkerzpli on 2020/11/25.
 */
public class Animal {
    private String name;
    private int imageId;
    private int imageIcon;

    public Animal(String name, int imageId, int imageIcon){
        this.name=name;
        this.imageId=imageId;
        this.imageIcon = imageIcon;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return  imageId;
    }

    public int getImageIcon() {
        return imageIcon;
    }
}
