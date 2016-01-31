package com.kotlinchina.smallpockets.model.db

import com.kotlinchina.smallpockets.model.Tag
import io.realm.RealmObject

/**
 * Created by jizhang on 1/31/16.
 */
open class RealmTag: RealmObject(), Tag {
    open override var name: String? = null
}