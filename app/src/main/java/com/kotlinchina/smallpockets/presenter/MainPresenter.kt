package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.arrayListOf
import kotlin.collections.hashMapOf

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
        //相当于java中的lsitView添加HashMap，非常好用
        var data = arrayListOf(
                hashMapOf<String,Any>(
                        "name" to "Android技术相关",
                        "tags" to arrayListOf(
                                "自定义View",
                                "IT",
                                "图形界面"
                        )
                ),
                hashMapOf<String,Any>(
                        "name" to "美女图片",
                        "tags" to arrayListOf(
                                "美女",
                                "图片",
                                "中国第一裸模",
                                "苍老师",
                                "美丽"
                        )
                ),
                hashMapOf<String,Any>(
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
}
