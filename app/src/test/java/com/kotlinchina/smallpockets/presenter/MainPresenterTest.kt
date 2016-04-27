package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.IDataBaseStore
import com.kotlinchina.smallpockets.service.ShareService
import com.kotlinchina.smallpockets.transform.ILinksToHTML
import com.kotlinchina.smallpockets.view.IMainView
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.lang.kotlin.observable
import java.text.SimpleDateFormat

class MainPresenterTest {
    var presenter: MainPresenter? = null
    var mainView: IMainView? = null
    var dataBaseStore: IDataBaseStore? = null
    var linksToHTML: ILinksToHTML? = null
    var shareService: ShareService? = null

    @Before
    fun setUp() {
        mainView = mock(IMainView::class.java)
        dataBaseStore = mock(IDataBaseStore::class.java)
        linksToHTML = mock(ILinksToHTML::class.java)
        shareService = mock(ShareService::class.java)
        presenter = MainPresenter(mainView!!, dataBaseStore!!, linksToHTML!!, shareService!!)
    }


    @Test
    fun testShouldShowSaveCloudResultWhenWeHaveLinkInWeek() {
        val today = SimpleDateFormat("yyyy-MM-dd").parse("2016-3-25")
        val link1 = CoreLink("", "", null)
        link1.createDate = SimpleDateFormat("yyyy-MM-dd").parse("2016-3-24")
        val lists = listOf(link1, link1, link1)
        val success = observable<String> {
            it.onNext("Save Success")
            it.onCompleted()
        }
        given(dataBaseStore!!.queryDataByDate(link1.createDate!!, today)).willReturn(lists)
        given(shareService!!.share(anyString(), anyString())).willReturn(success)
        given(linksToHTML!!.html(anyListOf(Link::class.java))).willReturn("")
        presenter!!.shareWeeklyLinks(today)
        then(mainView!!).should(times(1)).showSaveCloudResult("Cool")
    }
}