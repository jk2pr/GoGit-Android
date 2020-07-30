package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.ui.adapters.UserAdapter
import com.jk.gogit.ui.view.AboutRepoActivity
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contributors.*

class ContributorsFragment : BaseFragment(), UserAdapter.OnViewSelectedListener {


    lateinit var holdingActivity: AboutRepoActivity
    //val it: RepositoryDetails by lazy { Gson().fromJson(arguments?.getString("data"), RepositoryDetails::class.java) }
    val repo by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("owner") }


    override fun onItemSelected(login: String) {
        redirectToProfile(holdingActivity,login)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as AboutRepoActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contributors, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_contributors.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = UserAdapter(this@ContributorsFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_contributors.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_contributors.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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
        val model = holdingActivity.model
        if (pageNumber in 1..lastPage) {
            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(model.getContributors(owner!!,
                    repo!!,
                    true,
                    pageNumber, BaseActivity.sMaxRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showLoader(false)
                        lastPage = holdingActivity.handlePagination(it.headers())
                        (recyclerView_contributors.adapter as UserAdapter).apply {
                            if (pageNumber == 1)
                                clearItems()
                            addItems(it.body()!!)
                            loading = false
                        }
                    }, {
                        showLoader(false)
                        holdingActivity.onError(it)

                    }, {

                    }))
        }
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_contributors?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

}

