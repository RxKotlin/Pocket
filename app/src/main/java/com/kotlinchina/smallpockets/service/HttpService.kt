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
            val stringRequest = StringRequest(url,
                    object : Response.Listener<String> {
                        override fun onResponse(response: String?) {
                            response.let {
                                subscriber.onNext(it)
                            }
                        }
                    }, object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            error.let {
                                Log.d("TAG", error.toString())
                                subscriber.onError(Throwable(error.toString()))
                            }
                        }
                    })
            queue.add(stringRequest)
        }
    }
}