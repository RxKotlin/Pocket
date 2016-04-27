package com.kotlinchina.smallpockets.service.impl

import com.evernote.client.android.EvernoteSession
import com.evernote.client.android.EvernoteUtil
import com.evernote.client.android.asyncclient.EvernoteCallback
import com.evernote.edam.type.Note
import com.kotlinchina.smallpockets.service.StoreService
import rx.Observable
import rx.lang.kotlin.observable

class EvernoteStoreService(evernoteSession: EvernoteSession): StoreService {
    private val evernoteSession: EvernoteSession
    init {
        this.evernoteSession = evernoteSession
    }
    override fun store(title: String, content: String): Observable<String> {
        return observable {
            val note = Note()
            note.title = title
            note.content = EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX

            evernoteSession.evernoteClientFactory?.noteStoreClient?.createNoteAsync(note, object: EvernoteCallback<Note> {
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
}