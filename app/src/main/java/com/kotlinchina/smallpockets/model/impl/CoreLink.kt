package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.Tag
import java.util.*

/**
 * Created by jizhang on 1/31/16.
 */
class CoreLink : Link {

    override var url: String? = null
    override var title: String? = null
    override var createDate: Date? = null
    override var tags: ArrayList<Tag>? = null

    constructor(title: String, url: String, tags: Array<String>) {
        this.title = title
        this.url = url
        this.createDate = Date()
        this.tags = ArrayList<Tag>()
        tags.forEach {
            val tag = CoreTag(it)
            this.tags?.add(tag)
        }
    }
}
