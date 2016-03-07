package com.kotlinchina.smallpockets.presenter.impl

import android.content.Context
import cn.wanghaomiao.xpath.model.JXDocument
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.presenter.IMainPresenter
import com.kotlinchina.smallpockets.service.HttpService
import com.kotlinchina.smallpockets.view.IMainView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.arrayListOf
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.hashMapOf

class MainPresenter(mainView: IMainView, context: Context, httpService: HttpService): IMainPresenter {

    var mainView: IMainView
    val context: Context
    val httpService: HttpService

    init {
        this.mainView = mainView
        this.context = context
        this.httpService = httpService
    }

    override fun checkClipBoardValidation(clipboardString: String) {
        val url = try {
            URL(clipboardString)
        } catch (e: MalformedURLException) {
            this.mainView.showNoLinkWithMsg("Invalid String")
            return
        }

        this.mainView.showDialog("${url.toString()}")
    }

    override fun getTitleWithURL(url: String) {
        fun titleFromData(t: String): String? {
            val title = JXDocument(t).sel("//title/text()").first()
            return title as? String
        }

        httpService.fetchDataWithUrl(url)
                .map { t ->
                    titleFromData(t)
                }
                .subscribe { title ->
                    if (title != null) mainView.showSaveScreenWithTitle(title, url)
                }
    }

    override fun saveToDB(title: String, url: String, tags: Array<String>) {
        fun realmLinkWithLink(link: Link) {
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


        realmLinkWithLink(CoreLink(title, url, tags))
        this.mainView.setSiteListData(loadDB())
    }

    fun loadDB(): ArrayList<HashMap<String, Any>> {
        val realm = Realm.getDefaultInstance()
        val query = realm.where(RealmLink::class.java)
        val queryResults = query.findAll()

        var returnResults = ArrayList<HashMap<String, Any>>()

        for (item in queryResults) {
            var itemResult = HashMap<String, Any>()
            itemResult["name"] = item.title ?: "unknown"
            if (item.tags == null) {
                itemResult["tags"] = arrayListOf("unknown")
            } else {
                var tags = ArrayList<String>()
                for (tag in item.tags!!) {
                    tags.add(tag.name ?: "unknown")
                }
                if (tags.size == 0) {
                    tags.add("unknown")
                }
                itemResult["tags"] = tags
            }
            returnResults.add(itemResult)
        }

        return returnResults
    }

    override fun refreshList() {
        this.mainView.setSiteListData(loadDB())
    }
}
