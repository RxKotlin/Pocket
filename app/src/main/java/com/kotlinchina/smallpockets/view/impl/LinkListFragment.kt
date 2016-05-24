package com.kotlinchina.smallpockets.view.impl

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
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
import java.util.*

class LinkListFragment() : Fragment(), ILinkListView {

    var linkListPresenter: ILinkListPresenter? = null

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var listview: ListView? = null

    internal var datas: MutableList<Link> = ArrayList()

    var adapter: ShowSiteListAdapter? = null

    var mDrawerLayout: DrawerLayout? = null

    var drawer_content: RelativeLayout? = null


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
        setOnclickListener()
    }

    private fun setOnclickListener() {
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            val perSaveUrl:String? = datas?.get(i)?.url
            childFragmentManager.beginTransaction().replace(R.id.drawer_content,BaseWebViewFragment.newInstance(perSaveUrl!!)).commit()
            mDrawerLayout?.openDrawer(drawer_content)//打开抽屉内容
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.link_list_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listview = view?.findViewById(R.id.link_list) as ListView
        mDrawerLayout = view?.findViewById(R.id.drawer_layout) as DrawerLayout
        drawer_content = view?.findViewById(R.id.drawer_content) as RelativeLayout
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

    override fun setSiteListData(data: List<Link>) {
        datas?.clear()
        datas?.addAll(data)
        adapter = ShowSiteListAdapter(activity, R.layout.show_site_list_item, data)
        listview?.adapter = adapter
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
            if (title != null
                    && url != null
                    && tags != null) {
                linkListPresenter?.saveToDB(title, url, tags)
            }
        }
        dialog.show(fragmentManager, MainActivity.SAVE_TAGS)
    }

    public fun backPressed():Boolean {
        if(mDrawerLayout?.isDrawerOpen(drawer_content)!!){
            mDrawerLayout?.closeDrawer(drawer_content)
            return true ;
        }else{
            return false ;
        }
    }

}