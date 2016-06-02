package com.kotlinchina.smallpockets.view.impl

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.kotlinchina.smallpockets.R
import me.gujun.android.taggroup.TagGroup

class SaveTagDialog: DialogFragment() {

    companion object {
        val TITLE = "title"
        val URL = "url"
        val TAGS = "tags"
    }

    var onSave: ((data: Map<String, Any>)->Unit)? = null
    var saveTagGroup: TagGroup? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val title = arguments?.getString(TITLE)
        val url = arguments?.getString(URL)
        val layoutInflater = activity.layoutInflater
        val view = layoutInflater.inflate(R.layout.activity_save_tag, null)
        builder.setView(view)
                .setTitle(title)
                .setMessage(url)
                .setPositiveButton("Save", { dialogInterface, i ->
                    val tags = saveTagGroup?.tags
                    if (title != null && url != null && tags != null) {
                        this@SaveTagDialog.onSave?.invoke(mapOf(
                                TITLE to title,
                                URL to url,
                                TAGS to tags.toList()
                        ))
                    }
                })
        saveTagGroup = view.findViewById(R.id.save_tag_group) as? TagGroup
        return builder.create()
    }
}