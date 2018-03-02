package com.jk.gogit.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.jk.gogit.application.MyApplication
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.model.Users
import com.jk.gogit.network.api.IApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserViewModel : ViewModel() {
    init {
        MyApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var api: IApi
    fun getUser(userId: String): Observable<FinalData> = Observable
            .zip<UserProfile,
                    List<Repo>,
                    List<Users>,
                    List<Users>,
                    FinalData>(api.getUserProfile(userId),
                    api.getAllRepository("all"),
                    api.getFollowing(),
                    api.getFollowers(), Function4<UserProfile, List<Repo>, List<Users>, List<Users>, FinalData>
            { j, k, l, m ->
                merge(j, k, l, m)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun merge(loggedInUser: UserProfile,
                      repo: List<Repo>,
                      followers: List<Users>,
                      followings: List<Users>): FinalData {
        val data = FinalData()

        data.userData = loggedInUser
        data.repo = repo
        data.followers = followers
        data.following = followings

        return data
    }


    class FinalData {
        lateinit var userData: UserProfile
        lateinit var repo: List<Repo>
        lateinit var followers: List<Users>
        lateinit var following: List<Users>

    }
}