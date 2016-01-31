package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.Tag
import com.parse.ParseObject
import java.util.*

/**
 * Created by jizhang on 1/28/16.
 */
class ParseLink: ParseObject(), Link {
    val kLINK_URL = "url"
    val kLINK_TITLE = "title"
    val kLINK_CREATE_DATE = "createDate"

    override var url: String?
        get() = getString(kLINK_URL)
        set(value) {
            put(kLINK_URL, url)
        }
    override var title: String?
        get() = getString(kLINK_TITLE)
        set(value) {
            put(kLINK_TITLE, title)
        }
    override var createDate: Date?
        get() = getDate(kLINK_CREATE_DATE)
        set(value) {
            put(kLINK_CREATE_DATE, createDate)
        }
    override var tags: ArrayList<Tag>?
        get() = throw UnsupportedOperationException()
        set(value) {
        }
}