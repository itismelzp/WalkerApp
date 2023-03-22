

package com.demo.utils;

import android.app.KeyguardManager;
import android.content.Context;

import com.demo.MyApplication;

public class KeyguardManagerUtils {
    private static final String TAG = "KeyguardManagerUtils";
    private static volatile KeyguardManager sKeyguardManager = null;

    /**
     * Operation associated with KeyguardManager
     */

    private static KeyguardManager getKeyguardManager() {
        if (sKeyguardManager == null) {
            synchronized (KeyguardManager.class) {
                if (sKeyguardManager == null) {
                    sKeyguardManager = (KeyguardManager) MyApplication.getInstance()
                            .getSystemService(Context.KEYGUARD_SERVICE);
                }
            }
        }
        return sKeyguardManager;
    }

    public static boolean isKeyguardLocked() {
        KeyguardManager keyguardManager = getKeyguardManager();
        if (keyguardManager != null) {
            return keyguardManager.isKeyguardLocked();
        }
        return true;
    }

}
