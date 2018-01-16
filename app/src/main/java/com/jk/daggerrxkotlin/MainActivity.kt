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


       // toolbar.setBackgroundColor(resources.getColor())
       //  supportActionBar?.setNavigationIcon(R.mipmap.ic_launcher_foreground)
      //  supportActionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher_foreground)

       // val dataFragment = DataFragment();
      //  changeFragment(dataFragment)
    }

//    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
//        val ft = supportFragmentManager.beginTransaction();
//        ft.setCustomAnimations(
//                R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);
//        ft.replace(R.id.activity_base_content, f);
//        ft.addToBackStack(f.javaClass.simpleName);
//        ft.commit();
//    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

//        val searchViewMenuItem = menu!!.findItem(R.id.search)
//        val searchView = searchViewMenuItem.actionView as SearchView
//        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
//        searchView.setLayoutParams(params)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {


        return super.onPrepareOptionsMenu(menu)
     //   val v = searchView.findViewById(android.support.v7.appcompat.R.id.search_button) as ImageView
    }*/
}
