package com.demo.ipc;

import static android.os.Process.myPid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.base.log.MyLog;

import java.lang.reflect.Method;
import java.util.List;

/**
 * description
 * <p>
 * Created by walkerzpli on 2022/2/20.
 */
public class ProcessUtil {

    private static final String TAG = "ProcessUtil";

    private static final String PACKAGE_NAME = "com.demo";
    private static final String SUB_PROCESS_SUFFIX = ":sub";
    private static final String SUB_PROCESS_NAME = PACKAGE_NAME + SUB_PROCESS_SUFFIX;
    private static final String SUB_PROCESS_SERVICE = "com.demo.ipc.SubPreLoadService";

    private static String currentProcessName;

    public static String getCurProcessLog() {
        return "Current process: " + getCurrentProcessNameByApplication();
    }

    /**
     * @return 当前进程名
     */
    @Nullable
    public static String getCurrentProcessName(@NonNull Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //2)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //3)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager(context);

        return currentProcessName;
    }


    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    public static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            final Method declaredMethod = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader())
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    public static String getCurrentProcessNameByActivityManager(@NonNull Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();
            if (runningAppList != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppList) {
                    if (processInfo.pid == pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
    }

    public static void killMyProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean isProcessByName(Context context, String processName) {
        String curProcessName = "";
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == myPid()) {
                curProcessName = processInfo.processName;
                break;
            }
        }

        return curProcessName.endsWith(processName);
    }

    public static boolean isSubProcessAlive(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                String processName = processInfo.processName;
                if (SUB_PROCESS_NAME.equals(processName)) {
                    MyLog.d(TAG, "[isSubProcessAlive] isSubProcessAlive == true");
                    return true;
                }
            }
        } catch (Exception e) {
            MyLog.e(TAG, "get process info fail.", e);
        }
        MyLog.d(TAG, "[isSubProcessAlive] isSubProcessAlive == false");
        return false;
    }

    public static void loadSubProcessService(Context context) {
        Intent preLoaderIntent = new Intent();
        preLoaderIntent.setAction(SUB_PROCESS_SERVICE);
        preLoaderIntent.setPackage(PACKAGE_NAME);
        try {
            context.startService(preLoaderIntent);
        } catch (Exception e) {
            MyLog.e(TAG, "[loadSubProcessService] loadSubProcess failed.", e);
        }
        MyLog.d(TAG, "[loadSubProcessService] loadSubProcess success.");
    }

    public static void stopSubProcessService(Context context) {
        Intent preLoader = new Intent();
        preLoader.setAction(SUB_PROCESS_SERVICE);
        preLoader.setPackage(PACKAGE_NAME);
        context.stopService(preLoader);
    }

}
