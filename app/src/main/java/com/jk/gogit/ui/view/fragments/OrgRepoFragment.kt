package com.jk.gogit.ui.view.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.OrgProfileActivity
import com.jk.gogit.R
import com.jk.gogit.callbacks.OnFilterSelectedListener
import com.jk.gogit.model.Repo
import com.jk.gogit.ui.adapters.RepoAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repo.*


class OrgRepoFragment : BaseFragment(), RepoAdapter.OnViewSelectedListener, OnFilterSelectedListener {

    private val type by lazy { holdingActivity.getSelectedFilterType()!! }
    lateinit var holdingActivity :OrgProfileActivity
    val selectedUser by lazy { arguments?.getString("id")!! }

    override fun onItemSelected(txtRepo: TextView, owner: String) {
        redirectToRepoDetails(holdingActivity,owner)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity=activity as OrgProfileActivity
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
            adapter = RepoAdapter(this@OrgRepoFragment)

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

    override fun onFilterSelected(name: String) {
        type.delete(0, type.length)
        type.append(name)
        resetPages()
        loadPage()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded) {
            if (isVisibleToUser)
                holdingActivity.showFab(true)
            else holdingActivity.showFab(false)
        }
    }

    fun loadPage() {
        if (pageNumber in 1..lastPage) {

            val observable = holdingActivity.model.getOrgRepository(selectedUser,type.toString(), pageNumber, sMaxRecord)
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

                    }))
        }
    }

    private fun updateUI(repos: List<Repo>) {
        with(recyclerView_repo?.adapter as RepoAdapter) {
            if (pageNumber == 1)
                clearItems()
            addItems(repos)
        }


    }

}




