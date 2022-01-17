package com.demo.storage.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

/**
 * Parcelable序列化工具类
 * <p>
 * Created by walkerzpli on 2022/1/17.
 */
public class ParcelableUtil {

    public static byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();

        Log.d("ParcelableTest", "bytes = " + Arrays.toString(bytes) + "parcel" + parcel.toString());
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
