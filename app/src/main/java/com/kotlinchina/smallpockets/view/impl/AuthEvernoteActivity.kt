package com.kotlinchina.smallpockets.view.impl

import android.app.Activity
import com.evernote.client.android.EvernoteSession
import com.evernote.client.android.EvernoteUtil
import com.evernote.client.android.asyncclient.EvernoteCallback
import com.evernote.client.android.login.EvernoteLoginFragment
import com.evernote.edam.type.Note
import com.kotlinchina.smallpockets.service.ShareService
import rx.Observable
import rx.lang.kotlin.observable

class AuthEvernoteActivity : Activity(), EvernoteLoginFragment.ResultCallback, ShareService {
    private val session: EvernoteSession

    constructor(session: EvernoteSession) {
        this.session = session
    }

    override fun share(title: String, content: String): Observable<String> {
        return observable {
            val note = Note()
            note.title = title
            note.content = EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX

            session.evernoteClientFactory?.noteStoreClient?.createNoteAsync(note, object : EvernoteCallback<Note> {
                override fun onException(error: Exception?) {
                    it.onError(error)
                }

                override fun onSuccess(note: Note?) {
                    it.onNext("Save success")
                    it.onCompleted()
                }
            })
        }
    }

    override fun onLoginFinished(successful: Boolean) {

    }
}
