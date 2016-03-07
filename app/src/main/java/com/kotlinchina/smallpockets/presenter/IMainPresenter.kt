package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.model.Tag

interface IMainPresenter {
    fun checkClipBoardValidation(clipboardString: String)
    fun getTitleWithURL(url: String)
    fun saveToDB(title: String, url: String, tags: List<String>)
    fun refreshList()
}
