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
import com.jk.daggerrxkotlin.application.MyApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_data.*

import javax.inject.Inject
import kotlin.jk.com.dagger.R


class DataFragment : Fragment(), DataAdapter.onViewSelectedListener {
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
    @Inject
    lateinit var api: IApi;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        super.onCreateView(inflater,container,savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_data, container, false)
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
        api.searchUsers("Kotlin", 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ abc ->
                    run {
                        showLoader(false)
                        (recyclerView.adapter as DataAdapter).addItems(abc.items)
                        (recyclerView.adapter as DataAdapter).notifyDataSetChanged()
                    }

                }, { e ->
                    run {
                        Log.d("Tag", e.message)
                        Snackbar.make(recyclerView, e.message ?: "", Snackbar.LENGTH_LONG).show();
                    }
                }
                )


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

    fun showLoader(isShowing: Boolean) {
        if (isShowing == false) {
            recyclerView.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
        }

    }
}
