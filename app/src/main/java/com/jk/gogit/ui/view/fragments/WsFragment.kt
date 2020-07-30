package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.model.Users
import com.jk.gogit.ui.adapters.UserAdapter
import com.jk.gogit.ui.view.AboutRepoActivity
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_users.*

class WsFragment : BaseFragment(), UserAdapter.OnViewSelectedListener {
    private lateinit var holdingActivity: AboutRepoActivity     // val it: RepositoryDetails by lazy { Gson().fromJson(arguments?.getString("data"), RepositoryDetails::class.java) }

    private val index by lazy { arguments?.getInt("index") as Int }
    val owner by lazy { arguments?.getString("owner") as String }
    val repo by lazy { arguments?.getString("repoName")!! }

    // private var adapter: UserAdapter = UserAdapter(this)
    override fun onItemSelected(login: String) {
        redirectToProfile(holdingActivity,login)
        // startActivity(holdingActivity.intentFor<UserProfileActivity>(("id" to login)))
        // (activity as RepoDetailsActivity).updateUser(login)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as AboutRepoActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_followers.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = UserAdapter(this@WsFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_followers.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_followers.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                        pageNumber++
                        loading = true
                        loadPage()
                    }
                }
            })

        }
        loadPage()
    }

    fun loadPage() {
        val observable = when (index) {
            1 -> //Watch
                holdingActivity.model.getWatchersOfRepo(owner,
                        repo, pageNumber, BaseActivity.sMaxRecord)
            else -> // Stars
                holdingActivity.model.getStargazersOfRepo(owner,
                        repo, pageNumber, BaseActivity.sMaxRecord)
        }

        if (pageNumber in 1..lastPage) {
            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        lastPage = holdingActivity.handlePagination(it.headers())
                        updateAdapter(it.body()!!)

                    }, { e ->
                        showLoader(false)
                        e.printStackTrace()
                        loading = false
                        holdingActivity.onError(e)


                    }, {
                        showLoader(false)
                        loading = false
                    }))
        }
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_followers?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    private fun updateAdapter(data: List<Users>) {
        with(recyclerView_followers.adapter as UserAdapter)
        {
            if (pageNumber == 1)
                clearItems()
            addItems(data)
            // showLoader(false)

        }
    }
}
