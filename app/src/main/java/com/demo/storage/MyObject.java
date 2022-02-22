package com.demo.storage;

import android.os.Parcel;

import com.tencent.wink.apt.annotation.PackClass;

import java.util.Objects;

import io.packable.PackCreator;
import io.packable.PackEncoder;
import io.packable.Packable;

@PackClass
public class MyObject implements Packable {
    long id;
    String info;

    public MyObject(long id, String info) {
        this.id = id;
        this.info = info;
    }

    private MyObject(Parcel in) {
        id = in.readLong();
        info = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyObject)) return false;
        MyObject object = (MyObject) o;
        return id == object.id &&
                Objects.equals(info, object.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, info);
    }

    @Override
    public String toString() {
        return "TestObject{" + "id=" + id + ", info='" + info + '\'' + '}';
    }

    @Override
    public void encode(PackEncoder encoder) {
        encoder.putLong(0, id)
                .putString(1, info);
    }

    public static final PackCreator<MyObject> CREATOR = decoder -> {
        MyObject obj = new MyObject(decoder.getLong(0), decoder.getString(1));
        decoder.recycle();
        return obj;
    };

}
