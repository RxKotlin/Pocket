package com.kotlinchina.smallpockets.view.impl

import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.evernote.client.android.EvernoteSession
import com.evernote.client.android.login.EvernoteLoginFragment
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.presenter.impl.MainPresenter
import com.kotlinchina.smallpockets.service.impl.EvernoteShareService
import com.kotlinchina.smallpockets.service.impl.RealmStore
import com.kotlinchina.smallpockets.transform.impl.LinksToHTMLWithHTMLEngine
import com.kotlinchina.smallpockets.view.IMainView
import net.hockeyapp.android.CrashManager
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), IMainView, EvernoteLoginFragment.ResultCallback {
    companion object {
        val SAVE_TAGS = "1000"
        val EVERNOTE_SERVICE = EvernoteSession.EvernoteService.PRODUCTION
    }

    var mainPresenter: IMainPresenter? = null
    var evernoteSession: EvernoteSession? = null
    var linkListFragment: LinkListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupFrament()
        setupPresneter()
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
            mainPresenter?.shareWeeklyLinks(Date())
        } else {
            evernoteSession?.authenticate(this)
        }
        return true
    }

    private fun checkEvernoteLogin(): Boolean {
        val logined = evernoteSession?.isLoggedIn
        return logined ?: false
    }

    override fun onLoginFinished(successful: Boolean) {
        mainPresenter?.shareWeeklyLinks(Date())
    }

    override fun showSaveCloudResult(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupEvernoteSession(): EvernoteSession? {
        return try {
            val progerties = Properties()
            val inStream = assets.open("evernote.properties")
            progerties.load(inStream)
            val key = progerties.getProperty("consumer_key")
            val secret = progerties.getProperty("consumer_secret")
            inStream.close()

            EvernoteSession.Builder(this)
                    .setEvernoteService(EVERNOTE_SERVICE)
                    .setSupportAppLinkedNotebooks(false)
                    .build(key, secret)
                    .asSingleton()
        } catch (e: IOException) {
            null
        }
    }

    private fun setupPresneter() {
        val evernoteSession = setupEvernoteSession() ?: return
        mainPresenter = MainPresenter(this, RealmStore(), LinksToHTMLWithHTMLEngine(this), EvernoteShareService(evernoteSession))
        this.evernoteSession = evernoteSession
    }

    private fun setupFrament() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        linkListFragment = LinkListFragment()
        fragmentTransaction.add(R.id.main_container, linkListFragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val backPressedEvent = linkListFragment?.backPressed() as? Boolean
        if(backPressedEvent!=null && !backPressedEvent){
            super.onBackPressed()
        }
    }
}
