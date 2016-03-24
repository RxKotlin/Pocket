package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import com.kotlinchina.smallpockets.model.Link
import org.junit.Test
import org.mockito.BDDMockito.*

import org.junit.Assert.*

class EvernoteStoreServiceTest {

    @Test
    fun testStoreWeekly() {
        val context = mock(Context::class.java)
        val service = EvernoteStoreService(context)

        val lists = listOf<Link>()
        var msg: String? = ""
        service.storeWeekly(lists).subscribe({}, {
            msg = it.message
        })

        assertEquals("empty links array", msg)
    }
}
