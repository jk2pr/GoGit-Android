package com.jk.daggerrxkotlin

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import com.jk.daggerrxkotlin.application.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.jk.com.dagger.R



class MainActivity : AppCompatActivity(), DataFragment.OnFragmentInteractionListener {

   lateinit var searchView:SearchView;
    override fun onFragmentInteraction(uri: Uri) {
        println("uri = [${uri}]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.appComponent.inject(this);
        val toolbar = toolbar as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        searchView=search



    }

}
