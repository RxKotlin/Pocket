package com.kotlinchina.smallpockets.presenter

interface ILinkListPresenter {
    fun checkClipboard()
    fun getTitleWithURL(url: String)
    fun saveToDB(title: String, url: String, tags: List<String>)
    fun refreshList()
}
