package com.jk.gogit.ui.view.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.jk.gogit.R
import com.jk.gogit.SearchActivity
import com.jk.gogit.model.Repo
import com.jk.gogit.ui.adapters.RepoAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_data.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class SearchRepoFragment : BaseFragment(), AnkoLogger, RepoAdapter.OnViewSelectedListener {

    private lateinit var holdingActivity: SearchActivity

    override fun onItemSelected(txtRepo: TextView, owner: String) {
        redirectToRepoDetails(holdingActivity, owner)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as SearchActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    var query: String = ""
    fun initSearchView(q: String) {
        //val query ="java"// arguments?.getString("query")
        if (!q.isEmpty()) {
            this.query = q
            //val searchText = "%$query%"
            resetPages()
            showLoader(true)
            startSearch(query)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Search Text")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, query)
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
            holdingActivity.analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//                    val subscribeOn = appDatabase.userDao().getUserList(searchText).subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe { listOfPeople ->
//                                updateAdapter(listOfPeople)
//                                debug(listOfPeople.toList().toString())
//                            }
//                    subscriptions.add(subscribeOn)
            // }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = RepoAdapter(this@SearchRepoFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                        pageNumber++
                        loading = true
                        startSearch(query)
                    }
                }
            })

        }
        val query = arguments?.getString("query")
        query?.let {
            initSearchView(query)
        }

    }


    fun startSearch(query: String) {
        if (pageNumber in 1..lastPage) {
            val subscribeOn = holdingActivity.api.searchRepositories(query, "updated", pageNumber, sMaxRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ abc ->
                        run {
                            lastPage = holdingActivity.handlePagination(abc.headers())
                            updateAdapter(abc.body()!!.items!!)
                            loading = false
                        }
                    }, { e ->
                        run {
                            showLoader(false)
                            holdingActivity.onError(e)
                        }
                    }

                    )

            holdingActivity.subscriptions.add(subscribeOn)

        }
    }


    private fun updateAdapter(users: List<Repo>) {
        showLoader(false)
        if (users.isEmpty()) {
            debug("No user")
            return
        } else {
            if (recyclerView?.adapter != null) {
                with(recyclerView?.adapter as RepoAdapter) {
                    if (pageNumber == 1)
                        clearItems()
                    addItems(users)
                }

            }
        }
    }


    fun showLoader(isLoading: Boolean) {
        if (isLoading) {
            recyclerView?.visibility = View.GONE
            progressbar?.visibility = View.VISIBLE

        } else {
            recyclerView?.visibility = View.VISIBLE
            progressbar?.visibility = View.GONE
        }

    }


    /* override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
         super.onCreateOptionsMenu(menu, menuInflater)
         menuInflater.inflate(R.menu.menu, menu)
         val searchView = menu.findItem(R.id.action_search).getActionView() as SearchView
         val searchManager = holdingActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
         searchView.setSearchableInfo(searchManager.getSearchableInfo(holdingActivity.getComponentName()));
         searchView.setIconifiedByDefault(false)
         searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
             override fun onQueryTextSubmit(query: String): Boolean {

                 return false
             }

             override fun onQueryTextChange(s: String): Boolean {
                 return false
             }
         })

     }
 */
}

