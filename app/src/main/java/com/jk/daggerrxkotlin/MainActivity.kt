package com.jk.daggerrxkotlin

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.SearchView
import com.jk.daggerrxkotlin.application.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.jk.com.dagger.R



class MainActivity : BaseActivity(), DataFragment.OnFragmentInteractionListener {

   lateinit var searchView:SearchView;
    override fun onFragmentInteraction(uri: Uri) {
        println("uri = [${uri}]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this);
        searchView=search
        val profileFragment=UserProfileFragment()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentView,profileFragment)
        fragmentTransaction.commit()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

}
