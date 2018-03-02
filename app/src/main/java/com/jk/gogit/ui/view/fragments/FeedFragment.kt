package com.jk.gogit.ui.view.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.ui.adapters.FeedAdapter
import com.jk.gogit.ui.view.MainActivity
import com.jk.gogit.ui.view.UserProfileActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feed.*
import org.jetbrains.anko.intentFor


/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : Fragment(),FeedAdapter.onViewSelectedListener {
    override fun onItemSelected(id: String?) {
        startActivity(activity?.intentFor<UserProfileActivity>(("id" to id)))
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feed_adapter.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)

        }
        val activity: MainActivity = activity as MainActivity
        feed_adapter.adapter=FeedAdapter(this)
        activity.api.getMyProfile()
                .flatMap {
                    activity.save(it)
                    activity.api.getFeed(it.login, 1, 100)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run {

                        (feed_adapter.adapter as FeedAdapter).addItems(it)
                    }
                }, {
                    it.printStackTrace()
                })
    }

}
