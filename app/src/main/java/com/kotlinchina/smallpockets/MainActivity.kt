package com.kotlinchina.smallpockets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val VALID_HTTP_PREFIX: String = "http://"
    val VALID_HTTPS_PREFIX: String = "https://"
    val VALID_CLIPBOARD_STRING_MIN_LENGTH: Int = 7 // "http:// or https://
    val CLIPBOARD_TAG: String = "CLIPBOARD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultString: String = getClipBoardData()
        if (checkClipBoardValidation(resultString)) {
            Log.e(CLIPBOARD_TAG, "Valid String")
        } else {
            Log.e(CLIPBOARD_TAG, "Invalid String")
        }
    }

    private fun getClipBoardData(): String {
        val clipboardManager: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var resultString: String = ""

        if (clipboardManager.hasPrimaryClip()) {
            val clipData: ClipData = clipboardManager.primaryClip
            val clipDataCount = clipData.itemCount
            for (index in 0..clipDataCount - 1) {
                val item = clipData.getItemAt(index)
                val str = item.coerceToText(this)
                resultString += str
            }
        }

        return resultString
    }

    private fun checkClipBoardValidation(clipboardString: String):Boolean {
        if (clipboardString.length < VALID_CLIPBOARD_STRING_MIN_LENGTH) {
            return false
        }

        val prefixHTTP = clipboardString.subSequence(0, VALID_CLIPBOARD_STRING_MIN_LENGTH)
        if (prefixHTTP == VALID_HTTP_PREFIX) {
            Log.e(CLIPBOARD_TAG, prefixHTTP)
            return true
        }

        val prefixHTTPS = clipboardString.subSequence(0, VALID_CLIPBOARD_STRING_MIN_LENGTH + 1)
        if (prefixHTTPS == VALID_HTTPS_PREFIX) {
            Log.e(CLIPBOARD_TAG, prefixHTTPS)
            return true
        }

        return false
    }
}
