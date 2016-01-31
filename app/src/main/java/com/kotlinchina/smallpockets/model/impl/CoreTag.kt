package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Tag

/**
 * Created by jizhang on 1/31/16.
 */
class CoreTag : Tag {
    override var name: String? = null

    constructor(name: String) {
        this.name = name
    }
}
