package com.kotlinchina.smallpockets.adapter

import android.content.Context
import com.joanzapata.android.BaseAdapterHelper
import com.joanzapata.android.QuickAdapter
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.Tag
import me.gujun.android.taggroup.TagGroup

class ShowSiteListAdapter(context: Context, layoutResId: Int, data: List<Link>):
        QuickAdapter<Link>(context, layoutResId, data) {

    override fun convert(helper: BaseAdapterHelper?, item: Link?) {
        fun setTag(viewId: Int, tags: List<Tag>?) {
            val tagGroup = helper?.getView<TagGroup>(viewId) as TagGroup
            val tags = tags?.map { t -> t.name }
            tagGroup.setTags(tags)
        }

        if (item == null) { return }
        helper?.setText(R.id.title, item.title)
        setTag(R.id.tag_group, item.tags)
    }
}
