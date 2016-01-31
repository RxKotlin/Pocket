package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Tag
import com.kotlinchina.smallpockets.model.db.RealmTag

/**
 * Created by jizhang on 1/31/16.
 */
class ProxyRealmTag: Tag {
    private var tag: RealmTag

    constructor(tag: RealmTag) {
        this.tag = tag
    }

    override var name: String?
        get() = tag.name
        set(value) {
            tag.name = value
        }
}
