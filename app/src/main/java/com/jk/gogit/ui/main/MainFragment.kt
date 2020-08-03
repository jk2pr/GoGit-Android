package com.jk.gogit.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jk.gogit.R
import com.jk.gogit.ui.adapters.FeedAdapter
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.ui.view.MainActivity
import com.jk.gogit.ui.viewmodel.UserViewModel
import com.jk.gogit.utils.NavUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins.onError
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main), FeedAdapter.OnViewSelectedListener {

    var lastPage: Int = 1
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var loading = false
    var pageNumber = 1
    private lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(requireContext(), android.R.color.holo_green_light),
                ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light),
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))

        swipeRefresh?.setOnRefreshListener {
            loadPage()
        }

        feed_recyclerView.apply {
            setHasFixedSize(true)
            val lManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            layoutManager = lManager
            feed_recyclerView.adapter = FeedAdapter(this@MainFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = feed_recyclerView.layoutManager!!.itemCount
                    lastVisibleItem = (feed_recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + BaseActivity.VISIBLE_THRESHOLD) {
                        pageNumber++
                        loading = true
                        loadPage()
                    }
                }
            })

        }
        txt_internet.setOnClickListener {
            doLoadingInit()
        }

    }

   private val mainActivity by lazy { activity as MainActivity }
    private fun doLoadingInit() {
        showLoader(true, false)

        mainActivity.subscriptions.add(mainActivity.api.getMyProfile()
                .flatMap {
                    mainActivity.save(it)
                    // loadPage()
                    mainActivity.api.getFeed(it.login, pageNumber, sMaxRecord)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastPage = mainActivity.handlePagination(it.headers())
                    loading = false
                    showLoader(false, false)
                    if (it.body()!!.isEmpty())
                        showEmptyView()
                    else
                        (feed_recyclerView.adapter as FeedAdapter).addItems(it.body()!!)
                }, {
                    showLoader(false, true)
                }))
    }

    private fun loadPage() {
        if (pageNumber in 1..lastPage) {
            mainActivity.subscriptions.add(
                    mainActivity.api.getFeed(mainActivity.getLoginData()!!.login, pageNumber, sMaxRecord)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                //lastPage = handlePagination(it.headers())
                                loading = false
                                showLoader(false, false)
                                if (it.body()!!.isEmpty())
                                    showEmptyView()
                                else
                                    if (pageNumber == 1)
                                        (feed_recyclerView.adapter as FeedAdapter).clearItems()
                                (feed_recyclerView.adapter as FeedAdapter).addItems(it.body()!!)

                            }, {
                                onError(it)
                                showLoader(false, true)
                            }))

        }
    }

    private fun showEmptyView() {
        txt_internet.visibility = View.INVISIBLE
        progressbar?.visibility = View.INVISIBLE
        txt_empty.visibility = View.VISIBLE
    }

    fun showLoader(isLoading: Boolean, isError: Boolean) {
        txt_empty.visibility = View.INVISIBLE
        if (isError) {
            txt_internet.visibility = View.VISIBLE
            main_content?.visibility = View.INVISIBLE
            progressbar?.visibility = View.INVISIBLE
            return
        } else
            txt_internet.visibility = View.GONE
        if (isLoading) {
            main_content?.visibility = View.INVISIBLE
            progressbar?.visibility = View.VISIBLE

        } else {
            swipeRefresh.isRefreshing = false
            main_content?.visibility = View.VISIBLE
            progressbar?.visibility = View.INVISIBLE
        }

    }

    override fun onRepNameClicked(textView: TextView, owner: String) {
        NavUtils.redirectToRepoDetails(requireActivity(), owner)
    }

    override fun onActorNameClicked(imageView: ImageView, loginId: String) {
        NavUtils.redirectToProfile(requireActivity(), loginId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, Observer {

        })

    }

}