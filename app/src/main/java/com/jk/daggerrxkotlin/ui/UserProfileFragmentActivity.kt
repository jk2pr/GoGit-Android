package com.jk.daggerrxkotlin

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.jk.daggerrxkotlin.ui.adapters.RepoAdapter
import com.jk.daggerrxkotlin.network.api.IApi
import com.jk.daggerrxkotlin.network.api.LoggedInUser
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import com.jk.daggerrxkotlin.extensions.loading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject
import kotlin.jk.com.dagger.R


class UserProfileActivity : BaseActivity(), AnkoLogger {

    @Inject
    lateinit var api: IApi;
    @Inject
    lateinit var appDatabase: AppDatabase;
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
        val loggedInUser = intent.getSerializableExtra("user") as LoggedInUser
        init(loggedInUser)
    }


    fun init(loggedInUser: LoggedInUser) {
        txt_displayname.text = loggedInUser.displayName
        txt_email.text = loggedInUser.email
        image?.loading(loggedInUser.photoUrl)
        recyclerView_repo.apply {
            setHasFixedSize(true)
//            isNestedScrollingEnabled=false
            layoutManager = LinearLayoutManager(context)
            adapter = RepoAdapter(null)

        }

        collapsing_toolbar.title = txt_displayname.text.toString()

        getRepository()


    }

    private fun getRepository() {
        val subs = api.getAllRepository("all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    run {
                        if (recyclerView_repo?.adapter != null) {
                            val adapter = recyclerView_repo?.adapter as RepoAdapter
                            with(adapter) {
                                clearItems()
                                addItems(data)
                            }

                        }

                    }
                },
                        { e ->
                            print(e.message)
                        })
        subscriptions.add(subs)


    }


    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }
}
