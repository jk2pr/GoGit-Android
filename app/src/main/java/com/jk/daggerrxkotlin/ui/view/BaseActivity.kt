package com.jk.daggerrxkotlin.ui.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import kotlinx.android.synthetic.main.activity_user_profile.*
import javax.inject.Inject
import kotlin.jk.com.dagger.R


abstract class BaseActivity : AppCompatActivity() {
    @Inject
    protected lateinit var mAuth: FirebaseAuth
    @Inject
    protected lateinit var appDatabase: AppDatabase
    @Inject
    protected lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this)
        setContentView(getLayoutResourceId())
        setUpToolbar()
    }


    abstract fun getLayoutResourceId(): Int
    private fun setUpToolbar() {
        if (toolbar != null) {
            val toolbar = toolbar as android.support.v7.widget.Toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHomeButtonEnabled(false)
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.action_logout -> {
                mAuth.signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}