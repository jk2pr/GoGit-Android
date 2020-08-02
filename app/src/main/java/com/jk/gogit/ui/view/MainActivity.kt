package com.jk.gogit.ui.view

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.jk.gogit.R
import com.jk.gogit.db.AppDatabase
import com.jk.gogit.model.UserProfile
import com.jk.gogit.network.api.IApi
import com.jk.gogit.network.api.ILogin
import com.jk.gogit.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins.onError
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.Headers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var api: IApi

    @Inject
    lateinit var loginApi: ILogin

    @Inject
    protected lateinit var mAuth: FirebaseAuth

    @Inject
    protected lateinit var pref: SharedPreferences

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var firebaseAnalytics
            : FirebaseAnalytics

    var subscriptions = CompositeDisposable()
    val model: UserViewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }

    companion object {

        private var sUser: UserProfile? = null

        // The minimum amount of items to have below your current scroll position
        // before loading more.
        const val VISIBLE_THRESHOLD = 1 //5
        var sMaxRecord = 50
        var isNotificationAvailable = false

    }

    private fun setUpToolbar() {
        if (toolbar != null) {
            val toolbar = toolbar as androidx.appcompat.widget.Toolbar
            toolbar.elevation = 0.0f
            //findOptional<AppBarLayout>(R.id.app_bar_layout)?.setPadding(0, getStatusBarHeight(), 0, 0)
            //toolbar.visibility = View.VISIBLE
            toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_overflow_white)
            //toolbar_title.text = getText(R.string.app_name)
            setSupportActionBar(toolbar)
        }
    }

    fun getLoginData(): UserProfile? {
        if (sUser == null)
            sUser = Gson().fromJson(pref.getString("UserData", null), UserProfile::class.java)
        return sUser
    }

    fun save(user: UserProfile) {
        sUser = null
        pref.edit().putString("UserData", Gson().toJson(user)).apply()

    }

    fun handlePagination(headers: Headers?): Int {

        try {
            if (headers == null)
                return 1
            val list = headers.get("Link")?.split(",")
            val map = list?.associateBy(
                    {
                        it.split(";")[1].split("=")[1].replace("\"", "")

                    }, {
                it.split(";")[0]
            })
            val url = map?.get("last")?.trim()?.removeSurrounding("<", ">")
            //return url?.substring(url.lastIndexOf("=") + 1, url.length - 1)?.toInt() ?: 0

            url?.let {
                val uri = Uri.parse(it)
                return uri.getQueryParameter("page")!!.toInt()
            }
            return 1

        } catch (e: Exception) {
            return 1
        }
    }

    fun enableHomeInToolBar(title: String?, isUpEnable: Boolean) {
        supportActionBar?.apply {
            setHomeButtonEnabled(isUpEnable)
            setDisplayHomeAsUpEnabled(isUpEnable)
            toolbar_title.text = title
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableHomeInToolBar(resources.getString(R.string.app_name), false)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        subscriptions.add(model.getNotifications(false, 0, sMaxRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isNotificationAvailable = it.body()!!.isEmpty() == false
                    super.onPrepareOptionsMenu(menu)
                }, {
                    onError(it)
                    it.printStackTrace()
                }))

        return true
    }


    /*private fun resetPages() {
        lastPage = 1 //Will updated after first hit
        lastVisibleItem = 0
        totalItemCount = 0
        loading = false
        pageNumber = 1
    }*/
}
