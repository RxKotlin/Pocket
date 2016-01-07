package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL

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
}
