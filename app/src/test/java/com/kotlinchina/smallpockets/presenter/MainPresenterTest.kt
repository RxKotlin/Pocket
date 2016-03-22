package com.kotlinchina.smallpockets.presenter

import android.content.Context
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.view.IMainView
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*

class MainPresenterTest {

    var presenter: MainPresenter? = null
    var mockClipboardService: ClipboardService? = null
    var mockMainActivity: IMainView? = null


    @Before
    fun setUp() {
        mockMainActivity = mock(IMainView::class.java)
        mockClipboardService = mock(ClipboardService::class.java)
        presenter = MainPresenter(mockMainActivity!!, mock(Context::class.java),
                mock(HttpService::class.java), mock(StoreService::class.java),
                mockClipboardService!!, mock(IParseDom::class.java), mock(IDataBaseStore::class.java))
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
}