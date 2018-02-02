package com.jk.daggerrxkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.jk.com.dagger.R
import kotlin.jk.com.dagger.R.layout.toolbar


abstract class BaseActivity :AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        setUpToolbar()
    }

   abstract fun getLayoutResourceId(): Int
   private fun setUpToolbar(){
        val toolbar = toolbar as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
           setDisplayHomeAsUpEnabled(false)
        }

    }

  override  fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


}