package com.demo.storage;

import androidx.annotation.NonNull;

import com.walker.storage.winkkv.WinkKV;

import io.packable.PackDecoder;
import io.packable.PackEncoder;


public class MyObjectEncoder implements WinkKV.Encoder<MyObject> {

    public static MyObjectEncoder INSTANCE = new MyObjectEncoder();

    private MyObjectEncoder() {
    }

    @Override
    public String tag() {
        return "MyObjectEncoder";
    }

    @Override
    public byte[] encode(@NonNull MyObject obj) {
        return PackEncoder.marshal(obj);
    }

    @Override
    public MyObject decode(@NonNull byte[] bytes, int offset, int length) {
        return PackDecoder.unmarshal(bytes, offset, length, MyObject.CREATOR);
    }

}
