package com.jk.daggerrxkotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.jk.daggerrxkotlin.api.IApi
import com.jk.daggerrxkotlin.api.LoggedInUser
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import com.jk.daggerrxkotlin.extensions.loading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject
import kotlin.jk.com.dagger.R


class UserProfileFragment : Fragment(), AnkoLogger {

    @Inject
    lateinit var api: IApi;
    @Inject
    lateinit var appDatabase: AppDatabase;
    @Inject
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    lateinit var holdingActivity: MainActivity
    var subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        holdingActivity = activity as MainActivity
        MyApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        retainInstance = true
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loggedInUser = arguments?.getSerializable("user") as LoggedInUser
        txt_displayname.text = loggedInUser.displayName
        txt_email.text = loggedInUser.email
        user_image.loading(loggedInUser.photoUrl)
        getRepository()


    }

    fun getRepository() {
        val curentUser=   FirebaseAuth.getInstance().currentUser
        curentUser?.uid?.let {
            val subs =    api.getAllRepository(it,"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    run {
                        print(data)
                    }
                },
                        { e ->
                            print(e.message)
                        })
            subscriptions.add(subs)
        }

    }


    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }
}
