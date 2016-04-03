package com.kotlinchina.smallpockets.model.impl

import android.content.Context
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.service.impl.HTMLTemplateRender
import java.text.SimpleDateFormat

fun List<Link>.formatedHtml(context: Context): String? {
    val simpleDateFormat = SimpleDateFormat("MM-DD")
    val data = mapOf(
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
    val htmlTemplateRender = HTMLTemplateRender("www/template.html", context)
    return htmlTemplateRender.render(data)
}