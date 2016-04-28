package com.kotlinchina.smallpockets.presenter

import com.kotlinchina.smallpockets.presenter.impl.LinkListPresenter
import com.kotlinchina.smallpockets.service.*
import com.kotlinchina.smallpockets.transform.ILinksToHTML
import com.kotlinchina.smallpockets.view.ILinkListView
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*

class LinkListPresenterTest {

    var presenter: LinkListPresenter? = null
    var mockClipboardService: ClipboardService? = null
    var mockLinkListActivity: ILinkListView? = null
    var mockDatabaseStore: IDataBaseStore? = null
    var mockShareService: ShareService? = null
    var mockLinksToHTML: ILinksToHTML? = null

    @Before
    fun setUp() {
        mockLinkListActivity = mock(ILinkListView::class.java)
        mockClipboardService = mock(ClipboardService::class.java)
        mockDatabaseStore = mock(IDataBaseStore::class.java)
        mockShareService = mock(ShareService::class.java)
        mockLinksToHTML = mock(ILinksToHTML::class.java)
        presenter = LinkListPresenter(
                mockLinkListActivity!!,
                mock(HttpService::class.java),
                mockClipboardService!!, mockDatabaseStore!!)
    }

    @Test
    fun testCheckClipboardWhenClipBoardHasValidUrl() {
        given(mockClipboardService!!.content()).willReturn("http://google.com")
        presenter!!.checkClipboard()
        then(mockLinkListActivity!!).should(times(1)).showDialog("http://google.com");
    }

    @Test
    fun testNotShowLinkWhenCheckClipboardHasInValidUrl() {
        given(mockClipboardService!!.content()).willReturn("fjdklsjaklfjdkslajfklasjlk")
        presenter!!.checkClipboard()
        then(mockLinkListActivity!!).should(times(1)).showNoLinkWithMsg("Invalid String");
        then(mockLinkListActivity!!).should(never()).showDialog(anyString());
    }

}