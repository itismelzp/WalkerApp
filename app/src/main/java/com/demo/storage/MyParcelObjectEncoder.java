package com.demo.storage;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.wink.storage.winkkv.WinkKV;


/**
 * Parcelable对象的编、解码器
 * <p>
 * Created by walkerzpli on 2022/1/17.
 */
public class MyParcelObjectEncoder implements WinkKV.Encoder<MyParcelObject> {

    public static MyParcelObjectEncoder INSTANCE = new MyParcelObjectEncoder();

    private MyParcelObjectEncoder() {
    }

    @Override
    public String tag() {
        return "MyParcelObjectEncoder";
    }

    @Override
    public byte[] encode(MyParcelObject obj) {
        return marshall(obj);
    }

    @Override
    public MyParcelObject decode(byte[] bytes, int offset, int length) {
        Parcel parcel = unmarshall(bytes, offset, length);
        return MyParcelObject.CREATOR.createFromParcel(parcel);
    }

    public static byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes, int offset, int length) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, offset, length);
        parcel.setDataPosition(0);
        return parcel;
    }

}
