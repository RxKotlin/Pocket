package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.view.IMainView
import java.net.MalformedURLException
import java.net.URL
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

    override fun sycLinksOfCurrentWeekToCloud() {
        fun firstDateOfCurrentWeek(): Date {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -calendar.firstDayOfWeek);
            return calendar.time
        }

        storeService.storeWeekly(dataBaseStore.loadDataByDate(firstDateOfCurrentWeek(), Date())).subscribe {
            storeService.store("Weekly", it).subscribe({
                this.mainView.showSaveCloudResult(it.title!!)
            }, {
                this.mainView.showSaveCloudResult(it.message!!)
            })
        }
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
