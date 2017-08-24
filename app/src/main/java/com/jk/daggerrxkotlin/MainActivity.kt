package com.jk.daggerrxkotlin

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jk.daggerrxkotlin.application.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.jk.com.daggerrxkotlin.R

class MainActivity : AppCompatActivity(), DataFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        println("uri = [${uri}]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.appComponent.inject(this);
        val toolbar = toolbar as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(resources.getColor(R.color.colorAccent));
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
}
