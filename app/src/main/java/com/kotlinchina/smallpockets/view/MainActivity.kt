package com.kotlinchina.smallpockets.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.MainPresenter
import com.kotlinchina.smallpockets.view.IMainView

class MainActivity : AppCompatActivity(), IMainView {

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var mainPresenter: IMainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mainPresenter = MainPresenter(this)

        val resultString = getClipBoardData()
        this.mainPresenter?.checkClipBoardValidation(resultString)
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

    override fun showLink(link: String) {
        Log.e(CLIPBOARD_TAG, link)
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }
}
