package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import android.util.Log
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.service.impl.CalendarService
import com.kotlinchina.smallpockets.transform.ILinksToHTML
import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter(mainView: IMainView, context: Context, httpService: HttpService, storeService: StoreService, clipboardService: ClipboardService,iparseDom: IParseDom, dataBaseStore: IDataBaseStore, linksToHTML: ILinksToHTML): IMainPresenter {

    var mainView: IMainView
    val context: Context
    val httpService: HttpService
    val storeService: StoreService
    val clipboardService: ClipboardService
    val iparseDom: IParseDom
    val dataBaseStore: IDataBaseStore
    val linksToHTML: ILinksToHTML

    init {
        this.mainView = mainView
        this.context = context
        this.httpService = httpService
        this.storeService = storeService
        this.clipboardService = clipboardService
        this.iparseDom = iparseDom
        this.dataBaseStore = dataBaseStore
        this.linksToHTML = linksToHTML
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
        dataBaseStore.saveUrlInfoWithLink(CoreLink(title, url, tags))
        this.mainView.setSiteListData(dataBaseStore.loadData())
    }


    override fun refreshList() {
        this.mainView.setSiteListData(dataBaseStore.loadData())
    }

    override fun sycLinksOfCurrentWeekToCloud(today: Date) {
        fun titleForm(beginDate: Date, endDate: Date): String  {
            val simpleDateFormat = SimpleDateFormat("MM-dd")
            return "${simpleDateFormat.format(beginDate)} ~ ${simpleDateFormat.format(endDate)} Weekly"
        }
        val datePair = CalendarService().getMondayAndSundayDateOfThisWeek(today)
        val title = titleForm(datePair.first, datePair.second)
        val links = dataBaseStore.queryDataByDate(datePair.first, datePair.second)
        val html = linksToHTML.html(links)
        if (html == null) {
            Log.e("${this.javaClass}", "format error")
            return
        }

        storeService.store(title, html).subscribe ({
            this.mainView.showSaveCloudResult("Cool")
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
