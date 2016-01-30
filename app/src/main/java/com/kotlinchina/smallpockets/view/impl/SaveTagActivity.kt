package com.kotlinchina.smallpockets.view.impl

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kotlinchina.smallpockets.R
import me.gujun.android.taggroup.TagGroup

class SaveTagActivity : Activity() {

    companion object {
        val TITLE = "title"
        val URL = "url"
        val TAGS = "tags"
    }

    var saveButton: Button? = null
    var linkTV: TextView? = null
    var titleTV: TextView? = null
    var saveTagGroup: TagGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_tag)
        setupView()
    }

    private fun setupView() {
        fun setupSaveButton() {
            fun saveTags() {
                val tags = saveTagGroup?.tags
                if (tags != null) {
                    val bundle = Bundle()
                    bundle.putStringArray(TAGS, tags)
                    intent.putExtras(bundle)

                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

            saveButton = findViewById(R.id.save_button) as? Button
            saveButton?.setOnClickListener {
                saveTags()
            }
        }

        fun setupLinkTV() {
            linkTV = findViewById(R.id.link_tv) as? TextView
            val url = intent.getStringExtra(URL)
            linkTV?.text = url
        }

        fun setupTitleTV() {
            titleTV = findViewById(R.id.title_tv) as? TextView
            val title = intent.getStringExtra(TITLE)
            titleTV?.text = title
        }

        setupSaveButton()
        setupLinkTV()
        setupTitleTV()
        saveTagGroup = findViewById(R.id.save_tag_group) as? TagGroup
    }
}
