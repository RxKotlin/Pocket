package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kotlinchina.smallpockets.service.HttpService
import rx.Observable
import rx.lang.kotlin.observable

class VolleyHttpService constructor(applicationContext: Context): HttpService {
    val applicationContext: Context

    init {
        this.applicationContext = applicationContext
    }

    override fun fetchDataWithUrl(url: String): Observable<String> {
        return observable { subscriber ->
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val stringRequest = StringRequest(url, { response ->
                response.let {
                    subscriber.onNext(it)
                }
            }, { error ->
                error.let {
                    subscriber.onError(Throwable(error.toString()))
                }
            })
            queue.add(stringRequest)
        }
    }
}
