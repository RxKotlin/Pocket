package com.kotlinchina.smallpockets.presenter

import java.util.*

interface IMainPresenter {
    fun checkClipboard()
    fun getTitleWithURL(url: String)
    fun saveToDB(title: String, url: String, tags: List<String>)
    fun refreshList()
    fun sycLinksOfCurrentWeekToCloud(today: Date)
}
