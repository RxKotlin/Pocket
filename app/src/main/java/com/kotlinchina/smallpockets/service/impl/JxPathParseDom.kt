package com.kotlinchina.smallpockets.service.impl

import cn.wanghaomiao.xpath.model.JXDocument
import com.kotlinchina.smallpockets.service.IParseDom

class JxPathParseDom: IParseDom{
    override fun getTitle(domContent: String): String? {
        return JXDocument(domContent).sel("//title/text()").first() as? String
    }
}