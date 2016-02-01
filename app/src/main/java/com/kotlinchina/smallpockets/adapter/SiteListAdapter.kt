package com.kotlinchina.smallpockets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.model.db.RealmLink
import com.kotlinchina.smallpockets.model.db.RealmTag
import io.realm.RealmBaseAdapter
import io.realm.RealmList
import io.realm.RealmResults
import me.gujun.android.taggroup.TagGroup

/**
 * Created by Lewis on 2016/2/1.
 */

class SiteListAdapter(val ctx: Context, var data: RealmResults<RealmLink>, val automaticUpdate: Boolean)
    : RealmBaseAdapter<RealmLink>(ctx, data, automaticUpdate), ListAdapter {

    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): RealmLink? {
        return data[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View?
        val holder: ViewHolder

        if (convertView == null) {

            view = layoutInflater.inflate(R.layout.show_site_list_item, parent, false)
            holder = ViewHolder()
            holder.title = view.findViewById(R.id.title) as? TextView
            holder.siteTag = view.findViewById(R.id.tag_group) as? TagGroup

            view?.tag = holder
        } else {

            view = convertView
            holder = view.tag as ViewHolder
        }

        val link = data[position]

        holder.title?.text = link?.title

        val tags = link?.tags as RealmList<RealmTag>
        holder.siteTag?.setTags(tags.map { it -> it.name })

        return view;
    }

    class ViewHolder{
        var title: TextView? = null
        var siteTag: TagGroup? = null
    }
}
