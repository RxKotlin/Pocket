package com.kotlinchina.smallpockets.view.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment

import com.kotlinchina.smallpockets.model.Link

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by junjun.
 */
class MyFragment : Fragment() {


    internal var datas: List<Link> = ArrayList()

    companion object {


        fun newInstance(url: String): MyFragment {

            val args = Bundle()
            args.putString("url", url)

            val fragment = MyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
