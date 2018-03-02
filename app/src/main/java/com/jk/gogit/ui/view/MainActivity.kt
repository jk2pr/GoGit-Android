package com.jk.gogit.ui.view

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.SearchView
import com.jk.gogit.R
import com.jk.gogit.application.MyApplication
import com.jk.gogit.ui.view.fragments.FeedFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {

    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeDefaultToolbar()
        if (savedInstanceState == null) {
            var feedFragment = supportFragmentManager.findFragmentByTag(FeedFragment::class.java.simpleName)
            if (feedFragment == null) {
                feedFragment = FeedFragment()
                supportFragmentManager.beginTransaction().add(R.id.contentView, feedFragment, FeedFragment::class.java.simpleName).commit()
            }
        }
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

}
