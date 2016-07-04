package com.kotlinchina.smallpockets.utils

import android.app.Activity
import android.content.Context

import java.util.Stack

@Suppress("UNUSED_VALUE")
/**
 * Created by junjun.
 */
class AppManager private constructor() {

    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack<Activity>()
        }
        activityStack?.add(activity)
    }

    fun currentActivity(): Activity? {
        val activity = activityStack?.lastElement()
        return activity as? Activity
    }

    fun finishActivity() {
        val activity = activityStack?.lastElement()
        Companion.finishActivity(activity)
    }

    fun finishActivity(cls: Class<*>) {
        if(activityStack!=null){
            activityStack?.forEach { activity: Activity ->
                if (activity?.javaClass == cls) {
                    Companion.finishActivity(activity)
                }
            }
        }

    }

    fun finishAllActivity() {
        var i = 0
        val size = activityStack?.size as Int
        while (i < size) {
            val tempActivityStack = activityStack as? Stack<Activity>
            if(tempActivityStack!=null && tempActivityStack?.get(i)!=null){
                    Companion?.finishActivity(tempActivityStack?.get(i))
                    break
                i++
            }

        }
        activityStack?.clear()
    }

    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
        }

    }

    companion object {
        private var activityStack: Stack<Activity>? = null
        private var instance: AppManager? = null
        val appManager: AppManager?
            get() {
                if (instance == null) {
                    synchronized (AppManager::class.java) {
                        if (null == instance) {
                            instance = AppManager()
                        }
                    }
                }
                return instance as? AppManager
            }

        fun getActivity(cls: Class<*>): Activity? {
            if (activityStack != null)
                activityStack?.forEach { activity:Activity ->
                    if (activity.javaClass == cls) {
                        return activity
                    }
                }
            return null
        }

        fun finishActivity(activity: Activity?) {
            val activity = activity
            val canRemove = activity?.isFinishing as? Boolean
            if (activity != null && canRemove!=null && !canRemove) {
                activityStack?.remove(activity)
                activity?.finish()
            }
        }
    }
}
