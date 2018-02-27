package com.jk.gogit.ui.view

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.R
import com.jk.gogit.application.MyApplication
import com.jk.gogit.db.AppDatabase
import com.jk.gogit.network.api.AccessToken
import com.jk.gogit.network.api.IApi
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var api: IApi
    @Inject
    protected lateinit var mAuth: FirebaseAuth
    @Inject
    protected lateinit var pref:SharedPreferences
    @Inject
    protected lateinit var appDatabase: AppDatabase
    @Inject
    protected lateinit var mFirebaseAnalytics: FirebaseAnalytics

    protected var subscriptions = CompositeDisposable()

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
                pref.edit().clear().apply()
                finish()
                true
            }
            R.id.search -> {
                search.setOnClickListener {
                    name.visibility= View.GONE
                }

                //search.onActionViewExpanded()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}