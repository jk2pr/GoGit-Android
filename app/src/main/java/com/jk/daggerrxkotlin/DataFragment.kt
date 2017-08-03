package com.jk.daggerrxkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.daggerrxkotlin.adapters.DataAdapter
import com.jk.daggerrxkotlin.managers.NewsManager
import kotlinx.android.synthetic.main.fragment_data.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import kotlin.jk.com.daggerrxkotlin.R


class DataFragment : RxBaseFragment(), DataAdapter.onViewSelectedListener {
    override fun onItemSelected(url: String?) {

        if (url.isNullOrEmpty()) {
            Snackbar.make(recyclerView, "No URL assigned to this news", Snackbar.LENGTH_LONG).show()
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }


    private var mListener: OnFragmentInteractionListener? = null
    var newsManager: NewsManager= NewsManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_data, container, false)
        return view;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            clearOnScrollListeners()
        }
        initAdapter()
    }


    private fun initAdapter() {
        if (recyclerView.adapter == null) {
            requestNews();
            recyclerView.adapter = DataAdapter(this)
        }
    }

    private fun requestNews() {
        val subscription = newsManager.getNews("", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    retData ->
                    (recyclerView.adapter as DataAdapter).addItems(retData)
                }, {
                    e ->
                    Snackbar.make(recyclerView, e.message ?: "", Snackbar.LENGTH_LONG).show();
                }
                )

        subscriptions.add(subscription)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}
