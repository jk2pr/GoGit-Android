package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.SearchActivity
import com.jk.gogit.model.search.User
import com.jk.gogit.ui.adapters.DataAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.utils.NavUtils.redirectToOrganisation
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_data.*
import org.jetbrains.anko.AnkoLogger


class SearchUserFragment : BaseFragment(), AnkoLogger, DataAdapter.onViewSelectedListener {

    private lateinit var holdingActivity: SearchActivity
    override fun onItemSelected(user: User?) {
        if (user?.type!!.toLowerCase().contentEquals("user"))
            redirectToProfile(holdingActivity, user.login)
        else
            redirectToOrganisation(holdingActivity, user.login)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = DataAdapter(this@SearchUserFragment)
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
    }

    var query: String = ""
    fun initSearchView(q: String) {
        if (!q.isEmpty()) {
            this.query = q
            showLoader(true)
            //   val searchText = "%$query%"
            resetPages()
            startSearch(q)

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

    /* private fun showAllFromLocalDb() {
         subscriptions.clear()
         val subscribeOn = appDatabase.userDao().getAllPeople().subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe({ listOfPeople ->
                     updateAdapter(listOfPeople)
                     debug(listOfPeople.toList().toString())
                 }, { e ->
                     print(e.message)
                 },{
                     print("Test")
                 })
         subscriptions.add(subscribeOn)
     }
 */
    fun startSearch(query: String) {
        if (pageNumber in 1..lastPage) {
            val subscribeOn = holdingActivity.api.searchUsers(query, pageNumber, sMaxRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ abc ->
                        run {
                            lastPage = holdingActivity.handlePagination(abc.headers())
                            updateAdapter(abc.body()!!.items)
                            loading = false
                            /*Single.fromCallable {
                                holdingActivity.appDatabase.userDao().deleteAll()
                                print(abc.items)
                                holdingActivity.appDatabase.userDao().insert(abc.items)
                            }*/
                        }
                    }, { e ->
                        run {
                            showLoader(false)
                            holdingActivity.onError(e)
                            loading = false
                        }
                    }

                    )

            holdingActivity.subscriptions.add(subscribeOn)

        }
    }


    private fun updateAdapter(users: List<User>) {
        showLoader(false)
        if (recyclerView?.adapter != null) {
            (recyclerView?.adapter as DataAdapter).apply {
                if (pageNumber == 1)
                    clearItems()
                addItems(users)
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

}

