package com.jk.daggerrxkotlin

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.SearchView
import com.google.firebase.auth.FirebaseUser
import com.jk.daggerrxkotlin.api.LoggedInUser
import com.jk.daggerrxkotlin.application.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.jk.com.dagger.R
import kotlin.jk.com.dagger.R.id.search


class MainActivity : BaseActivity(), DataFragment.OnFragmentInteractionListener {

   lateinit var searchView:SearchView;
    override fun onFragmentInteraction(uri: Uri) {
        println("uri = [${uri}]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this);
        searchView=search
       // val profileFragment=UserProfileFragment()
        //val arg =Bundle()
        //arg.putSerializable("user",intent.extras.getSerializable("user"))
       // profileFragment.arguments=arg

       // val fragmentTransaction=supportFragmentManager.beginTransaction()
       // fragmentTransaction.replace(R.id.contentView,profileFragment)
       // fragmentTransaction.commit()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

}
