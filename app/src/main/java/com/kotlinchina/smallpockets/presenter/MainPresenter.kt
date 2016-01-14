package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainPresenter(mainView: IMainView): IMainPresenter {

    var mainView: IMainView

    init {
        this.mainView = mainView
    }

    override fun checkClipBoardValidation(clipboardString: String) {
        val url = try { URL(clipboardString) } catch (e: MalformedURLException) {
            this.mainView.showNoLinkWithMsg("Invalid String")
            return
        }

        this.mainView.showLink("Valid: ${url.toString()}")
    }

    override fun loadSiteListData() {
        val data = ArrayList<HashMap<String, Any>>()

        val cur = HashMap<String, Any>()
        cur.put("name", "Android技术相关")
        val tags = ArrayList<String>()
        tags.add("自定义View")
        tags.add("IT")
        tags.add("图形界面")
        cur.put("tags", tags)

        data.add(cur)


        val cur1 = HashMap<String, Any>()
        cur1.put("name", "美女图片")
        val tags2 = ArrayList<String>()
        tags2.add("美女")
        tags2.add("图片")
        tags2.add("中国第一裸模")
        tags2.add("美丽")
        cur1.put("tags", tags2)

        data.add(cur1)


        val cur3 = HashMap<String, Any>()
        cur3.put("name", "汽车之家")
        val tag3 = ArrayList<String>()
        tag3.add("新款")
        tag3.add("兰坡基尼")
        tag3.add("最爱")
        cur3.put("tags", tag3)

        data.add(cur3)


        val cur4 = HashMap<String, Any>()
        cur4.put("name", "软件之家")
        val tags4 = ArrayList<String>()
        tags4.add("锤子阅读")
        tags4.add("掘金技术")
        tags4.add("唱吧")
        tags4.add("苏宁易购")
        tags4.add("今日头条")
        tags4.add("当当")
        tags4.add("微博")
        tags4.add("百词斩")
        tags4.add("九个头条")
        tags4.add("唯品会")
        tags4.add("是男人就下100层")

        cur4.put("tags", tags4)

        data.add(cur4)

        this.mainView.setSiteListData(data)

    }
}
