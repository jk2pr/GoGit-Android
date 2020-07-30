package com.jk.gogit.ui.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    var lastPage: Int = 1
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var loading = false
    var pageNumber = 1

    fun resetPages() {
        lastPage = 1 //Will updated after first hit
        lastVisibleItem = 0
        totalItemCount = 0
        loading = false
        pageNumber = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetPages()
    }
}