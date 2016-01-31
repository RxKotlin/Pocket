package com.kotlinchina.smallpockets.model.db

import io.realm.RealmObject

/**
 * Created by jizhang on 1/31/16.
 */
open class RealmTag: RealmObject() {
    open var name: String? = null
}
