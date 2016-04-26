package com.kotlinchina.smallpockets.presenter.impl

import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.ILinkListPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.view.ILinkListView
import java.net.MalformedURLException
import java.net.URL

class LinkListPresenter(linkListView: ILinkListView, httpService: HttpService,
                        clipboardService: ClipboardService,
                        dataBaseStore: IDataBaseStore): ILinkListPresenter {

    var linkListView: ILinkListView
    val httpService: HttpService
    val clipboardService: ClipboardService
    val dataBaseStore: IDataBaseStore

    init {
        this.linkListView = linkListView
        this.httpService = httpService
        this.clipboardService = clipboardService
        this.dataBaseStore = dataBaseStore
    }

    override fun getTitleWithURL(url: String) {
          httpService.fetchDataWithUrl(url)
                .subscribe { title ->
                    if (title != null) linkListView.showSaveScreenWithTitle(title, url)
                }
    }

    override fun saveToDB(title: String, url: String, tags: List<String>) {
        dataBaseStore.saveUrlInfoWithLink(CoreLink(title, url, tags))
        this.linkListView.setSiteListData(dataBaseStore.loadData())
    }

    override fun refreshList() {
        this.linkListView.setSiteListData(dataBaseStore.loadData())
    }


    override fun checkClipboard() {
        fun checkClipBoardValidation(clipboardString: String) {
            val url = try {
                URL(clipboardString)
            } catch (e: MalformedURLException) {
                this.linkListView.showNoLinkWithMsg("Invalid String")
                return
            }

            this.linkListView.showDialog("${url.toString()}")
        }
        checkClipBoardValidation(clipboardService.content())
    }
}
