package com.jk.gogit.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.jk.gogit.application.MyApplication
import com.jk.gogit.model.Feed
import com.jk.gogit.model.UserProfile
import com.jk.gogit.network.api.IApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Jitendra Prajapati (jk2praj@gmail.com) on 02/03/2018 (MM/DD/YYYY )
 */
class FeedModel: ViewModel() {
    init {
        MyApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var api: IApi


}