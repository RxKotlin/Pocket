package com.kotlinchina.smallpockets.service.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import com.kotlinchina.smallpockets.service.ISaveUrlInfo
import io.realm.Realm

class RealmSavaUrlInfo: ISaveUrlInfo {
    override fun saveUrlInfoWithLink(link: Link) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val realmLink = realm.createObject(RealmLink::class.java)
            realmLink.title = link.title
            realmLink.url = link.url
            realmLink.createDate = link.createDate
            link.tags?.forEach { it ->
                val realmTag = realm.createObject(RealmTag::class.java)
                realmTag.name = it.name
                realmLink.tags?.add(realmTag)
            }
            realm.copyFromRealm(realmLink)
        }
    }
}