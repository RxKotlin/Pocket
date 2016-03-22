package com.kotlinchina.smallpockets.service

import com.kotlinchina.smallpockets.model.Link
import java.util.*

interface IDataBaseStore {
    fun saveUrlInfoWithLink(link: Link)
    fun loadData(): List<Link>
    fun loadDataByDate(fromDate: Date, toDate: Date): List<Link>
}