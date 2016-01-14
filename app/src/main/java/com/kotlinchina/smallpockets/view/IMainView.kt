package com.kotlinchina.smallpockets.view

import java.util.*

interface IMainView {
    fun showLink(link: String)
    fun showNoLinkWithMsg(msg: String)
    fun setSiteListData(data: ArrayList<HashMap<String, Any>>)
}
