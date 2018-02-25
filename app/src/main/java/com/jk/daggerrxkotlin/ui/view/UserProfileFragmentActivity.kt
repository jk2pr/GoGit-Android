package com.jk.daggerrxkotlin.ui.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jk.daggerrxkotlin.extensions.loading
import com.jk.daggerrxkotlin.model.Repo
import com.jk.daggerrxkotlin.model.UserProfile
import com.jk.daggerrxkotlin.ui.adapters.RepoAdapter
import com.jk.daggerrxkotlin.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.jetbrains.anko.AnkoLogger
import kotlin.jk.com.dagger.R


class UserProfileActivity : BaseActivity(), AnkoLogger {


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
            updateUI(it)
        }, { e ->
            e.printStackTrace()
        }, {
            print("ONComplete")
        })
        )

    }

    fun updateUI(data: MutableMap<UserProfile, List<Repo>>) {
        showLoader(false)
        for ((key, value) in data) {
            txt_displayname.text = key.name
            txt_email.text = key.email
            profile?.loading(key.avatarUrl)
            followers_count.text = "Following ${key.followers.toString()}"
            followering_count.text = "Followers ${key.following.toString()}"
            repo_count.text = "Repositories ${key.publicRepos.toString()}"
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


  private  fun showLoader(isLoading: Boolean) {

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
