package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.model.impl.html
import org.junit.Test
import org.mockito.BDDMockito.*

import org.junit.Assert.*

class LinkListMapperTest {

    @Test
    fun testShouldNullWhenLinksNotHaveLink() {
        val links = listOf<Link>()
        assertEquals(null, links.html())
    }

    @Test
    fun testShouldReturnHTMLWhenLinksNotHaveLink() {
        val links = listOf<Link>(CoreLink("1", "2", null), CoreLink("a", "b", null))
        assertEquals("<a href='2'>1</a><br/><a href='b'>a</a><br/>", links.html())
    }
}
