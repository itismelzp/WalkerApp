package com.demo.logger;

/**
 * Created by lizhiping on 2023/3/29.
 * <p>
 * description
 */
public final class ExifEntry {

    public String mOrientation;
    public String mDateTime;
    public String mDateTimeOriginal;
    public String mDateTimeDigitized;

    public Long mTimeStamp;

    public String timeStamp;

    @Override
    public String toString() {
        return "ExifEntry{" +
                "mOrientation='" + mOrientation + '\'' +
                ", mDateTime=" + mDateTime +
                ", mDateTimeOriginal='" + mDateTimeOriginal + '\'' +
                ", mDateTimeDigitized='" + mDateTimeDigitized + '\'' +
                ", mTimeStamp=" + mTimeStamp + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}