package com.kotlinchina.smallpockets.view.impl

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RelativeLayout
import com.kotlinchina.smallpockets.BuildConfig
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.presenter.ILinkListPresenter
import com.kotlinchina.smallpockets.presenter.impl.LinkListPresenter
import com.kotlinchina.smallpockets.service.impl.CacheClipboardService
import com.kotlinchina.smallpockets.service.impl.DebugCacheClipboardService
import com.kotlinchina.smallpockets.service.impl.RealmStore
import com.kotlinchina.smallpockets.service.impl.WebViewClientHttpService
import com.kotlinchina.smallpockets.view.Fragment.BaseWebViewFragment
import com.kotlinchina.smallpockets.view.ILinkListView
import com.melnykov.fab.FloatingActionButton
import java.util.*

class LinkListFragment() : Fragment(), ILinkListView ,SwipeRefreshLayout.OnRefreshListener {

    var linkListPresenter: ILinkListPresenter? = null
    val CLIPBOARD_TAG: String = "CLIPBOARD"
    var listView: ListView? = null
    var data: MutableList<Link> = ArrayList()
    var adapter: ShowSiteListAdapter? = null
    var drawerLayout: DrawerLayout? = null
    var drawerContent: RelativeLayout? = null
    var fab: FloatingActionButton? = null
    var shShareWeeklyLinks:ShareWeeklyLinks? = null
    var swipeLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipboardService = if (BuildConfig.DEBUG) DebugCacheClipboardService() else CacheClipboardService(activity)
        linkListPresenter = LinkListPresenter(linkListView = this,
                httpService = WebViewClientHttpService(activity.applicationContext),
                clipboardService = clipboardService,
                dataBaseStore = RealmStore())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linkListPresenter?.refreshList()
        linkListPresenter?.checkClipboard()
        setupListViewItem()
    }

    private fun setupListViewItem() {
        listView?.setOnItemClickListener { adapterView, view, i, l ->
            val perSaveUrl = data?.get(i)?.url
            childFragmentManager.beginTransaction().replace(R.id.drawer_content,BaseWebViewFragment.newInstance(perSaveUrl)).commit()
            drawerLayout?.openDrawer(drawerContent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.link_list_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setSwipeLayout()
    }

    private fun setSwipeLayout() {
        swipeLayout?.setOnRefreshListener(this)
        swipeLayout?.setSize(SwipeRefreshLayout.LARGE)
        swipeLayout?.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright)
    }

    private fun initView(view: View?) {
        listView = view?.findViewById(R.id.link_list) as? ListView
        drawerLayout = view?.findViewById(R.id.drawer_layout) as? DrawerLayout
        drawerContent = view?.findViewById(R.id.drawer_content) as? RelativeLayout
        fab = view?.findViewById(R.id.fab) as? FloatingActionButton
        swipeLayout = view?.findViewById(R.id.swipeLayout) as? SwipeRefreshLayout
        var curList = listView as? ListView
        if(curList !=null){
            fab?.attachToListView(curList)
        }
        fab?.setOnClickListener {
            shShareWeeklyLinks?.shareLink()
        }
    }

    override fun showDialog(link: String) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        dialog.setTitle(R.string.save_link_ornot)
        dialog.setMessage(link)
        dialog.setPositiveButton(R.string.Ok, { dialogInterface, i ->
            linkListPresenter?.getTitleWithURL(link)
        })
        dialog.setNegativeButton(R.string.Cancel, { dialogInterface, i ->
            Log.d(CLIPBOARD_TAG, "Cancel")
        })
        dialog.show()
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }

    override fun setSiteListData(dataResult: List<Link>) {
        data?.clear()
        data?.addAll(dataResult)
        adapter = ShowSiteListAdapter(activity, R.layout.show_site_list_item, dataResult)
        listView?.adapter = adapter
        swipeLayout?.isRefreshing = false;
    }

    override fun showSaveScreenWithTitle(title: String, url: String) {
        val dialog = SaveTagDialog()
        var bundle = Bundle()
        bundle.putString(SaveTagDialog.TITLE, title)
        bundle.putString(SaveTagDialog.URL, url)
        dialog.arguments = bundle
        dialog.onSave = { data ->
            val title = data[SaveTagDialog.TITLE] as? String
            val url = data[SaveTagDialog.URL] as? String
            val tags = data[SaveTagDialog.TAGS] as? List<String>
            if (title != null && url != null && tags != null) {
                linkListPresenter?.saveToDB(title, url, tags)
            }
        }
        dialog.show(fragmentManager, MainActivity.SAVE_TAGS)
    }

    fun backPressed():Boolean {
        val isDrawerLayoutOpenOrNot = drawerLayout?.isDrawerOpen(drawerContent) as? Boolean
        if(isDrawerLayoutOpenOrNot !=null && isDrawerLayoutOpenOrNot){
            drawerLayout?.closeDrawer(drawerContent)
            return true
        }else{
            return false
        }
    }

    fun setShareWeeklyLinks(shShareWeeklyLinks:ShareWeeklyLinks){
        this.shShareWeeklyLinks = shShareWeeklyLinks
    }

    interface ShareWeeklyLinks{
        fun shareLink()
    }

    override fun onRefresh() {
        linkListPresenter?.refreshList()
    }
}