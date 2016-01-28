package com.kotlinchina.smallpockets.service

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import rx.Observable
import rx.lang.kotlin.observable

class HttpService constructor(applicationContext: Context) {
    val applicationContext: Context

    init {
        this.applicationContext = applicationContext
    }

    fun fetchDataWithUrl(url: String): Observable<String> {
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