package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Link

fun List<Link>.html(): String? {
    if (this.count() <= 0) {
        return null
    }
    return this.filter {
        it.title != null && it.url != null && it.createDate != null
    }.sortedBy { it.createDate }.map {
        "<a href='${it.url}'>${it.title}</a>"
    }.reduce { result, link ->
        "$result<br/>$link<br/>"
    }
}