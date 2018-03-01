package com.jk.gogit.ui.view

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.adapters.RepoAdapter
import com.jk.gogit.ui.view.fragments.FollowersFragment
import com.jk.gogit.ui.view.fragments.FollowingFragment
import com.jk.gogit.ui.view.fragments.RepoFragment
import com.jk.gogit.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.view.*
import org.jetbrains.anko.AnkoLogger
import java.util.*


class UserProfileActivity : BaseActivity(), AnkoLogger {

    var data: MutableMap<UserProfile, List<Repo>> = mutableMapOf()
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        container?.adapter = PageAdapter(supportFragmentManager, this)
        container?.offscreenPageLimit=3
        tabs?.setupWithViewPager(container)

    }
    private fun updateProfile(userProfile: UserProfile) {
        profile?.loading(userProfile.avatarUrl)
        txt_displayname.text = userProfile.name
        txt_loginame?.text = userProfile.login
        txt_bio?.text = userProfile.bio
        txt_company?.text = userProfile.company
        txt_location?.text = userProfile.location
        txt_email?.text = userProfile.email
        txt_blog?.text = userProfile.blog

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
        //  val value = savedInstanceState?.getSerializable("List") as List<Repo>
        //  val profile = savedInstanceState.getString("Profile")
        //  val userProfile = Gson().fromJson(profile, UserProfile::class.java)
        //  data[userProfile] = value
    }

    override fun onResume() {
        super.onResume()
        val model = ViewModelProviders.of(this).get(UserViewModel::class.java)
        showLoader(true)
        subscriptions.add(model.getUser().subscribe({
            // data=it
            showLoader(false)
            updateProfile(it.userData)
            val adapter = container?.adapter as PageAdapter
            val f0 = adapter.registeredFragments[0] as RepoFragment
            f0.updateUI(it.repo)
            val f1 = adapter.registeredFragments[1] as FollowersFragment
            f1.updateAdapter(it.followers)
            val f2 = adapter.registeredFragments[2] as FollowingFragment
            f2.updateAdapter(it.following)

            //  updateUI(it)
        }, { e ->
            e.printStackTrace()
        }, {
            print("ONComplete")
        }))
        if (data.isNotEmpty()) {
            // updateUI(data)
        }
    }


    /*private fun updateUI(data: MutableMap<UserProfile, List<Repo>>) {
        showLoader(false)
        //progressbar.tag = data
       this.data=data
        for ((key, value) in data) {
          //  name.text = key.name

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
    }*/


    private fun showLoader(isLoading: Boolean) {

          progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
          parentConstraintUsrProfile?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
           print("SHowingloader       -------------" + isLoading)


    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
        subscriptions.dispose()
    }

    class PageAdapter(fm: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fm) {

        var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 ->
                    RepoFragment()
                1 ->
                    FollowersFragment()
                2 ->
                    FollowingFragment()
                else ->
                    null

            }

        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment

        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 ->
                    "Repositiories"
                1 ->
                    "Followers"
                2 ->
                    "Followings"
                else ->
                    null

            }
        }

    }
}
