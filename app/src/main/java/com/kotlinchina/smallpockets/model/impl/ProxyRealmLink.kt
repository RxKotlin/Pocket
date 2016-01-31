package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.db.RealmLink
import java.util.*

/**
 * Created by jizhang on 1/31/16.
 */
class ProxyRealmLink : Link {
    private val link: RealmLink
    
    constructor(link: RealmLink) {
        this.link = link
    }

    override var url: String?
        get() = link.url
        set(value) {
            link.url = value
        }
    override var title: String?
        get() = link.title
        set(value) {
            link.title = value
        }
    override var createDate: Date?
        get() = link.createDate
        set(value) {
            link.createDate = value
        }
}
