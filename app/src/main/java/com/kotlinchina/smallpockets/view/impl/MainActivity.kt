package com.kotlinchina.smallpockets.view.impl

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast
import com.evernote.client.android.login.EvernoteLoginFragment
import com.kotlinchina.smallpockets.BuildConfig
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.adapter.ShowSiteListAdapter
import com.kotlinchina.smallpockets.application.PocketApplication
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.impl.*
import com.kotlinchina.smallpockets.transform.impl.LinksToHTMLWithHTMLEngine
import com.kotlinchina.smallpockets.view.Fragment.BaseWebViewFragment
import com.kotlinchina.smallpockets.view.IMainView
import net.hockeyapp.android.CrashManager
import java.util.*


class MainActivity : AppCompatActivity(), IMainView, EvernoteLoginFragment.ResultCallback {
    companion object {
        val SAVE_TAGS = "1000"
    }
    val CLIPBOARD_TAG: String = "CLIPBOARD"

    var mainPresenter: IMainPresenter? = null

    var listview: ListView? = null

    val datas: MutableList<Link>? = null

    var adapter: ShowSiteListAdapter? = null

    var mDrawerLayout: DrawerLayout? = null
    var drawer_content: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clipboardService = if (BuildConfig.DEBUG) DebugCacheClipboardService() else CacheClipboardService(this)

        this.mainPresenter = MainPresenter(mainView = this,
                context = applicationContext,
                httpService = VolleyHttpService(this),
                storeService = EvernoteStoreService(application),
                clipboardService = clipboardService,
                iparseDom = JxPathParseDom(),
                dataBaseStore = RealmStore(),
                linksToHTML = LinksToHTMLWithHTMLEngine(this))

        initView()
        setOnclickListener()
    }

    override fun onResume() {
        super.onResume()
        CrashManager.register(this)
    }

    private fun setOnclickListener() {
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this@MainActivity,"show detail"+ ": position" +l, Toast.LENGTH_SHORT).show()
            //在这里可以取得URl传递 过去即可
            supportFragmentManager.beginTransaction().replace(R.id.drawer_content, BaseWebViewFragment()).commit()
            mDrawerLayout?.openDrawer(drawer_content)//打开抽屉内容
        }
    }

    private fun initView() {
        listview = this.findViewById(R.id.listView) as ListView
        mDrawerLayout = this.findViewById(R.id.drawer_layout) as DrawerLayout
        drawer_content = this.findViewById(R.id.drawer_content) as RelativeLayout
    }

    override fun showDialog(link: String) {
        Log.e(CLIPBOARD_TAG, link)

        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle(R.string.save_link_ornot)
        dialog.setMessage(link)
        dialog.setPositiveButton(R.string.Ok, { dialogInterface, i ->
             mainPresenter?.getTitleWithURL(link)
        })
        dialog.setNegativeButton(R.string.Cancel, { dialogInterface, i ->
            Log.d(CLIPBOARD_TAG, "Cancel")
            mainPresenter?.refreshList()
        })
        dialog.show()
    }

    override fun showNoLinkWithMsg(msg: String) {
        Log.e(CLIPBOARD_TAG, msg)
    }

    override fun setSiteListData(data: List<Link>) {
        datas?.clear()
        adapter = ShowSiteListAdapter(this, R.layout.show_site_list_item, data)
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
                mainPresenter?.saveToDB(title, url, tags)
            }
        }
        dialog.show(fragmentManager, SAVE_TAGS)
    }

    override fun onStart() {
        super.onStart()
        this.mainPresenter?.checkClipboard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (checkEvernoteLogin()) {
            mainPresenter?.sycLinksOfCurrentWeekToCloud(Date())
        } else {
            val everNoteSession = (application as? PocketApplication)?.everNoteSession
            everNoteSession?.authenticate(this)
        }
        return true
    }

    private fun checkEvernoteLogin(): Boolean {
        val everNoteSession = (application as? PocketApplication)?.everNoteSession
        val logined = everNoteSession?.isLoggedIn
        return logined ?: false
    }

    override fun showSaveCloudResult(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLoginFinished(successful: Boolean) {
        if (successful) {
            mainPresenter?.sycLinksOfCurrentWeekToCloud(Date())
        } else {
            Toast.makeText(this, "authentication failed", Toast.LENGTH_SHORT).show()
        }
    }
}
