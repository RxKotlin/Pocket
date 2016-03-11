package com.kotlinchina.smallpockets.presenter

interface IMainPresenter {
    fun checkClipboard()
    fun getTitleWithURL(url: String)
    fun saveToDB(title: String, url: String, tags: List<String>)
    fun refreshList()
    fun saveLinkToCloud(title: String, conent: String)
}
