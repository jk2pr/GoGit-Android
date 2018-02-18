package com.jk.daggerrxkotlin.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.model.Repo
import com.jk.daggerrxkotlin.model.UserProfile
import com.jk.daggerrxkotlin.network.api.IApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserViewModel : ViewModel() {
    init {
        MyApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var api: IApi
    lateinit var userProfile: UserProfile
    lateinit var list: List<Repo>
    fun getUser(): Observable<MutableMap<UserProfile, List<Repo>>> = Observable.zip<UserProfile, List<Repo>, MutableMap<UserProfile, List<Repo>>>(api.getUserProfile(),
            api.getAllRepository("all"),
            BiFunction<UserProfile, List<Repo>, MutableMap<UserProfile, List<Repo>>>
            { k, f ->
                merge(k, f)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    // return sub


    private fun merge(loggedInUser: UserProfile, repo: List<Repo>): MutableMap<UserProfile, List<Repo>> {
        val data: MutableMap<UserProfile, List<Repo>> = mutableMapOf()
        data.put(loggedInUser, repo)
        return data
    }
}