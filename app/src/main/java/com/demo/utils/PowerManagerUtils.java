
package com.demo.utils;

import android.content.Context;
import android.os.PowerManager;

import com.demo.MyApplication;


public class PowerManagerUtils {
    private static final String TAG = "PowerManagerUtils";
    private static final long STAGE_PROTECTS_TIMEOUT = 1000L;
    private static volatile PowerManager sPowerManager = null;

    /**
     * Operation associated with PowerManager
     */
    private static PowerManager getPowerManager() {
        if (sPowerManager == null) {
            synchronized (PowerManager.class) {
                if (sPowerManager == null) {
                    sPowerManager = (PowerManager) MyApplication.getInstance()
                            .getSystemService(Context.POWER_SERVICE);
                }
            }
        }
        return sPowerManager;
    }

    public static boolean isInteractive() {
        PowerManager powerManager = getPowerManager();
        if (powerManager != null) {
            return powerManager.isInteractive();
        }
        return true;
    }

    public static boolean isPowerSaveMode() {
        PowerManager powerManager = getPowerManager();
        if (powerManager != null) {
            return powerManager.isPowerSaveMode();
        }
        return false;
    }
}
