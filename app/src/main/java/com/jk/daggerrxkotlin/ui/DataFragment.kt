package com.jk.daggerrxkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.jk.daggerrxkotlin.ui.adapters.DataAdapter
import com.jk.daggerrxkotlin.network.api.IApi
import com.jk.daggerrxkotlin.network.api.User
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import com.jk.daggerrxkotlin.network.networkutils.NetworkUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_data.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.snackbar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.jk.com.dagger.R


class DataFragment : Fragment(), AnkoLogger, DataAdapter.onViewSelectedListener {

    @Inject
    lateinit var api: IApi
    @Inject
    lateinit var appDatabase: AppDatabase
    @Inject
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var holdingActivity: MainActivity
    var subscriptions = CompositeDisposable()
    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            snackbar(recyclerView, "No URL assigned to this news")
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        holdingActivity = activity as MainActivity
        MyApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        retainInstance = true
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "006")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Jitendra")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)


        return view

    }

    private fun initSearchView() {
        val layoutParams = android.support.v7.widget.Toolbar.LayoutParams(Gravity.END)
        // layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT
        holdingActivity.searchView.layoutParams = layoutParams
        holdingActivity.searchView.setIconifiedByDefault(true)
        holdingActivity.searchView.queryHint = "Type to search"
        holdingActivity.searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    showLoader(true)
                    val searchText = "%$query%"
                    subscriptions.clear()
                    val subscribeOn = appDatabase.userDao().getUserList(searchText).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { listOfPeople ->
                                updateAdapter(listOfPeople)
                                debug(listOfPeople.toList().toString())
                            }
                    subscriptions.add(subscribeOn)
                    return false
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val searchText = "%$newText%"
                subscriptions.clear()
                showLoader(true)
                val subs = appDatabase.userDao().getUserList(searchText)
                        .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe({ data ->
                            run {
                                updateAdapter(data)
                            }
                        },
                                { e ->
                                    print(e.message)
                                })
                subscriptions.add(subs)
                return false
            }

        })
        holdingActivity.searchView.setOnCloseListener {
            holdingActivity.searchView.clearFocus()
            holdingActivity.searchView.setQuery(null, false)
            holdingActivity.searchView.onActionViewCollapsed()


            showAllFromLocalDb()
            true
        }

        print(holdingActivity.searchView.tag)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager =LinearLayoutManager(context)

        }
        initAdapter()
        initSearchView()
    }


    private fun initAdapter() {
        //  if (recyclerView.adapter == null)
        recyclerView.adapter = DataAdapter(this)
        if (NetworkUtils.hasActiveInternetConnection(activity as Context)) {
            debug("Has internet, will downloaded from server")
            requestNews()
        } else {
            debug("No Internet will fetch from database")
            showAllFromLocalDb()
        }


    }

    private fun showAllFromLocalDb() {
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

    private fun requestNews() {
        subscriptions.clear()
        val subscribeOn = api.searchUsers("Kotlin", 1, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ abc ->
                    run {
                        updateAdapter(abc.items)
                        Single.fromCallable {
                            appDatabase.userDao().deleteAll()
                            print(abc.items)
                            appDatabase.userDao().insert(abc.items)

                        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()


                    }

                }, { e ->
                    run {
                        debug(e.message)
                        snackbar(recyclerView, e.message ?: "")
                    }
                }

                )

        subscriptions.add(subscribeOn)

    }


    fun updateAdapter(users: List<User>) {
        showLoader(false)
        if (users.isEmpty()) {
            showEmptyView()
            debug("No user")
            return
        } else {
            if (recyclerView?.adapter != null) {
                val adapter = recyclerView?.adapter as DataAdapter
                with(adapter) {
                    clearItems()
                    addItems(users)
                }

            }
        }
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    fun showLoader(isShowing: Boolean) {
        if (!isShowing) {
            recyclerView?.visibility = View.VISIBLE
            progressbar?.visibility = View.GONE
        } else {
            recyclerView?.visibility = View.GONE
            progressbar?.visibility = View.VISIBLE
        }

    }

    private fun showEmptyView() {
        emptyview.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }
}
