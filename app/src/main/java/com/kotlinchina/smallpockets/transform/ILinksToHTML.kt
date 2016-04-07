package com.kotlinchina.smallpockets.transform

import com.kotlinchina.smallpockets.model.Link

interface ILinksToHTML {
    fun html(links: List<Link>): String?
}
