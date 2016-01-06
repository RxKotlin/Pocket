package com.kotlinchina.smallpockets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "There is no data in clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}
