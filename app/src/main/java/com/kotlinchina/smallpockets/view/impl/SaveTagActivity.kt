package com.kotlinchina.smallpockets.view.impl

import android.app.Activity
import android.content.Intent
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
            fun backActivity() {
                fun intentWithData(): Intent? {
                    val title = titleTV?.text
                    val url = linkTV?.text
                    val tags = saveTagGroup?.tags

                    if (title != null
                            && url != null
                            && tags != null) {
                        val intent = Intent()
                        intent.putExtra(TITLE, title)
                        intent.putExtra(URL, url)
                        intent.putExtra(TAGS, tags)
                        return intent
                    }
                    return null
                }

                val intent = intentWithData()
                if (intent != null) {
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

            saveButton = findViewById(R.id.save_button) as? Button
            saveButton?.setOnClickListener {
                backActivity()
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

        //测试添加的tag
        saveTagGroup?.setTags(arrayListOf("tag1", "tag2"))
    }
}
