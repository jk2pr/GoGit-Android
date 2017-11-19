package com.jk.daggerrxkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.daggerrxkotlin.adapters.DataAdapter
import com.jk.daggerrxkotlin.api.IApi
import com.jk.daggerrxkotlin.api.User
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import com.jk.daggerrxkotlin.networkutils.NetworkUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_data.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject
import kotlin.jk.com.dagger.R


class DataFragment : Fragment(), AnkoLogger,DataAdapter.onViewSelectedListener {

    @Inject
    lateinit var api: IApi;
    @Inject


    lateinit var appDatabase: AppDatabase;

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
        MyApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        super.onCreateView(inflater,container,savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        retainInstance= true
        return view;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
          //  clearOnScrollListeners()
        }
        initAdapter()
    }


    private fun initAdapter() {
      //  if (recyclerView.adapter == null)
            recyclerView.adapter = DataAdapter(this)
        if (NetworkUtils.hasActiveInternetConnection(activity as Context)) {
            debug("Has internet, will downloaded from server")
            requestNews()
        } else {
            debug("No Internet will fetch from database")
            appDatabase.userDao().getAllPeople().subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe { listOfPeople ->
                        updateAdapter(listOfPeople);
                       debug(listOfPeople.toList().toString());
                    }
        }


    }

    private fun requestNews() {
        api.searchUsers("Kotlin", 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ abc ->
                    run {
                        updateAdapter(abc.items)
                        Single.fromCallable {
                            // val user = abc.items.get(0);
                            appDatabase.userDao().insert(abc.items)

                        }.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe()


                    }

                }, { e ->
                    run {
                       debug( e.message)
                        snackbar(recyclerView, e.message ?: "")
                    }
                }
                )


    }


    fun updateAdapter(users: List<User>) {
        showLoader(false)
        if (users.isEmpty()) {
            showEmptyView()
            debug("No user")
            return
        } else {
            if (recyclerView?.adapter!=null)
            {
                (recyclerView.adapter as DataAdapter).addItems(users)
                (recyclerView.adapter as DataAdapter).notifyDataSetChanged()
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

    fun showEmptyView() {
        emptyview.visibility = View.VISIBLE

    }

}
