package com.kotlinchina.smallpockets.view.impl

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.kotlinchina.smallpockets.BuildConfig
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.presenter.ILinkListPresenter
import com.kotlinchina.smallpockets.presenter.impl.LinkListPresenter
import com.kotlinchina.smallpockets.service.impl.*
import com.kotlinchina.smallpockets.transform.impl.LinksToHTMLWithHTMLEngine
import com.kotlinchina.smallpockets.view.ILinkListView

class LinkListFragment() : Fragment(), ILinkListView {

    var linkListPresenter: ILinkListPresenter? = null

    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var listview: ListView? = null

    val datas: MutableList<Link>? = null

    var adapter: ShowSiteListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipboardService = if (BuildConfig.DEBUG) DebugCacheClipboardService() else CacheClipboardService(activity)
        linkListPresenter = LinkListPresenter(linkListView = this,
                httpService = WebViewClientHttpService(activity.applicationContext),
                clipboardService = clipboardService,
                dataBaseStore = RealmStore())
    }

    override fun onStart() {
        super.onStart()
        listview = activity.findViewById(R.id.link_list) as ListView
        linkListPresenter?.refreshList()
        linkListPresenter?.checkClipboard()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.link_list_fragment, container, false)
        return view
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
}