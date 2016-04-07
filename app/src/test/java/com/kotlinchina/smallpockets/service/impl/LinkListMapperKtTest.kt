package com.kotlinchina.smallpockets.service.impl

import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.model.impl.toMap
import org.junit.Test

import org.junit.Assert.*

class LinkListMapperTest {
    @Test
    fun testShouldBeMapWhenExecToMap() {
        val links = listOf<Link>(CoreLink("1", "2", listOf("11", "22")), CoreLink("a", "b", null))
        val map = links.toMap()
        val mapedLinks = map["links"] as List<Map<String, Any>>
        val mapedLink1Tags = mapedLinks[0]["tags"] as List<Map<String, String>>

        assertNotNull(mapedLinks)
        assertEquals(2, mapedLinks.count())
        assertEquals("1", mapedLinks[0]["title"])
        assertEquals("2", mapedLinks[0]["link"])
        assertEquals("11", mapedLink1Tags[0]["name"])
        assertEquals("22", mapedLink1Tags[1]["name"])
        assertEquals("a", mapedLinks[1]["title"])
        assertEquals("b", mapedLinks[1]["link"])
        assertEquals(emptyList<Map<String, String>>(), mapedLinks[1]["tags"])
    }
}
