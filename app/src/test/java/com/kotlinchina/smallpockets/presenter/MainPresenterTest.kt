package com.kotlinchina.smallpockets.presenter

import android.content.Context
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.view.IMainView
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.lang.kotlin.observable
import java.text.SimpleDateFormat
import java.util.*

class MainPresenterTest {

    var presenter: MainPresenter? = null
    var mockClipboardService: ClipboardService? = null
    var mockMainActivity: IMainView? = null
    var mockDatabaseStore: IDataBaseStore? = null
    var mockStoreService: StoreService? = null

    @Before
    fun setUp() {
        mockMainActivity = mock(IMainView::class.java)
        mockClipboardService = mock(ClipboardService::class.java)
        mockDatabaseStore = mock(IDataBaseStore::class.java)
        mockStoreService = mock(StoreService::class.java)
        presenter = MainPresenter(mockMainActivity!!, mock(Context::class.java),
                mock(HttpService::class.java), mockStoreService!!,
                mockClipboardService!!, mock(IParseDom::class.java), mockDatabaseStore!!)
    }



    @Test
    fun testCheckClipboardWhenClipBoardHasValidUrl() {
        given(mockClipboardService!!.content()).willReturn("http://google.com")
        presenter!!.checkClipboard()
        then(mockMainActivity!!).should(times(1)).showDialog("http://google.com");
    }

    @Test
    fun testNotShowLinkWhenCheckClipboardHasInValidUrl() {
        given(mockClipboardService!!.content()).willReturn("fjdklsjaklfjdkslajfklasjlk")
        presenter!!.checkClipboard()
        then(mockMainActivity!!).should(times(1)).showNoLinkWithMsg("Invalid String");
        then(mockMainActivity!!).should(never()).showDialog(anyString());
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
        given(mockDatabaseStore!!.queryDataByDate(link1.createDate!!, today)).willReturn(lists)
        given(mockStoreService!!.store(anyString(), anyString())).willReturn(success)
        presenter!!.sycLinksOfCurrentWeekToCloud(today)
        then(mockMainActivity!!).should(times(1)).showSaveCloudResult("Cool")
    }
}