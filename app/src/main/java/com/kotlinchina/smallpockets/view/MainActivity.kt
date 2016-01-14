package com.kotlinchina.smallpockets.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.MainPresenter
import java.util.*

class MainActivity : AppCompatActivity(), IMainView {

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var mainPresenter: IMainPresenter? = null

    var listview: ListView? = null

    val datas = ArrayList<HashMap<String, Any>>()

    var adapter:ShowSiteListAdapter<HashMap<String,Any>>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mainPresenter = MainPresenter(this)

        val resultString = getClipBoardData()
        this.mainPresenter?.checkClipBoardValidation(resultString)

        this.mainPresenter?.loadSiteListData()

        initView()

        initData()

        setOnclickListener()
    }

    private fun setOnclickListener() {
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@MainActivity,"show detail"+ ": position" +l,Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
//把加载数据放在p中去处理
//        val cur = HashMap<String, Any>()
//        cur.put("name", "Android技术相关")
//        val tags = ArrayList<String>()
//        tags.add("自定义View")
//        tags.add("IT")
//        tags.add("图形界面")
//        cur.put("tags", tags)
//
//        data.add(cur)
//
//
//        val cur1 = HashMap<String, Any>()
//        cur1.put("name", "美女图片")
//        val tags2 = ArrayList<String>()
//        tags2.add("美女")
//        tags2.add("图片")
//        tags2.add("中国第一裸模")
//        tags2.add("美丽")
//        cur1.put("tags", tags2)
//
//        data.add(cur1)
//
//
//        val cur3 = HashMap<String, Any>()
//        cur3.put("name", "汽车之家")
//        val tag3 = ArrayList<String>()
//        tag3.add("新款")
//        tag3.add("兰坡基尼")
//        tag3.add("最爱")
//        cur3.put("tags", tag3)
//
//        data.add(cur3)
//
//
//        val cur4 = HashMap<String, Any>()
//        cur4.put("name", "软件之家")
//        val tags4 = ArrayList<String>()
//        tags4.add("锤子阅读")
//        tags4.add("掘金技术")
//        tags4.add("唱吧")
//        tags4.add("苏宁易购")
//        tags4.add("今日头条")
//        tags4.add("当当")
//        tags4.add("微博")
//        tags4.add("百词斩")
//        tags4.add("九个头条")
//        tags4.add("唯品会")
//        tags4.add("是男人就下100层")
//
//        cur4.put("tags", tags4)
//
//        data.add(cur4)


        adapter = ShowSiteListAdapter(this,datas)

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

    override fun showLink(link: String) {
        Log.e(CLIPBOARD_TAG, link)
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }

    override fun setSiteListData(data: ArrayList<HashMap<String, Any>>) {
        datas.addAll(data)
    }
}
