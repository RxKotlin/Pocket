package com.kotlinchina.smallpockets.service

import com.kotlinchina.smallpockets.model.Link

interface ISaveUrlInfo {
    fun saveUrlInfoWithLink(link: Link)
}