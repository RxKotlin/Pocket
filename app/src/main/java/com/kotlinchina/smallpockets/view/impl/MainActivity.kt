package com.kotlinchina.smallpockets.view.impl

import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.evernote.client.android.login.EvernoteLoginFragment
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.application.PocketApplication
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.impl.EvernoteStoreService
import com.kotlinchina.smallpockets.service.impl.RealmStore
import com.kotlinchina.smallpockets.transform.impl.LinksToHTMLWithHTMLEngine
import com.kotlinchina.smallpockets.view.IMainView
import net.hockeyapp.android.CrashManager
import java.util.*

class MainActivity : AppCompatActivity(), IMainView, EvernoteLoginFragment.ResultCallback {
    companion object {
        val SAVE_TAGS = "1000"
    }

    var mainPresenter: IMainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mainPresenter = MainPresenter(this, RealmStore(), LinksToHTMLWithHTMLEngine(this), EvernoteStoreService(application))

        val fragmentTransaction = fragmentManager.beginTransaction()
        val linkListFragment = LinkListFragment()
        fragmentTransaction.add(R.id.main_container, linkListFragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        CrashManager.register(this)
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

    override fun onLoginFinished(successful: Boolean) {
        mainPresenter?.sycLinksOfCurrentWeekToCloud(Date())
    }

    override fun showSaveCloudResult(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
