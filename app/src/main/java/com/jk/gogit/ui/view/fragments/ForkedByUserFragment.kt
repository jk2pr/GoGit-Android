package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.model.RepoForked
import com.jk.gogit.ui.adapters.ForkedByUserAdapter
import com.jk.gogit.ui.view.AboutRepoActivity
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_users.*


class ForkedByUserFragment : BaseFragment(), ForkedByUserAdapter.OnViewSelectedListener {

    override fun onItemSelected(txtRepo: TextView, owner: String?) {
        redirectToProfile(holdingActivity,owner)
    }


    lateinit var holdingActivity: AboutRepoActivity
    // val it: RepositoryDetails by lazy { Gson().fromJson(arguments?.getString("data"), RepositoryDetails::class.java) }
    val owner by lazy { arguments?.getString("owner")!! }
    val repo by lazy { arguments?.getString("repoName")!! }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as AboutRepoActivity
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_followers?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_followers.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = ForkedByUserAdapter(this@ForkedByUserFragment)
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
        if (pageNumber in 1..lastPage) {
            if (pageNumber == 1)
                showLoader(true)
            val observable = holdingActivity.model.getForkedRepo(owner,
                    repo, pageNumber, BaseActivity.sMaxRecord)
            holdingActivity.subscriptions.add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        lastPage = holdingActivity.handlePagination(it.headers())
                        updateUI(it.body()!!)
                        showLoader(false)
                    }, { e ->

                        showLoader(false)
                        holdingActivity.onError(e)
                    }, {
                        showLoader(false)

                    }))

        }
    }

    private fun updateUI(repos: List<RepoForked>) {
        if (recyclerView_followers?.adapter != null)
            with(recyclerView_followers?.adapter as ForkedByUserAdapter) {
                if (pageNumber == 1)
                    clearItems()
                addItems(repos)
                // showLoader(false)

            }


    }


}



