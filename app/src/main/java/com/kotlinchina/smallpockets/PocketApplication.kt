package com.kotlinchina.smallpockets

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Lewis on 2016/2/1.
 */

class PocketApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    fun init() {

        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
    }
}