package com.jk.gogit.ui.view.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jk.gogit.R
import com.jk.gogit.model.Users
import com.jk.gogit.ui.adapters.UserAdapter
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.ui.view.UserProfileActivity
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class FollowersFragment : BaseFragment(), UserAdapter.OnViewSelectedListener {
    lateinit var holdingActivity: UserProfileActivity

    private var adapter: UserAdapter = UserAdapter(this)

    private val selectedUser by lazy { arguments?.getString("id")!! }
    private val index by lazy { arguments?.getInt("index") }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as UserProfileActivity
    }

    override fun onItemSelected(login: String) {
        holdingActivity.navHistory.apply {
            push(login)
        }
        redirectToProfile(holdingActivity,login)
        // holdingActivity.updateUser(login)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        holdingActivity.showFab(false)
        return inflater.inflate(R.layout.fragment_users, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_followers.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = this@FollowersFragment.adapter
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_followers.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_followers.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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

            val observable: Observable<Response<List<Users>>> = if (index == 1)
                holdingActivity.model.getFollowers(selectedUser, pageNumber, sMaxRecord)
            else
                holdingActivity.model.getFollowing(selectedUser, pageNumber, sMaxRecord)

            if (pageNumber == 1)
                showLoader(true)

            holdingActivity.subscriptions.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        loading = false
                        lastPage = holdingActivity.handlePagination(it.headers())
                        updateAdapter(it.body()!!)
                    }, { e ->
                        holdingActivity.onError(e)
                        showLoader(false)

                    }, {
                        showLoader(false)

                    }))

        }
    }


    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_followers?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE

    }

    private fun updateAdapter(data: List<Users>) {
        with(adapter)
        {
            if (pageNumber == 1)
                clearItems()
            addItems(data)
            // showLoader(false)
        }
    }


}// Required empty public constructor
