package com.kotlinchina.smallpockets.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import com.kotlinchina.smallpockets.R

/**
 * Created by junjun.
 */
class DoubClickExitHelper(private val mActivity: Activity) {

    private var isOnKeyBacking: Boolean? = false
    private var mHandler: Handler? = null
    private var mBackToast: Toast? = null

    init {
        mHandler = Handler(Looper.getMainLooper())
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false
        }
        var isBack = isOnKeyBacking as? Boolean
        if (isBack!=null && isBack) {
            mHandler?.removeCallbacks(onBackTimeRunnable)
            if (mBackToast != null) {
                mBackToast?.cancel()
            }
            // 退出
            AppManager.appManager?.AppExit(mActivity)
            return true
        } else {
            isOnKeyBacking = true
            if (mBackToast == null) {
                mBackToast = Toast.makeText(mActivity, R.string.double_click_exit, 2000)
            }
            mBackToast?.show()
            mHandler?.postDelayed(onBackTimeRunnable, 2000)
            return true
        }
    }

    private val onBackTimeRunnable = Runnable {
        isOnKeyBacking = false
        if (mBackToast != null) {
            mBackToast?.cancel()
        }
    }
}
