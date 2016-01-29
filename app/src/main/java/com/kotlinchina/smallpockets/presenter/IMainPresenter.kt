package com.kotlinchina.smallpockets.presenter

interface IMainPresenter {
    fun checkClipBoardValidation(clipboardString: String)
    fun loadSiteListData()
    fun getTitleWithURL(url: String)
    fun setupParse()
}
