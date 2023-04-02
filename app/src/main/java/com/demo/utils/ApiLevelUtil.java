package com.demo.utils;

import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

/**
 * Device-side compatibility utility class for reading device API level.
 */
public final class ApiLevelUtil {

    private ApiLevelUtil() {
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isAndroidS() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.S;
    }

    public static boolean isAndroidR() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.R;
    }

    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.Q;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    public static boolean isAtLeastAndroidT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    public static boolean isAtLeastAndroidS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    public static boolean isAtLeastAndroidR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public static boolean isAtMostAndroidS() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.S;
    }

    public static boolean isAtMostAndroidR() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.R;
    }
}
