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
import android.widget.Switch
import android.widget.Toast
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.impl.VolleyHttpService
import com.kotlinchina.smallpockets.view.IMainView
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*


class MainActivity : AppCompatActivity(), IMainView {

    companion object {
        val SAVE_TAGS = 1000
    }

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var mainPresenter: IMainPresenter? = null

    var listview: ListView? = null

    val datas = ArrayList<HashMap<String, Any>>()

    var adapter: ShowSiteListAdapter<HashMap<String, Any>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mainPresenter = MainPresenter(this, this, VolleyHttpService(this))

        mainPresenter?.setupParse()

        checkURL()
        initView()
        initData()
        setOnclickListener()
    }

    private fun checkURL() {
        val resultString = getClipBoardData()
        this.mainPresenter?.checkClipBoardValidation(resultString)
        this.mainPresenter?.loadSiteListData()
    }

    private fun setOnclickListener() {
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@MainActivity,"show detail"+ ": position" +l, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        adapter = ShowSiteListAdapter(this, datas)
        listview?.adapter = adapter

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

        return resultString
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
        })
        dialog.show()
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }

    override fun setSiteListData(data: ArrayList<HashMap<String, Any>>) {
        datas.addAll(data)
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
                            mainPresenter?.saveToDB(title, url, tags)
                        }
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        saveData()
    }

    override fun onRestart() {
        super.onRestart()
        checkURL()
    }
}
