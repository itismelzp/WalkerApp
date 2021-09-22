package com.walker.storage.room.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/9/10.
 */
public class Address {

    public String street;

    public String state;

    public String city;

//    @PrimaryKey
    @ColumnInfo(name = "post_code")
    public String postCode;

    public Address(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}
