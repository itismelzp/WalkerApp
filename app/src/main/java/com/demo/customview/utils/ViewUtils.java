package com.demo.customview.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

public class ViewUtils {

    private static float DEVICE_DENSITY = 0;
    public static int densityDpi;
    public static float mDensity;
    public static Context mContext;
    
    public static void initContext(Context context) {
        mContext = context;
        init();
    }

    private static void init() {
        Context ctx = mContext;//mContext;
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        mDensity = dm.density;    /* FixMe: Android 7.0以后Target API为23以上时Display Size Change改变不会重启App，而是调用onConfigurationChanged，需要对mDensity重新赋值 */
        densityDpi = dm.densityDpi;
    }

    public static float getSpValue(float value) {
        if (DEVICE_DENSITY == 0) {
            DEVICE_DENSITY = densityDpi;//mContext.getResources().getDisplayMetrics().densityDpi;
        }
        return value * DEVICE_DENSITY / 160;
    }

    public static int dip2px(float dpValue) {
        return (int) (dpValue * mDensity + 0.5f);
    }

    public static boolean isChildOf(View c, View p) {
        if (c == p) {
            return true;
        } else if (p instanceof ViewGroup) {
            int count = ((ViewGroup) p).getChildCount();
            for (int i = 0; i < count; i++) {
                View ci = ((ViewGroup) p).getChildAt(i);
                if (isChildOf(c, ci)) {
                    return true;
                }
            }
        }
        return false;
    }



    static private float density = -1;
    static private float scaleDensity = -1;

    /**
     * add by mars
     */
    public static float getDensity() {
        if (density < 0) {
            density = mContext.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static float getScaleDensity() {
        if (scaleDensity < 0) {
            scaleDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        }
        return scaleDensity;
    }

    static private int densityDPI = -1;

    public static float getDensityDpi() {
        if (densityDPI < 0) {
            densityDPI = mContext.getResources().getDisplayMetrics().densityDpi;
        }
        return densityDPI;
    }

    static private int screenWidth = -1;
    static private int screenHeight = -1;

    /**
     * add by mars
     */
    public static int getScreenWidth() {
        if (screenWidth < 0) {
            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                screenWidth = mContext.getResources().getDisplayMetrics().heightPixels;
            } else {
                screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            }
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight < 0) {
            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                screenHeight = mContext.getResources().getDisplayMetrics().widthPixels;
            } else {
                screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
            }
        }
        return screenHeight;
    }

    public static void resetScreenSize() {
        screenWidth = -1;
        screenHeight = -1;
    }

    //重新获取新的屏幕高宽
    public static void setScreenSizeByConfig(Configuration configuration) {
        screenWidth = getScreenWidthByConfig(configuration);
        screenHeight = getScreenHeightByConfig(configuration);
    }

    private static int getScreenWidthByConfig(Configuration configuration) {
        int width = 0;
        if (configuration != null) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                width = dpToPx(configuration.screenHeightDp);
            } else {
                width = dpToPx(configuration.screenWidthDp);
            }
        }
        return width;
    }

    private static int getScreenHeightByConfig(Configuration configuration) {
        int height = 0;
        if (configuration != null) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                height = dpToPx(configuration.screenWidthDp);
            } else {
                height = dpToPx(configuration.screenHeightDp);
            }
        }
        return height;
    }


    static private double screenSizeCM = 0;
    static private int pixelPerCM = 0;

    // 获取屏幕尺寸
    public static double getScreenSizeCM() {
        if (screenSizeCM <= 0) {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
            screenSizeCM = diagonalPixels / getPixelPerCM();
        }
        return screenSizeCM;
    }

    public static int getPixelPerCM() {
        if (pixelPerCM <= 0) {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            pixelPerCM = (int) (dm.xdpi / 2.54);
        }
        return pixelPerCM;
    }


    /*
     * android 2.3机器setEnable后，child没有效果，改用这个高级的来做
     *view层级不会太多时使用，此处采用递归处理
     */
    public static void setEnableForAllChild(View view, boolean enabled) {
        if (view == null) {
            return;
        }

        view.setEnabled(enabled);
        if (!(view instanceof ViewGroup)) {
            return;
        }

        ViewGroup root = (ViewGroup) view;
        int count = root.getChildCount();
        for (int i = 0; i < count; i++) { // 递归所有的子view
            setEnableForAllChild(root.getChildAt(i), enabled);
        }
    }

    public static int dpToPx(float dp) {
        return Math.round(dp * getDensity());
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int PxToDp(float px) {
        return Math.round(px / getDensity());
    }

    public static int spToPx(float sp) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5F);
    }

    public static int pxTosp(float px) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5F);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void changeTransparency(View view, int transparency) {
        if (view.getBackground() != null) {
            view.getBackground().setAlpha(transparency);
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                changeTransparency(vg.getChildAt(i), transparency);
            }
        }
    }

    public static boolean setViewVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
            return true;
        }
        return false;
    }

    public static void setVisible(View v, int visible) {
        if (v != null && v.getVisibility() != visible) {
            v.setVisibility(visible);
        }
    }

    public static Drawable getShapeDrawable(@ColorInt int solidColor, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(solidColor);
        return drawable;
    }

}