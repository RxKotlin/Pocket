package com.kotlinchina.smallpockets.model.impl

import com.kotlinchina.smallpockets.model.Link
import java.text.SimpleDateFormat

fun List<Link>.toMap(): Map<String, Any> {
    val simpleDateFormat = SimpleDateFormat("MM-DD")
    return mapOf(
            "links" to this.map { link ->
                mapOf(
                        "title" to link.title,
                        "link" to link.url,
                        "tags" to link.tags?.map { tag ->
                            mapOf("name" to tag.name)
                        },
                        "date" to simpleDateFormat.format(link.createDate)
                )
            }
    )
}
