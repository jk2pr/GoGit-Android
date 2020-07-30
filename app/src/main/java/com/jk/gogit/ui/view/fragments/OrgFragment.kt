package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.model.Org
import com.jk.gogit.ui.adapters.OrgAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.ui.view.UserProfileActivity
import com.jk.gogit.utils.NavUtils.redirectToOrganisation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repo.*
import retrofit2.Response

class OrgFragment : BaseFragment(), OrgAdapter.OnViewSelectedListener {
    lateinit var holdingActivity: UserProfileActivity
    val selectedUser by lazy { arguments?.getString("id") as String }


    override fun onItemSelected(txtRepo: TextView, id: String) {
        redirectToOrganisation(holdingActivity,id)
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
            adapter = OrgAdapter(this@OrgFragment)
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

            val observable: Observable<Response<List<Org>>> = if (holdingActivity.isProfileSameAsLogin(selectedUser))
                holdingActivity.model.getOrgOfUser("N/A", pageNumber, sMaxRecord)
            else
                holdingActivity.model.getOrgOfUser(selectedUser, pageNumber, sMaxRecord)
            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        lastPage = holdingActivity.handlePagination(it.headers())
                        updateUI(it.body()!!)
                        loading = false
                        showLoader(false)
                    }, { e ->
                        loading = false
                        showLoader(false)
                        holdingActivity.onError(e)

                    }, {

                    }))
        }
    }


    private fun updateUI(orgs: List<Org>) {
        val adapter = recyclerView_repo?.adapter as OrgAdapter
        with(adapter) {
            clearItems()
            addItems(orgs)
            // showLoader(false)

        }


    }

}

