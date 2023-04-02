package com.demo.utils;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

import static androidx.exifinterface.media.ExifInterface.TAG_DATETIME_ORIGINAL;
import static androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME_ORIGINAL;

import android.annotation.SuppressLint;
import android.media.ExifInterface;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.demo.logger.MyLog;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public final class ExifUtils {
    public static final int SUB_SEC_STR_MAX_LEN = 3;

    /**
     * copy from Android Q ExifInterface , which is marked as hide.
     */
    public static final String TAG_OFFSET_TIME = "OffsetTime";
    public static final String TAG_OFFSET_TIME_ORIGINAL = "OffsetTimeOriginal";
    public static final String UTC = "UTC";
    private static final int VALUE_ONE_THOUSAND = 1000;
    private static final int VALUE_TEN = 10;
    private static final int FILE_SIZE_MAX = 1024 * 1024;

    // Pattern to check non zero timestamp
    private static final Pattern NON_ZERO_TIME_PATTERN = Pattern.compile(".*[1-9].*");

    /**
     * save bitmap format
     */
    private static final String FORMAT_DATETIME = "%04d:%02d:%02d %02d:%02d:%02d";
    private static final String FORMAT_GPS_DATE_STAMP = "%04d:%02d:%02d";
    private static final String FORMAT_GPS_TIMESTAMP = "%02d:%02d:%02d";
    private static final String TAG = "ExifUtils";

    private static final ThreadLocal<SimpleDateFormat> FORMAT_THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Nullable
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter;
        }
    };

    private static final ThreadLocal<List<SimpleDateFormat>> FORMAT_THREAD_LOCAL_2 = ThreadLocal.withInitial(() -> {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return Arrays.asList(formatter1, formatter2);
    });

    private static final ThreadLocal<SimpleDateFormat> FORMAT_TZ_THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Nullable
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss XXX");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter;
        }
    };

    private static final String[] EXIF_TAGS = {ExifInterface.TAG_DATETIME, ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MODEL, ExifInterface.TAG_FLASH, ExifInterface.TAG_GPS_LATITUDE,
            ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LATITUDE_REF,
            ExifInterface.TAG_GPS_LONGITUDE_REF, ExifInterface.TAG_GPS_ALTITUDE,
            ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_TIMESTAMP,
            ExifInterface.TAG_GPS_DATESTAMP, ExifInterface.TAG_WHITE_BALANCE,
            ExifInterface.TAG_FOCAL_LENGTH, ExifInterface.TAG_GPS_PROCESSING_METHOD};

    private ExifUtils() {

    }

    /**
     * Returns number of milliseconds since Jan. 1, 1970, midnight UTC.
     * Returns -1 if the date time information if not available.
     * <p>
     * copy from Android Q ExifInterface.java which is marked as hide.
     *
     * @param exif exif
     */
    public static long getGpsDateTime(final ExifInterface exif) {
        if (exif == null) {
            return -1;
        }
        String date = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        String time = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
        if ((date == null) || (time == null)
                || (!NON_ZERO_TIME_PATTERN.matcher(date).matches()
                && !NON_ZERO_TIME_PATTERN.matcher(time).matches())) {
            return -1;
        }

        String dateTimeString = date + ' ' + time;
        ParsePosition pos = new ParsePosition(0);
        try {
            Date datetime = getDateFormat().parse(dateTimeString, pos);
            if (datetime == null) {
                return -1;
            }
            return datetime.getTime();
        } catch (IllegalArgumentException e) {
            MyLog.e(TAG, "getGpsDateTime", e);
        }
        return -1;
    }

    /**
     * Returns parsed {@code DateTimeOriginal} value, or -1 if unavailable or
     * invalid.
     * <p>
     * copy from Android Q ExifInterface.java which is marked as hide.
     *
     * @param exif exif
     */
    public static long getDateTimeOriginal(ExifInterface exif) {
        return parseDateTime(exif.getAttribute(TAG_DATETIME_ORIGINAL),
                exif.getAttribute(TAG_SUBSEC_TIME_ORIGINAL),
                exif.getAttribute(TAG_OFFSET_TIME_ORIGINAL));
    }

    /**
     * Returns parsed {@code DateTime} value, or -1 if unavailable or invalid.
     * <p>
     * copy from Android Q ExifInterface.java which is marked as hide.
     *
     * @param exif exif
     */
    public static long getDateTime(final ExifInterface exif) {
        if (exif == null) {
            return -1;
        }
        return parseDateTime(exif.getAttribute(ExifInterface.TAG_DATETIME),
                exif.getAttribute(ExifInterface.TAG_SUBSEC_TIME),
                exif.getAttribute(TAG_OFFSET_TIME));
    }

    public static long getDateTime2(final ExifInterface exif) {
        if (exif == null) {
            return -1;
        }

        String timeToParse;
        String originalTime = exif.getAttribute(TAG_DATETIME_ORIGINAL);
        String digitizedTime = exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);

        String dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);

        if (originalTime != null) {
            timeToParse = originalTime;
        } else if (digitizedTime != null) {
            timeToParse = digitizedTime;
        } else {
            timeToParse = dateTime;
        }

        if (timeToParse == null) {
            return -1;
        }

        return parseDateTime(timeToParse, null, null);
    }


    //返回当地时区与UTC的时差，如东八区 返回 “+08:00”
    //注意：不要频繁创建SimpleDateFormat
    @SuppressLint("SimpleDateFormat")
    public static String getRawOffsetTimeFromUTC() {
        return new SimpleDateFormat("XXX").format(new Date());
    }

    private static Date tryGetDateFormat(String dateTimeString, ParsePosition pos) {
        Date datetime = null;
        for (SimpleDateFormat simpleDateFormat : getDateFormat2()) {
            datetime = simpleDateFormat.parse(dateTimeString, pos);
            if (datetime != null) {
                break;
            }
        }
        return datetime;
    }

    private static long parseDateTime(String dateTimeString, String subSec, String offsetString) {
        if ((dateTimeString == null)
                || !NON_ZERO_TIME_PATTERN.matcher(dateTimeString).matches()) {
            return -1;
        }

        ParsePosition pos = new ParsePosition(0);
        try {
            // The exif field is in local time. Parsing it as if it is UTC will yield time
            // since 1/1/1970 local time
            Date datetime = tryGetDateFormat(dateTimeString, pos);

            if (offsetString != null) {
                dateTimeString = dateTimeString + " " + offsetString;
                ParsePosition position = new ParsePosition(0);
                datetime = getDateFormatTz().parse(dateTimeString, position);
            }

            if (datetime == null) {
                return -1;
            }

            long msecs = datetime.getTime();
            if (subSec != null) {
                msecs += parseSubSeconds(subSec);
            }
            return msecs;
        } catch (IllegalArgumentException e) {
            MyLog.e(TAG, "parseDateTime", e);
        }
        return -1;
    }

    private static long parseSubSeconds(@NonNull String subSec) {
        long sub = 0;
        try {
            if (ApiLevelUtil.isAtLeastAndroidR()) {
                // subSec一直乘以10直到数值大于100为止
                final int len = Math.min(subSec.length(), SUB_SEC_STR_MAX_LEN);
                sub = Long.parseLong(subSec.substring(0, len));
                for (int i = len; i < SUB_SEC_STR_MAX_LEN; i++) {
                    sub *= VALUE_TEN;
                }
            } else {
                // subSec一直除以10直到数值小于1000为止
                sub = Long.parseLong(subSec);
                while (sub > VALUE_ONE_THOUSAND) {
                    sub /= VALUE_TEN;
                }
            }

        } catch (NumberFormatException e) {
            MyLog.e(TAG, "parseSubSeconds", e);
        }
        return sub;
    }

    public static int getExifDegree(String filePath) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            return orientationToDegree(orientation);
        } catch (IOException ex) {
            // exif is null
        } catch (Throwable error) {
            MyLog.e(TAG, "getExifDegree", error);
        }
        return 0;
    }

    public static int getExifDegree(InputStream is) {
        try {
            ExifInterface exif = new ExifInterface(is);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            return orientationToDegree(orientation);
        } catch (IOException ex) {
            // exif is null
        } catch (Throwable error) {
            MyLog.e(TAG, "getExifDegree", error);
        }
        return 0;
    }

    public static int orientationToDegree(int orientation) {
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return AppConstants.Degree.DEGREE_90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return AppConstants.Degree.DEGREE_180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return AppConstants.Degree.DEGREE_270;
            default:
                return AppConstants.Degree.DEGREE_0;
        }
    }

    public static void setExifForSaveBitmap(long dateTaken, ExifInterface exif, double[] mLatLng) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.setTimeInMillis(dateTaken);
        String dateTime = getDateTime(calendar);
        exif.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
        exif.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, dateTime);
        exif.setAttribute(TAG_DATETIME_ORIGINAL, dateTime);
        exif.setAttribute(TAG_SUBSEC_TIME_ORIGINAL, String.valueOf(dateTaken % SECOND_IN_MILLIS));

        if (!GPS.isLatLngInvalid(mLatLng)) {
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(mLatLng[0]));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(mLatLng[0]));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(mLatLng[1]));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(mLatLng[1]));
        }

        calendar.setTimeInMillis(dateTaken);
        exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, getGpsDateStamp(calendar));
        exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, getGpsTimeStamp(calendar));
        try {
            exif.saveAttributes();
        } catch (IOException e) {
            MyLog.e(TAG, "setExifForSaveBitmap" + e);
        }
    }


    public static String getDateTime(Calendar calendar) {
        return String.format(Locale.ENGLISH, FORMAT_DATETIME,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));

    }

    public static String getGpsDateStamp(Calendar calendar) {
        return String.format(Locale.ENGLISH, FORMAT_GPS_DATE_STAMP,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));

    }

    public static String getGpsTimeStamp(Calendar calendar) {
        return String.format(Locale.ENGLISH, FORMAT_GPS_TIMESTAMP,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

    public static void setDateTakenToExifInterface(String filePath, long dateTaken) {
        if (filePath == null) {
            return;
        }
        long startTime = System.currentTimeMillis();
        try {
            ExifInterface exif = new ExifInterface(filePath);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTaken);
            String dateTime = getDateTime(calendar);
            exif.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
            exif.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, dateTime);

            exif.setAttribute(TAG_DATETIME_ORIGINAL, dateTime);
            exif.setAttribute(TAG_SUBSEC_TIME_ORIGINAL, String.valueOf(dateTaken % SECOND_IN_MILLIS));
            exif.setAttribute(ExifInterface.TAG_OFFSET_TIME_ORIGINAL, getRawOffsetTimeFromUTC());

            calendar.setTimeZone(TimeZone.getTimeZone(UTC));
            calendar.setTimeInMillis(dateTaken);

            exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, getGpsDateStamp(calendar));
            exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, getGpsTimeStamp(calendar));
            exif.saveAttributes();
            MyLog.d(TAG, "setExifInterface cost time =  " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            MyLog.e(TAG, "setExifInterface " + e);
        }
    }

    public static int getOrientation(InputStream is) {
        if (is == null) {
            return 0;
        }

        byte[] buf = new byte[8];
        int length = 0;

        // ISO/IEC 10918-1:1993(E)
        while (read(is, buf, 2) && ((buf[0] & 0xFF) == 0xFF)) {
            int marker = buf[1] & 0xFF;

            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }

            // Check if the marker is SOI or TEM.
            if ((marker == 0xD8) || (marker == 0x01)) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if ((marker == 0xD9) || (marker == 0xDA)) {
                return 0;
            }

            // Get the length and check if it is reasonable.
            if (!read(is, buf, 2)) {
                return 0;
            }
            length = pack(buf, 0, 2, false);
            if (length < 2) {
                MyLog.e(TAG, "getOrientation Invalid length");
                return 0;
            }
            length -= 2;

            // Break if the marker is EXIF in APP1.
            if ((marker == 0xE1) && (length >= 6)) {
                if (!read(is, buf, 6)) {
                    return 0;
                }
                length -= 6;
                if ((pack(buf, 0, 4, false) == 0x45786966)
                        && (pack(buf, 4, 2, false) == 0)) {
                    break;
                }
            }

            // Skip other markers.
            try {
                long skipLength = is.skip(length);
                if (skipLength <= 0) {
                    MyLog.d(TAG, "getOrientation skip maybe EOF is reached");
                }
            } catch (IOException ex) {
                return 0;
            }
            length = 0;
        }

        // JEITA CP-3451 Exif Version 2.2
        if (length > 8) {
            int offset = 0;
            byte[] jpeg = new byte[length];
            if (!read(is, jpeg, length)) {
                return 0;
            }

            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if ((tag != 0x49492A00) && (tag != 0x4D4D002A)) {
                MyLog.e(TAG, "getOrientation Invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);

            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if ((count < 10) || (count > length)) {
                MyLog.e(TAG, "getOrientation Invalid offset");
                return 0;
            }
            offset += count;
            length -= count;

            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while ((count-- > 0) && (length >= 12)) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    // We do not really care about type and count, do we?
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                    }
                    MyLog.d(TAG, "getOrientation Unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }

        MyLog.d(TAG, "getOrientation orientation not found");
        return 0;
    }

    public static boolean isSupportedExif(String mimeType) {
        return (mimeType != null)
                && (MimeTypeUtils.isJpeg(mimeType)
                || MimeTypeUtils.isHeifOrHeic(mimeType)
                || MimeTypeUtils.isPng(mimeType)
                || MimeTypeUtils.isRaw(mimeType));
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }

        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }

    private static boolean read(InputStream is, byte[] buf, int length) {
        try {
            return is.read(buf, 0, length) == length;
        } catch (IOException ex) {
            return false;
        }
    }

    private static SimpleDateFormat getDateFormat() {
        return FORMAT_THREAD_LOCAL.get();
    }

    private static List<SimpleDateFormat> getDateFormat2() {
        return FORMAT_THREAD_LOCAL_2.get();
    }

    private static SimpleDateFormat getDateFormatTz() {
        return FORMAT_TZ_THREAD_LOCAL.get();
    }

//    public static void copyExif(String source, String destination, int newWidth, int newHeight) {
//        MyLog.v(TAG, "copyExif");
//        try {
//            ExifInterface oldExif = new ExifInterface(source);
//            ExifInterface newExif = new ExifInterface(destination);
//
//            newExif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(newWidth));
//            newExif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, String.valueOf(newHeight));
//            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(0));
//
//            for (String tag : EXIF_TAGS) {
//                String value = oldExif.getAttribute(tag);
//                if (value != null) {
//                    newExif.setAttribute(tag, value);
//                }
//            }
//
//            // Handle some special values here
//            String value = oldExif.getAttribute(ExifInterface.TAG_F_NUMBER);
//            if (value != null) {
//                try {
//                    float aperture = Float.parseFloat(value);
//                    float offset = 0.5f;
//                    newExif.setAttribute(ExifInterface.TAG_F_NUMBER,
//                            String.valueOf((int) (aperture * Number.NUMBER_10 + offset)) + "/10");
//                } catch (NumberFormatException e) {
//                    MyLog.w(TAG, "cannot parse aperture: " + value);
//                }
//            }
//            newExif.saveAttributes();
//        } catch (IOException exception) {
//            MyLog.w(TAG, "cannot copy exif: " + source, exception);
//        }
//    }

    /**
     * 写入exif方向。
     * 使用oplusExifInterface写exif信息，是因为直接使用原生ExifInterface写exif信息,会导致文件大小发生变化，
     * 从而产生如下场景问题：
     * 场景：
     * 1.相册无照片-设置开启云服务-打开优化空间-下载缩图后，进入大图-旋转-下载原图-回到设置-关闭再打开云服务-
     * 图片被重命名，再次设置关闭再打开云服务，会重新下载一张原和上传重命名的图片
     * 结果产生两张一模一样的图片
     * 2.相册无照片-开启云服务-下载原图后-点击进入大图-旋转方向-回到设置页关闭再打开云服务-回到时间轴：查看大图，照片已被被重命名
     * --再次进入设置关闭再开启云服务，被重命名的照片会被上传到云端，且会下载原来的照片
     * 结果：产生两张一模一样的照片。
     * 以上产生原因：
     * 通过原生ExifInterface写入信息会使原来的照片大小发生变化，而云同步检测数据变化是通过判断文件大小是否发生变化来确定的，
     * 当照片大小发生变化时，会认为是一张新照片，会对其重命名。
     * <p>
     * 原生ExifInterface:存储宽、高各使用4个字节存储，此外TAG是LightSource占用4个字节，而内置的无这个TAG。
     * 内置的ExifInterface存储宽、高各使用2个字节存储，此外TAG InteropIndex InteropVersion 这两个TAG各占用3个自己
     * 其它这两个是一样的。
     *
     * @param filePath 文件绝对路径
     * @param degree   旋转角度：比如 90、180 270
     * @return
     */
//    public static boolean writeExifOrientation(String filePath, int degree) {
//        if (TextUtils.isEmpty(filePath)) {
//            MyLog.w(TAG, "writeExifOrientation, path is null");
//            return false;
//        }
//        OplusExifInterface exifInterface = new OplusExifInterface();
//        OplusExifTag tag = exifInterface.buildTag(
//                OplusExifInterface.TAG_ORIENTATION,
//                OplusExifInterface.getOrientationValueForRotation(degree)
//        );
//        boolean result = false;
//        if (tag != null) {
//            exifInterface.setTag(tag);
//            try {
//                exifInterface.forceRewriteExif(filePath);
//                result = true;
//            } catch (FileNotFoundException e) {
//                MyLog.e(TAG, "writeExifOrientation, cannot find file to set exif ", e);
//            } catch (IOException e) {
//                MyLog.e(TAG, "writeExifOrientation, IOException cannot set exif ", e);
//                result = saveRotateByAndroidExif(filePath, degree);
//            } catch (Exception t) {
//                // 非法文件路径,new File("/xxxx")时会抛空指针异常
//                MyLog.e(TAG, "writeExifOrientation, err cannot set exif ", t);
//                result = saveRotateByAndroidExif(filePath, degree);
//            }
//        }
//        return result;
//    }

//    /**
//     * 将Orientation信息写入文件的Exif部分。
//     * 该方法存在的意义，详见{@link #writeExifOrientation(String, int)} 的注释。
//     * @param fileDescriptor
//     * @param degree
//     * @return
//     */
//    public static boolean writeExifOrientation(@NonNull FileDescriptor fileDescriptor, int degree) {
//        if (fileDescriptor == null) {
//            MyLog.e(TAG, "Fd is null when write exif orientation.");
//            return false;
//        }
//        OplusExifInterface exifInterface = new OplusExifInterface();
//        OplusExifTag tag = exifInterface.buildTag(
//                OplusExifInterface.TAG_ORIENTATION,
//                OplusExifInterface.getOrientationValueForRotation(degree)
//        );
//        boolean result = false;
//        if (tag != null) {
//            exifInterface.setTag(tag);
//            try {
//                result = exifInterface.forceRewriteExif(fileDescriptor);
//            } catch (IOException e) {
//                MyLog.e(TAG, "writeExifOrientation, IOException cannot set exif ", e);
//            } catch (ErrnoException e) {
//                MyLog.e(TAG, "writeExifOrientation, ErrnoException cannot set exif ", e);
//            } catch (Exception e) {
//                MyLog.e(TAG, "writeExifOrientation, err cannot set exif ", e);
//            }
//        }
//        return result;
//    }
    @VisibleForTesting
    public static boolean saveRotateByAndroidExif(String filePath, int degree) {
        if (TextUtils.isEmpty(filePath)) {
            MyLog.w(TAG, "saveRotateByAndroidExif, path is null");
            return false;
        }
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                MyLog.w(TAG, "saveRotateByAndroidExif, file not exist!!!");
                return false;
            }
            long size = f.length();
            if (size <= FILE_SIZE_MAX) {
                ExifInterface exif = new ExifInterface(filePath);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, AppConstants.Degree.degreeToOrientation(degree));
                exif.saveAttributes();
                return true;
            } else {
                MyLog.w(TAG, "saveRotateByAndroidExif, Android-File is too large to write Exif!");
            }
        } catch (FileNotFoundException e) {
            MyLog.e(TAG, "saveRotateByAndroidExif, cannot find file to set exif ", e);
        } catch (IOException ex) {
            MyLog.e(TAG, "saveRotateByAndroidExif, Android-IOException cannot set exif ", ex);
        } catch (Exception t) {
            // 防止非法文件路径,会抛空指针异常
            MyLog.e(TAG, "saveRotateByAndroidExif, err cannot set exif ", t);
        }
        return false;
    }

    public static ExifEntry readExif(FileDescriptor fd) {
        if (fd == null) {
            MyLog.e(TAG, "readExif fd null");
            return null;
        }
        if (!fd.valid()) {
            MyLog.e(TAG, "readExif fd is invalid");
            return null;
        }
        try {
            return readExif(new ExifInterface(fd));
        } catch (Exception e) {
            MyLog.e(TAG, "readExif fd error:", e);
        } finally {
            try {
                Os.lseek(fd, 0, OsConstants.SEEK_SET);
            } catch (ErrnoException e) {
                MyLog.e(TAG, "readExif lseek fd error:", e);
            }
        }
        return null;
    }

    /**
     * 从文件的exif获取指定的tag
     *
     * @param filePath 文件路径
     * @param tag      ExifInterface内定义的TAG
     * @return exif指定tag的信息
     */
    public static String readExif(String filePath, String tag) {
        if (TextUtils.isEmpty(filePath)) {
            MyLog.e(TAG, "readExif filePath is empty or null");
            return null;
        }
        try {
            return new ExifInterface(filePath).getAttribute(tag);
        } catch (IOException e) {
            MyLog.e(TAG, "readExif filePath error:", e);
        }
        return null;
    }

    /**
     * 通过filePath去读取exif,沙盒模式下，只能读取私有目录的文件
     *
     * @param filePath 文件路径
     * @return ExifEntry exif信息
     */
    public static ExifEntry readExif(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            MyLog.e(TAG, "readExif filePath is empty or null");
            return null;
        }
        try {
            return readExif(new ExifInterface(filePath));
        } catch (IOException e) {
            MyLog.e(TAG, "readExif filePath error:", e);
        }
        return null;
    }

    @VisibleForTesting
    public static ExifEntry readExif(ExifInterface exifInterface) {
        ExifEntry exifData = new ExifEntry();
        exifData.mOrientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        exifData.mDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        exifData.mOriginDateTime = exifInterface.getAttribute(TAG_DATETIME_ORIGINAL);
        exifData.mMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
        exifData.mModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
        exifData.mFlash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
        exifData.mImageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        exifData.mImageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        exifData.mLatitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        exifData.mLongitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        exifData.mLatitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        exifData.mLongitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        exifData.mExposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        exifData.mFNumber = exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER);
        exifData.mIsoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
        exifData.mDateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
        exifData.mSubSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
        exifData.mSubSecTimeOriginal = exifInterface.getAttribute(TAG_SUBSEC_TIME_ORIGINAL);
        exifData.mSubSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED);
        exifData.mAltitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
        exifData.mAltitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
        exifData.mGpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
        exifData.mGpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        exifData.mWhiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
        exifData.mFocalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        exifData.mProcessingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
        exifData.mUserComment = exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT);
        exifData.mUserCommentByteArray = exifInterface.getAttributeBytes(ExifInterface.TAG_USER_COMMENT);
        exifData.mOffsetTimeOriginal = exifInterface.getAttribute(TAG_OFFSET_TIME_ORIGINAL);
        exifData.mOffsetTime = exifInterface.getAttribute(TAG_OFFSET_TIME);

        return exifData;
    }

    public static void writeExif(FileDescriptor fd, ExifEntry exifData) {
        if (fd == null) {
            MyLog.e(TAG, "writeExif fd null");
            return;
        }
        if (!fd.valid()) {
            MyLog.e(TAG, "writeExif fd is invalid");
            return;
        }
        try {
            writeExif(new ExifInterface(fd), exifData);
        } catch (Exception e) {
            MyLog.e(TAG, "writeExif fd error:", e);
        } finally {
            try {
                Os.lseek(fd, 0, OsConstants.SEEK_SET);
            } catch (ErrnoException e) {
                MyLog.e(TAG, "writeExif lseek fd error:", e);
            }
        }
    }

    /**
     * 将exifData写入fillPath文件，沙盒模式下仅能写入应用自有目录下的文件
     *
     * @param filePath 文件路径
     * @param exifData exif信息
     */
    public static void writeExif(String filePath, ExifEntry exifData) {
        try {
            writeExif(new ExifInterface(filePath), exifData);
        } catch (IOException e) {
            MyLog.e(TAG, "writeExif fd error:", e);
        }
    }

    private static void writeExif(ExifInterface exifInterface, ExifEntry exifData) throws IOException {
        exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, exifData.mOrientation);
        exifInterface.setAttribute(ExifInterface.TAG_DATETIME, exifData.mDateTime);
        exifInterface.setAttribute(TAG_DATETIME_ORIGINAL, exifData.mOriginDateTime);
        exifInterface.setAttribute(ExifInterface.TAG_MAKE, exifData.mMake);
        exifInterface.setAttribute(ExifInterface.TAG_MODEL, exifData.mModel);
        exifInterface.setAttribute(ExifInterface.TAG_FLASH, exifData.mFlash);
        exifInterface.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, exifData.mImageLength);
        exifInterface.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, exifData.mImageWidth);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, exifData.mLatitude);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, exifData.mLongitude);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, exifData.mLatitudeRef);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, exifData.mLongitudeRef);
        exifInterface.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, exifData.mExposureTime);
        exifInterface.setAttribute(ExifInterface.TAG_F_NUMBER, exifData.mFNumber);
        exifInterface.setAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS, exifData.mIsoSpeedRatings);
        exifInterface.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, exifData.mDateTimeDigitized);
        exifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME, exifData.mSubSecTime);
        exifInterface.setAttribute(TAG_SUBSEC_TIME_ORIGINAL, exifData.mSubSecTimeOriginal);
        exifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, exifData.mSubSecTimeDig);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, exifData.mAltitude);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, exifData.mAltitudeRef);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, exifData.mGpsTimeStamp);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, exifData.mGpsDateStamp);
        exifInterface.setAttribute(ExifInterface.TAG_WHITE_BALANCE, exifData.mWhiteBalance);
        exifInterface.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, exifData.mFocalLength);
        exifInterface.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD, exifData.mProcessingMethod);
        exifInterface.setAttribute(ExifInterface.TAG_USER_COMMENT, exifData.mUserComment);
        exifInterface.setAttribute(TAG_OFFSET_TIME_ORIGINAL, exifData.mOffsetTimeOriginal);
        exifInterface.setAttribute(TAG_OFFSET_TIME, exifData.mOffsetTime);
        exifInterface.saveAttributes();
    }

    public static final class ExifEntry {
        private String mOrientation;
        private String mDateTime;
        private String mOriginDateTime;
        private String mMake;
        private String mModel;
        private String mFlash;
        private String mImageLength;
        private String mImageWidth;
        private String mLatitude;
        private String mLongitude;
        private String mLatitudeRef;
        private String mLongitudeRef;
        private String mExposureTime;
        private String mFNumber;
        private String mIsoSpeedRatings;
        private String mDateTimeDigitized;
        private String mSubSecTime;
        private String mSubSecTimeOriginal;
        private String mSubSecTimeDig;
        private String mAltitude;
        private String mAltitudeRef;
        private String mGpsTimeStamp;
        private String mGpsDateStamp;
        private String mWhiteBalance;
        private String mFocalLength;
        private String mProcessingMethod;
        private String mUserComment;
        private String mOffsetTimeOriginal;
        private String mOffsetTime;
        /**
         * 某些图片直接使用原生接口exifInterface.getAttribute(ExifInterface.TAG_USER_COMMENT)
         * 读取user_comment会丢失部分信息。如：
         * 瘦身图原图GID保存在eixf的user_comment中，格式为
         * oplus_32/0{"thumb_globalid":"f7ae4906ddcf446db9329453dcef5ebc"}"
         * GID保存在tagflags之后，并用'\0'隔开，但是'\0'在原生exifInterface中是作为结束符（US_ASCII），
         * 使用原生接口去读取user_comment会丢失原图的GID，导致瘦身图认不出来。
         * 这里直接读取exif中user_comment的byte[]，业务可根据对应的编码格式转换成String
         */
        private byte[] mUserCommentByteArray;

        public void setOrientation(String orientation) {
            this.mOrientation = orientation;
        }

        public void setImageLength(String imageLength) {
            this.mImageLength = imageLength;
        }

        public void setImageWidth(String imageWidth) {
            this.mImageWidth = imageWidth;
        }

        public String getOrientation() {
            return mOrientation;
        }

        public String getImageLength() {
            return mImageLength;
        }

        public String getImageWidth() {
            return mImageWidth;
        }

        public byte[] getUserCommentByteArray() {
            return mUserCommentByteArray;
        }

        public void setUserComment(String userComment) {
            mUserComment = userComment;
        }

        public void setFNumber(String fNumber) {
            mFNumber = fNumber;
        }
    }

}

