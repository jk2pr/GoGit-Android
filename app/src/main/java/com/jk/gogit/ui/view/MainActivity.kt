package com.jk.gogit.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.crashlytics.android.answers.CustomEvent
import com.jk.gogit.R
import com.jk.gogit.ui.adapters.FeedAdapter
import com.jk.gogit.utils.NavUtils.redirectToProfile
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), FeedAdapter.OnViewSelectedListener {

    var lastPage: Int = 1
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var loading = false
    var pageNumber = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(resources.getString(R.string.app_name), false)
        answers.logCustom(CustomEvent("MainActivity"))

        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_blue_bright),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_red_light))

        swipeRefresh?.setOnRefreshListener {
            resetPages()
            loadPage()
        }

        feed_recyclerView.apply {
            setHasFixedSize(true)
            val lManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            layoutManager = lManager
            feed_recyclerView.adapter = FeedAdapter(this@MainActivity)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = feed_recyclerView.layoutManager!!.itemCount
                    lastVisibleItem = (feed_recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
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

        doLoadingInit()
        // loadPage()
    }


    private fun doLoadingInit() {
        showLoader(true, false)
        subscriptions.add(api.getMyProfile()
                .flatMap {
                    save(it)
                    // loadPage()
                    api.getFeed(it.login, pageNumber, sMaxRecord)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastPage = handlePagination(it.headers())
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
            subscriptions.add(
                    api.getFeed(getLoginData()!!.login, pageNumber, sMaxRecord)
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        subscriptions.add(model.getNotifications(false, 0, sMaxRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isNotificationAvailable = it.body()!!.isEmpty() == false
                    super.onPrepareOptionsMenu(menu)
                }, {
                    onError(it)
                    it.printStackTrace()
                }))

        return true
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onRepNameClicked(textView: TextView, owner: String) {
        redirectToRepoDetails(this, owner)
    }

    override fun onActorNameClicked(imageView: ImageView, loginId: String) {
        redirectToProfile(this, loginId)
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

    private fun resetPages() {
        lastPage = 1 //Will updated after first hit
        lastVisibleItem = 0
        totalItemCount = 0
        loading = false
        pageNumber = 1
    }
}
