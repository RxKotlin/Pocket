package com.kotlinchina.smallpockets.view.impl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.kotlinchina.smallpockets.BuildConfig
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.impl.VolleyHttpService
import com.kotlinchina.smallpockets.view.IMainView


class MainActivity : AppCompatActivity(), IMainView {

    companion object {
        val SAVE_TAGS = 1000
    }

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var mainPresenter: IMainPresenter? = null

    var listview: ListView? = null

    val datas: MutableList<Link>? = null

    var adapter: ShowSiteListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mainPresenter = MainPresenter(this, applicationContext, VolleyHttpService(this))

        initView()
        setOnclickListener()
    }

    private fun checkURL() {
        val resultString = getClipBoardData()
        this.mainPresenter?.checkClipBoardValidation(resultString)
    }

    private fun setOnclickListener() {
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@MainActivity,"show detail"+ ": position" +l, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        listview = this.findViewById(R.id.listView) as ListView

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

        if (BuildConfig.DEBUG) {
            return "http://www.baidu.com"
        } else {
            return resultString
        }
    }

    override fun showDialog(link: String) {
        Log.e(CLIPBOARD_TAG, link)

        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("需要保存此链接么？")
        dialog.setMessage(link)
        dialog.setPositiveButton("OK", { dialogInterface, i ->
            mainPresenter?.getTitleWithURL(link)
        })
        dialog.setNegativeButton("Cancel", { dialogInterface, i ->
            Log.d(CLIPBOARD_TAG, "Cancel")
            mainPresenter?.refreshList()
        })
        dialog.show()
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }

    override fun setSiteListData(data: List<Link>) {
        datas?.clear()
        adapter = ShowSiteListAdapter(this, R.layout.show_site_list_item, data)
        listview?.adapter = adapter
    }

    override fun showSaveScreenWithTitle(title: String, url: String) {
        val intent = Intent(this@MainActivity, SaveTagActivity::class.java)
        intent.putExtra(SaveTagActivity.TITLE, title)
        intent.putExtra(SaveTagActivity.URL, url)
        startActivityForResult(intent, SAVE_TAGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun saveData() {
            if (resultCode == RESULT_OK) {
                when (requestCode) {
                    SAVE_TAGS -> {
                        val title = data?.getStringExtra(SaveTagActivity.TITLE)
                        val url = data?.getStringExtra(SaveTagActivity.URL)
                        val tags = data?.getStringArrayExtra(SaveTagActivity.TAGS)
                        if (title != null
                                && url != null
                                && tags != null) {
                            mainPresenter?.saveToDB(title, url, tags.asList())
                        }
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        saveData()
    }

    override fun onStart() {
        super.onStart()
        checkURL()
    }
}
