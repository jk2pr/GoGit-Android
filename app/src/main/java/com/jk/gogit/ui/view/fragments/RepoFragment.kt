package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jk.gogit.R
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.adapters.RepoAdapter
import kotlinx.android.synthetic.main.fragment_repo.*

class RepoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView_repo.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = RepoAdapter(null)

        }
    }

    fun updateUI(repos: List<Repo>) {
        if (recyclerView_repo?.adapter == null)
            recyclerView_repo.adapter = RepoAdapter(null)
        val adapter = recyclerView_repo?.adapter as RepoAdapter
        with(adapter) {
            clearItems()
            addItems(repos)
            // showLoader(false)

        }
    }
}

