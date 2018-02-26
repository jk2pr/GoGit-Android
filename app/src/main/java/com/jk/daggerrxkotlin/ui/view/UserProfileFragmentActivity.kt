package com.jk.daggerrxkotlin.ui.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.jk.daggerrxkotlin.extensions.loading
import com.jk.daggerrxkotlin.model.Repo
import com.jk.daggerrxkotlin.model.UserProfile
import com.jk.daggerrxkotlin.ui.adapters.RepoAdapter
import com.jk.daggerrxkotlin.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import java.util.ArrayList
import kotlin.jk.com.dagger.R


class UserProfileActivity : BaseActivity(), AnkoLogger {

    var data: MutableMap<UserProfile, List<Repo>> = mutableMapOf()
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            showLoader(true)
            recyclerView_repo.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = RepoAdapter(null)

            }
            val model = ViewModelProviders.of(this).get(UserViewModel::class.java)
            subscriptions.add(model.getUser().subscribe({
              // data=it
                updateUI(it)
            }, { e ->
                e.printStackTrace()
            }, {
                print("ONComplete")
            })
            )

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val gson = Gson()
        val data = progressbar.tag as MutableMap<UserProfile, List<Repo>>
        for ((key, value) in data) {
            outState?.putSerializable("List", value as ArrayList<Repo>)
            outState?.putString("Profile", gson.toJson(key))
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val value = savedInstanceState?.getSerializable("List") as List<Repo>
        val profile = savedInstanceState.getString("Profile")
        val userProfile = Gson().fromJson(profile, UserProfile::class.java)
        data.put(userProfile, value)
    }

    override fun onResume() {
        super.onResume()
        if (data.size>0)
        updateUI(data)
    }
    fun updateUI(data: MutableMap<UserProfile, List<Repo>>) {
        showLoader(false)
        progressbar.tag = data
        for ((key, value) in data) {
            name.text = key.name
            txt_email.text = key.email
            profile?.loading(key.avatarUrl)
            //profile?.tag = key.avatarUrl
            followers_count.text = "Following ${key.followers.toString()}"
            followering_count.text = "Followers ${key.following.toString()}"
            repo_count.text = "Repositories ${key.publicRepos.toString()}"
            if (recyclerView_repo?.adapter==null)
                recyclerView_repo.adapter=RepoAdapter(null)
                val adapter = recyclerView_repo?.adapter as RepoAdapter
                with(adapter) {
                    clearItems()
                    addItems(value)
                    showLoader(false)
                }

        }
    }


    private fun showLoader(isLoading: Boolean) {

        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        parentConstraintUsrProfile.visibility = if (isLoading) View.GONE else View.VISIBLE
        print("SHowingloader       -------------" + isLoading)


    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
        subscriptions.dispose()
    }
}
