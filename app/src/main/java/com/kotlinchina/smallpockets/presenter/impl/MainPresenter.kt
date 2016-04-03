package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import android.util.Log
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.model.impl.formatedHtml
import com.kotlinchina.smallpockets.model.impl.html
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.service.impl.HTMLTemplateRender
import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter(mainView: IMainView, context: Context, httpService: HttpService, storeService: StoreService, clipboardService: ClipboardService,iparseDom: IParseDom, dataBaseStore: IDataBaseStore): IMainPresenter {

    var mainView: IMainView
    val context: Context
    val httpService: HttpService
    val storeService: StoreService
    val clipboardService: ClipboardService
    val iparseDom: IParseDom
    val dataBaseStore: IDataBaseStore

    init {
        this.mainView = mainView
        this.context = context
        this.httpService = httpService
        this.storeService = storeService
        this.clipboardService = clipboardService
        this.iparseDom = iparseDom
        this.dataBaseStore = dataBaseStore
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
        fun firstDateOfCurrentWeek(today: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, -calendar.firstDayOfWeek);
            return calendar.time
        }

        fun titleForm(currentDate: Date, firstDate: Date): String  {
            val simpleDateFormat = SimpleDateFormat("MM-DD")
            return "${simpleDateFormat.format(firstDate)} ~ ${simpleDateFormat.format(currentDate)} Weekly"
        }

        val firstDate = firstDateOfCurrentWeek(today)
        val title = titleForm(today, firstDate)
        val html = (dataBaseStore.queryDataByDate(firstDate, today)).formatedHtml(context)
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
