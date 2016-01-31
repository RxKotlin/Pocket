package com.kotlinchina.smallpockets.model.db

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

/**
 * Created by jizhang on 1/31/16.
 */
open class RealmLink: RealmObject() {
    open var url: String? = null
    open var title: String? = null
    open var createDate: Date? = null
    open var tags: RealmList<RealmTag>? = null
}
