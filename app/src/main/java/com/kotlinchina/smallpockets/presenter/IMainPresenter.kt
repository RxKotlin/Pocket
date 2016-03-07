package com.kotlinchina.smallpockets.presenter

interface IMainPresenter {
    fun checkClipBoardValidation(clipboardString: String)
    fun getTitleWithURL(url: String)
    fun saveToDB(title: String, url: String, tags: Array<String>)
    fun refreshList()
}
