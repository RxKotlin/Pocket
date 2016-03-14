package com.kotlinchina.smallpockets.presenter

import rx.Subscription

interface IMainPresenter {
    fun checkClipboard()
    fun getTitleWithURL(url: String): Subscription
    fun saveToDB(title: String, url: String, tags: List<String>)
    fun refreshList()
    fun saveLinkToCloud(title: String, conent: String)
}
