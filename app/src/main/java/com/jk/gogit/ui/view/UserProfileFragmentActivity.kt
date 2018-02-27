package com.jk.gogit.ui.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.adapters.RepoAdapter
import com.jk.gogit.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.AnkoLogger
import java.util.ArrayList


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
        data[userProfile] = value
    }

    override fun onResume() {
        super.onResume()
        if (data.isNotEmpty())
            updateUI(data)
    }


    private fun updateUI(data: MutableMap<UserProfile, List<Repo>>) {
        showLoader(false)
        //progressbar.tag = data
       this.data=data
        for ((key, value) in data) {
            name.text = key.name
            txt_email.text = key.email
            profile?.loading(key.avatarUrl)
            //profile?.tag = key.avatarUrl
            followers_count.text = "Following ${key.followers}"
            followering_count.text = "Followers ${key.following}"
            repo_count.text = "Repositories ${key.publicRepos}"
            if (recyclerView_repo?.adapter == null)
                recyclerView_repo.adapter = RepoAdapter(null)
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
