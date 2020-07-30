package com.jk.gogit

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.content.ContextCompat
import com.jk.gogit.model.Notification
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : BaseActivity(), NotificationAdapter.OnViewSelectedListener {
    override fun onNotificationClicked(item: Notification) {
        redirectToRepoDetails(this, item.repository.fullName)
        subscriptions.add(model.notificationMarkAsRead(item.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},
                        {
                            print(it.message)
                        })
        )
    }

    var lastPage: Int = 1
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var loading = false
    var pageNumber = 1
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_notification
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(getString(R.string.notification), true)
        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_blue_bright),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_red_light))

        swipeRefresh?.setOnRefreshListener {
            resetPages()
            loadPage()
        }

        notification_recyclerView.apply {
            setHasFixedSize(true)
            val lManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            layoutManager = lManager
            adapter = NotificationAdapter(this@NotificationActivity)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = notification_recyclerView.layoutManager!!.itemCount
                    lastVisibleItem = (notification_recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.action_notification)
        item?.isVisible = false
        return true
    }


    fun loadPage() {
        if (pageNumber in 1..lastPage)
            if (pageNumber == 1 && !swipeRefresh.isRefreshing)
                showLoader(true)
        subscriptions.add(model.getNotifications(true, pageNumber, sMaxRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastPage = handlePagination(it.headers())
                    loading = false
                    showLoader(false)
                    if ((it.body()!!.isEmpty()) and (pageNumber == 1))
                        showEmpty()
                    else
                        if (pageNumber == 1)
                            (notification_recyclerView.adapter as NotificationAdapter).clearItems()
                    (notification_recyclerView.adapter as NotificationAdapter).addItems(it.body()!!)
                    swipeRefresh.isRefreshing = false
                }, {
                    onError(it)
                    loading = false
                }))
    }

    private fun showEmpty() {
        txt_empty.visibility = View.VISIBLE
        notification_recyclerView?.visibility = View.INVISIBLE
        progressbar?.visibility = View.INVISIBLE

    }

    fun showLoader(isLoading: Boolean) {
        txt_empty.visibility = View.INVISIBLE
        if (isLoading) {
            notification_recyclerView?.visibility = View.INVISIBLE
            progressbar?.visibility = View.VISIBLE

        } else {
            notification_recyclerView?.visibility = View.VISIBLE
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
