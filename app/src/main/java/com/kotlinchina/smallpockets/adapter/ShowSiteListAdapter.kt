package com.kotlinchina.smallpockets.adapter

import android.content.Context
import com.joanzapata.android.BaseAdapterHelper
import com.joanzapata.android.QuickAdapter
import com.kotlinchina.smallpockets.R
import me.gujun.android.taggroup.TagGroup

class ShowSiteListAdapter(context: Context, layoutResId: Int, data: List<MutableMap<String,Any>>):
        QuickAdapter<MutableMap<String,Any>>(context, layoutResId, data) {

    override fun convert(helper: BaseAdapterHelper?, item: MutableMap<String, Any>?) {
        if (item == null) { return }

        helper?.setText(R.id.title, item["name"] as String)

        val tagGroup = helper?.getView<TagGroup>(R.id.tag_group) as TagGroup
        val tags = item["tags"] as List<String>
        tagGroup.setTags(tags)
    }
}
