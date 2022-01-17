package com.demo.storage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Parcelable序列化测试类
 * <p>
 * Created by walkerzpli on 2022/1/17.
 */
public class MyParcelObject implements Parcelable {

    private int a;
    private int b;
    private int c;
    private String str;

    public MyParcelObject(int a, int b, int c, String str) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.str = str;
    }

    protected MyParcelObject(Parcel in) {
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        str = in.readString();

    }

    public static final Creator<MyParcelObject> CREATOR = new Creator<MyParcelObject>() {
        @Override
        public MyParcelObject createFromParcel(Parcel in) {
            return new MyParcelObject(in);
        }

        @Override
        public MyParcelObject[] newArray(int size) {
            return new MyParcelObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(a);
        parcel.writeInt(b);
        parcel.writeInt(c);
        parcel.writeString(str);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyParcelObject that = (MyParcelObject) o;
        return a == that.a && b == that.b && c == that.c && Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, str);
    }

    @Override
    public String toString() {
        return "MyParcelObject{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", str='" + str + '\'' +
                '}';
    }
}
