package com.kotlinchina.smallpockets.presenter.impl

import android.util.Log
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.service.impl.CalendarService
import com.kotlinchina.smallpockets.transform.ILinksToHTML
import com.kotlinchina.smallpockets.view.IMainView
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter: IMainPresenter {

    var mainView: IMainView? = null
    var dataBaseStore: IDataBaseStore? = null
    var linksToHTML: ILinksToHTML? = null
    var storeService: StoreService? = null

    constructor(mainView: IMainView, dataBaseStore: IDataBaseStore, linksToHTML: ILinksToHTML,
                storeService: StoreService) {
        this.mainView = mainView
        this.dataBaseStore = dataBaseStore
        this.linksToHTML = linksToHTML
        this.storeService = storeService
    }

    override fun sycLinksOfCurrentWeekToCloud(today: Date) {
        fun titleForm(beginDate: Date, endDate: Date): String  {
            val simpleDateFormat = SimpleDateFormat("MM-dd")
            return "${simpleDateFormat.format(beginDate)} ~ ${simpleDateFormat.format(endDate)} Weekly"
        }

        val datePair = CalendarService().getMondayAndSundayDateOfThisWeek(today)
        val title = titleForm(datePair.first, datePair.second)
        val links = dataBaseStore?.queryDataByDate(datePair.first, datePair.second)
        val html = linksToHTML?.html(links!!)
        if (html == null) {
            Log.e("${this.javaClass}", "format error")
            return
        }

        storeService?.store(title, html)?.subscribe ({
            this.mainView?.showSaveCloudResult("Cool")
        }, {
            this.mainView?.showSaveCloudResult(it.message!!)
        })
    }
}