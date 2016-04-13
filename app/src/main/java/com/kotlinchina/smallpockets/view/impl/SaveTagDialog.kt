package com.kotlinchina.smallpockets.view.impl

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.kotlinchina.smallpockets.R
import me.gujun.android.taggroup.TagGroup

interface SaveTagDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment)
}

class SaveTagDialog: DialogFragment() {

    companion object {
        val TITLE = "title"
        val URL = "url"
        val TAGS = "tags"
    }

    var saveTagDialogListener: SaveTagDialogListener? = null
    var saveTagGroup: TagGroup? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        saveTagDialogListener = context as SaveTagDialogListener
        saveTagGroup = activity.findViewById(R.id.save_tag_group) as TagGroup
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val builder = AlertDialog.Builder(activity)
        val url = arguments?.getString(URL)
        val layoutInflater = activity.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.activity_save_tag, null))
                .setTitle("Save Link")
                .setMessage(url)
                .setPositiveButton("Save", DialogInterface.OnClickListener { dialogInterface, i ->
                    saveTagGroup?.tags
                    saveTagDialogListener?.onDialogPositiveClick(this)
                })

        return builder.create()
    }
}