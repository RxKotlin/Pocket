package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import com.evernote.client.android.EvernoteUtil
import com.evernote.client.android.asyncclient.EvernoteCallback
import com.evernote.edam.type.Note
import com.kotlinchina.smallpockets.application.PocketApplication
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.CoreLink
import com.kotlinchina.smallpockets.service.StoreService
import rx.Observable
import rx.lang.kotlin.observable

class EvernoteStoreService(application: Context): StoreService {
    private val application: Context
    init {
        this.application = application
    }
    override fun store(title: String, content: String): Observable<Link> {
        return observable {
            val everNoteSession = (application as? PocketApplication)?.everNoteSession
            val note = Note()
            note.title = title
            note.content = EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX
            everNoteSession?.evernoteClientFactory?.noteStoreClient?.createNoteAsync(note, object: EvernoteCallback<Note> {
                override fun onException(error: Exception?) {
                    it.onError(error)
                }

                override fun onSuccess(note: Note?) {
                    val title = note?.title
                    if (title != null) {
                        val link = CoreLink(title , "", null)
                        it.onNext(link)
                        it.onCompleted()
                    }

                    it.onError(Exception("the evernote title do not exit"))
                }
            })
        }
    }
}