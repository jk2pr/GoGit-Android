package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.callbacks.OnFilterSelectedListener
import com.jk.gogit.model.Repo
import com.jk.gogit.ui.adapters.RepoAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.ui.view.UserProfileActivity
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repo.*


class RepoFragment : BaseFragment(), RepoAdapter.OnViewSelectedListener, OnFilterSelectedListener {

    private lateinit var holdingActivity : UserProfileActivity
    private val selectedUser by lazy { arguments?.getString("id") as String }
    private val sort = "updated"
    private val type by lazy { holdingActivity.getSelectedFilterType()!! }
    private val index by lazy { arguments?.getInt("index") as Int }

    override fun onItemSelected(txtRepo: TextView, owner: String) {
        redirectToRepoDetails(holdingActivity,owner)


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as UserProfileActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo, container, false)
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_repo?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_repo.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = RepoAdapter(this@RepoFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_repo.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_repo.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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
            val observable = if (index == 0)
                holdingActivity.model.getRepository(type.toString(), selectedUser, sort, pageNumber, sMaxRecord)
            else
                holdingActivity.model.getStarredRepository(selectedUser, sort, pageNumber, sMaxRecord)

            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        lastPage = holdingActivity.handlePagination(it.headers())
                        showLoader(false)
                        loading = false
                        /* if (it.body()!!.isEmpty())
                             holdingActivity.showSnackForNoData()*/
                        updateUI(it.body()!!)

                    }, { e ->
                        e.printStackTrace()
                        showLoader(false)
                        loading = false
                        holdingActivity.onError(e)

                    }, {

                    }))
        }
    }

    override fun onFilterSelected(name: String) {
        type.delete(0, type.length)
        type.append(name)
        resetPages()
        loadPage()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded)
            if (isVisibleToUser)
                if (index == 0) holdingActivity.showFab(true)
                else holdingActivity.showFab(false)
            else
                holdingActivity.showFab(false)
    }


    private fun updateUI(repos: List<Repo>) {
        val adapter = recyclerView_repo?.adapter as RepoAdapter
        with(adapter) {
            if (pageNumber == 1)
                clearItems()
            addItems(repos)
        }
    }
}




