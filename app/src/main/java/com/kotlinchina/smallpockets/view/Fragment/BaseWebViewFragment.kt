package com.kotlinchina.smallpockets.view.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.widget.CusTomProgressWebView

/**
 * Created by junjun.
 */
class BaseWebViewFragment : Fragment() {

    var customProgressWebView:CusTomProgressWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //test url
        customProgressWebView?.loadUrl("http://www.baidu.com")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_basewebview,container,false) ;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customProgressWebView = view?.findViewById(R.id.customProgressWebView) as CusTomProgressWebView
    }
}
