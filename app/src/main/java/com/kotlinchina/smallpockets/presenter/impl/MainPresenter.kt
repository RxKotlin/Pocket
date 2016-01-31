package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import cn.wanghaomiao.xpath.model.JXDocument
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.HttpService
import com.kotlinchina.smallpockets.view.IMainView
import io.realm.Realm
import io.realm.RealmConfiguration
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.arrayListOf
import kotlin.collections.hashMapOf

class MainPresenter(mainView: IMainView, context: Context, httpService: HttpService): IMainPresenter {

    var mainView: IMainView
    val context: Context
    val httpService: HttpService

    init {
        this.mainView = mainView
        this.context = context
        this.httpService = httpService
    }

    override fun checkClipBoardValidation(clipboardString: String) {
        val url = try {
            URL(clipboardString)
        } catch (e: MalformedURLException) {
            this.mainView.showNoLinkWithMsg("Invalid String")
            return
        }

        this.mainView.showDialog("${url.toString()}")
    }

    override fun loadSiteListData() {
        //相当于java中的lsitView添加HashMap，非常好用
        var data = arrayListOf(
                hashMapOf<String, Any>(
                        "name" to "Android技术相关",
                        "tags" to arrayListOf(
                                "自定义View",
                                "IT",
                                "图形界面"
                        )
                ),
                hashMapOf<String, Any>(
                        "name" to "美女图片",
                        "tags" to arrayListOf(
                                "美女",
                                "图片",
                                "中国第一裸模",
                                "苍老师",
                                "美丽"
                        )
                ),
                hashMapOf<String, Any>(
                        "name" to "汽车之家",
                        "tags" to arrayListOf(
                                "新款",
                                "兰坡基尼",
                                "最爱"
                        )
                ),
                hashMapOf<String, Any>(
                        "name" to "软件之家",
                        "tags" to arrayListOf(
                                "锤子阅读",
                                "掘金技术",
                                "唱吧",
                                "苏宁易购",
                                "今日头条",
                                "当当",
                                "微博",
                                "百词斩",
                                "九个头条",
                                "唯品会",
                                "是男人就下100层"
                        )
                )

        )
        this.mainView.setSiteListData(data)

    }

    override fun getTitleWithURL(url: String) {
        fun titleFromData(t: String): String? {
            val title = JXDocument(t).sel("//title/text()").first()
            return title as? String
        }

        httpService.fetchDataWithUrl(url)
                .map { t ->
                    titleFromData(t)
                }
                .subscribe { title ->
                    if (title != null) mainView.showSaveScreenWithTitle(title, url)
                }
    }

    override fun saveToDB(title: String, url: String, tags: Array<String>) {
        fun realmLinkWithLink(link: Link) {
            val config = RealmConfiguration.Builder(context).build()
            val realm = Realm.getInstance(config)
            realm.executeTransaction {
                val realmLink = realm.createObject(RealmLink::class.java)
                realmLink.title = link.title
                realmLink.url = link.url
                realmLink.createDate = link.createDate
                link.tags?.forEach { it ->
                    val realmTag = realm.createObject(RealmTag::class.java)
                    realmTag.name = it.name
                    realmLink.tags?.add(realmTag)
                }
                realm.copyFromRealm(realmLink)
            }
        }

        realmLinkWithLink(CoreLink(title, url, tags))
    }
}
