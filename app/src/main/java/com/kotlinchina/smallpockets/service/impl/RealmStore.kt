package com.kotlinchina.smallpockets.service.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.service.IDataBaseStore
import io.realm.Realm
import java.util.*

class RealmStore : IDataBaseStore {

    override fun loadData(): List<Link> {
        val realm = Realm.getDefaultInstance()
        val query = realm.where(RealmLink::class.java)
        val queryResults = query.findAll()

        return queryResults.map { link ->
            transformRealmLinkToCoreLink(link)
        }
    }

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

    override fun loadDataByDate(fromDate: Date, toDate: Date): List<Link> {
        val realm = Realm.getDefaultInstance()
        val results = realm.where(RealmLink::class.java).between("createDate", fromDate, toDate).findAll()
        return results.map {
            transformRealmLinkToCoreLink(it)
        }
    }

    private fun transformRealmLinkToCoreLink(link: RealmLink): CoreLink {
        val title = link.title ?: ""
        val url = link.url ?: ""
        val tags = link.tags?.map { tag ->
            tag.name ?: ""
        }
        return CoreLink(title, url, tags)
    }
}