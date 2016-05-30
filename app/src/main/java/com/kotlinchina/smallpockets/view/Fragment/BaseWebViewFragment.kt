package com.kotlinchina.smallpockets.view.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlinchina.smallpockets.R
import com.kotlinchina.smallpockets.widget.CustomProgressWebView

/**
 * Created by junjun.
 */
class BaseWebViewFragment : Fragment() {

    companion object {
        fun newInstance(url: String?): BaseWebViewFragment {
            val args = Bundle()
            args.putString("url", url)
            val fragment = BaseWebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var customProgressWebView: CustomProgressWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val perItemUrl: String? = arguments.get("url") as? String
        customProgressWebView?.loadUrl(perItemUrl)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_basewebview,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customProgressWebView = view?.findViewById(R.id.customProgressWebView) as? CustomProgressWebView
    }
}
