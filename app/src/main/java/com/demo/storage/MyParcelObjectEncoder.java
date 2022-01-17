package com.demo.storage;

import android.os.Parcel;

import com.demo.storage.utils.ParcelableUtil;
import com.walker.storage.winkkv.WinkKV;


/**
 * Parcelable对象的编、解码器
 * <p>
 * Created by walkerzpli on 2022/1/17.
 */
public class MyParcelObjectEncoder implements WinkKV.Encoder<MyParcelObject>  {

    public static MyParcelObjectEncoder INSTANCE = new MyParcelObjectEncoder();

    private MyParcelObjectEncoder() {
    }

    @Override
    public String tag() {
        return "MyParcelObjectEncoder";
    }

    @Override
    public byte[] encode(MyParcelObject obj) {
        return ParcelableUtil.marshall(obj);
    }

    @Override
    public MyParcelObject decode(byte[] bytes, int offset, int length) {
        Parcel parcel = ParcelableUtil.unmarshall(bytes, offset, length);
        return MyParcelObject.CREATOR.createFromParcel(parcel);
    }
}
