package com.kotlinchina.smallpockets.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.kotlinchina.smallpockets.R
import me.gujun.android.taggroup.TagGroup
import java.util.*

/**
 * Created by chenjunjun on 1/10/16.
 */
class ShowSiteListAdapter<E> internal constructor( val context: Context,  val list: ArrayList<HashMap<String,Any>>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        
        convertView?.let {//相当于if(convertView !=null){return convertView}
            return convertView
        }

        val showListView = View.inflate(context,R.layout.show_site_list_item,null)
        val holder =  ViewHolder()
        holder.title = showListView.findViewById(R.id.title) as? TextView
        holder.siteTag = showListView.findViewById(R.id.tag_group) as? TagGroup

        val defineData = list[position]
        val defineName = defineData?.get("name") as String
        holder.title?.text = defineName.toString()
        val tags = defineData?.get("tags") as ArrayList<String>
        Log.e("===", defineData?.get("tags").toString())
        holder.siteTag?.setTags(tags)

        return showListView
    }

    class ViewHolder{
        var title: TextView? = null
        var siteTag: TagGroup? = null
    }


}
