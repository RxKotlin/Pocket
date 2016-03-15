package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL

class MainPresenter(mainView: IMainView, context: Context, httpService: HttpService, storeService: StoreService, clipboardService: ClipboardService,iparseDom: IParseDom,iSaveUrlInfo: ISaveUrlInfo): IMainPresenter {

    var mainView: IMainView
    val context: Context
    val httpService: HttpService
    val storeService: StoreService
    val clipboardService: ClipboardService
    val iparseDom: IParseDom
    val iSaveUrlInfo: ISaveUrlInfo

    init {
        this.mainView = mainView
        this.context = context
        this.httpService = httpService
        this.storeService = storeService
        this.clipboardService = clipboardService
        this.iparseDom = iparseDom
        this.iSaveUrlInfo = iSaveUrlInfo
    }

    override fun getTitleWithURL(url: String) {
          httpService.fetchDataWithUrl(url)
                .map { t ->
                    iparseDom.getTitle(t)
                }
                .subscribe { title ->
                    if (title != null) mainView.showSaveScreenWithTitle(title, url)
                }
    }

    override fun saveToDB(title: String, url: String, tags: List<String>) {
        iSaveUrlInfo.saveUrlInfoWithLink(CoreLink(title, url, tags))
        this.mainView.setSiteListData(iSaveUrlInfo.loadData())
    }


    override fun refreshList() {
        this.mainView.setSiteListData(iSaveUrlInfo.loadData())
    }

    override fun saveLinkToCloud(title: String, conent: String) {
        storeService.store(title, conent).subscribe({
            this.mainView.showSaveCloudResult(it.title!!)
        }, {
            this.mainView.showSaveCloudResult(it.message!!)
        })
    }

    override fun checkClipboard() {
        fun checkClipBoardValidation(clipboardString: String) {
            val url = try {
                URL(clipboardString)
            } catch (e: MalformedURLException) {
                this.mainView.showNoLinkWithMsg("Invalid String")
                return
            }

            this.mainView.showDialog("${url.toString()}")
        }
        checkClipBoardValidation(clipboardService.content())
    }
}
