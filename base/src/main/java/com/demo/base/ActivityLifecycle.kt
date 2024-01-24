package com.demo.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * Created by lizhiping on 2023/6/17.
 * <p>
 * description
 */
object ActivityLifecycle : ActivityLifecycleCallbacks {

    private var activityCount: Int = 0;

    private val activities: MutableList<Activity> = mutableListOf()
    private val listeners: MutableList<LifecycleListener> = mutableListOf()

    @JvmStatic
    fun addListener(listener: LifecycleListener) {
        listeners.add(listener)
    }

    @JvmStatic
    fun removeListener(listener: LifecycleListener) {
        listeners.remove(listener)
    }

    @JvmStatic
    fun isRunningForeground(): Boolean {
        return activityCount > 0
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
        for (listener in listeners) {
            listener.onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        for (listener in listeners) {
            listener.onActivityStarted(activity)
            if (activityCount == 1) {
                listener.onForeground(activity)
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        for (listener in listeners) {
            listener.onActivityResumed(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        for (listener in listeners) {
            listener.onActivityPaused(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        for (listener in listeners) {
            listener.onActivityStopped(activity)
            if (activityCount == 0) {
                listener.onBackground(activity)
            }
        }
        activities.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        for (listener in listeners) {
            listener.onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        for (listener in listeners) {
            listener.onActivityDestroyed(activity)
        }
    }

    interface LifecycleListener {

        fun onForeground(activity: Activity)

        fun onBackground(activity: Activity)

        fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        fun onActivityStarted(activity: Activity) {}

        fun onActivityResumed(activity: Activity) {}

        fun onActivityPaused(activity: Activity) {}

        fun onActivityStopped(activity: Activity) {}

        fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        fun onActivityDestroyed(activity: Activity) {}
    }

}

