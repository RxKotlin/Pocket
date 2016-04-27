package com.kotlinchina.smallpockets.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class PocketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupRealm()
    }

    private fun setupRealm() {
        val realmConfiguration = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

}

