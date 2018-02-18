package com.jk.daggerrxkotlin.ui.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import com.jk.daggerrxkotlin.extensions.loading
import com.jk.daggerrxkotlin.model.Repo
import com.jk.daggerrxkotlin.model.UserProfile
import com.jk.daggerrxkotlin.network.api.IApi
import com.jk.daggerrxkotlin.ui.adapters.RepoAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject
import kotlin.jk.com.dagger.R


class UserProfileActivity : BaseActivity(), AnkoLogger {

    @Inject
    lateinit var api: IApi
    @Inject
    lateinit var appDatabase: AppDatabase
    @Inject
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    lateinit var holdingActivity: MainActivity
    var subscriptions = CompositeDisposable()

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this)
        showLoader(true)
        recyclerView_repo.apply {
            setHasFixedSize(true)
//            isNestedScrollingEnabled=false
            layoutManager = LinearLayoutManager(context)
            adapter = RepoAdapter(null)

        }
        val sub = Observable.zip<UserProfile, List<Repo>, MutableMap<UserProfile, List<Repo>>>(api.getUserProfile(),
                api.getAllRepository("all"),
                BiFunction<UserProfile, List<Repo>, MutableMap<UserProfile, List<Repo>>>
                { k, f ->
                    merge(k, f)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateUI(it)
                }, { e ->
                    e.printStackTrace()
                }, {
                    print("ONComplete")
                })
        subscriptions.add(sub)
    }

    fun updateUI(data: MutableMap<UserProfile, List<Repo>>) {
        showLoader(false)
        for ((key, value) in data) {
            txt_displayname.text = key.name
            txt_email.text = key.email
            profile?.loading(key.avatarUrl)
            if (recyclerView_repo?.adapter != null) {
                val adapter = recyclerView_repo?.adapter as RepoAdapter
                with(adapter) {
                    clearItems()
                    addItems(value)
                    showLoader(false)
                }
            }
        }
    }

    fun merge(loggedInUser: UserProfile, repo: List<Repo>): MutableMap<UserProfile, List<Repo>> {
        val data: MutableMap<UserProfile, List<Repo>> = mutableMapOf()
        data.put(loggedInUser, repo)
        return data
    }


    fun showLoader(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                progressbar.visibility = View.VISIBLE
                parentConstraintUsrProfile.visibility = View.GONE
            }
            false -> {
                progressbar.visibility = View.GONE
                parentConstraintUsrProfile.visibility = View.VISIBLE
            }
        }

    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }
}
