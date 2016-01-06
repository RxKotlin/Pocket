package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.view.IMainView

class MainPresenter(mainView: IMainView): IMainPresenter {
    val VALID_HTTP_PREFIX: String = "http://"
    val VALID_HTTPS_PREFIX: String = "https://"
    val VALID_CLIPBOARD_STRING_MIN_LENGTH: Int = 7 // "http:// or https://

    var mainView: IMainView

    init {
        this.mainView = mainView
    }

    override fun checkClipBoardValidation(clipboardString: String) {
        if (clipboardString.length < VALID_CLIPBOARD_STRING_MIN_LENGTH) {
            this.mainView.showNoLinkWithMsg("Invalid String")
        }

        val prefixHTTP = clipboardString.subSequence(0, VALID_CLIPBOARD_STRING_MIN_LENGTH)
        if (prefixHTTP == VALID_HTTP_PREFIX) {
            this.mainView.showNoLinkWithMsg("Invalid String")
        }

        val prefixHTTPS = clipboardString.subSequence(0, VALID_CLIPBOARD_STRING_MIN_LENGTH + 1)
        if (prefixHTTPS == VALID_HTTPS_PREFIX) {
            this.mainView.showNoLinkWithMsg("Invalid String")
        }

        this.mainView.showLink("Valid: ${clipboardString}")
    }
}
