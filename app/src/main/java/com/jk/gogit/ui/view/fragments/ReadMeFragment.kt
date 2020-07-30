package com.jk.gogit.ui.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.codeview.Codeview
import com.jk.gogit.R
import com.jk.gogit.ui.view.RepoDetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_read_me.*

class ReadMeFragment : androidx.fragment.app.Fragment() {

    val holdingActivity by lazy { activity as RepoDetailsActivity }
    val repoName by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("owner") }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_me, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = holdingActivity.model
        var baseUrl = ""
        showLoader(true)

     /*   web_readme_content.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN ->
                    // Disallow ScrollView to intercept touch events.
                    nested.requestDisallowInterceptTouchEvent(false)

                MotionEvent.ACTION_UP ->
                    // Allow ScrollView to intercept touch events.
                    nested.requestDisallowInterceptTouchEvent(true)
            }

            // Handle HorizontalScrollView touch events.
            v.onTouchEvent(event)
            true
        }*/



        holdingActivity.subscriptions.add(model.getReadMeAsJSON(owner!!, repoName!!)
                .flatMap {
                    baseUrl = it.downloadUrl.removeSuffix("/README.MD")
                    model.getReadMeAsHTML(owner!!, repoName!!)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoader(false)
                    render(baseUrl, it)
                }, {
                    holdingActivity.onError(it)
                    txt_no_content?.visibility = View.VISIBLE
                    showLoader(false)
                }))
    }


    private fun render(baseUrl: String, it: String) {
        txt_no_content?.visibility = View.GONE
        Codeview.with(holdingActivity)
                .withCode(it)
                .setAutoWrap(true)
                .into(web_readme_content, baseUrl)
        /*    web_readme_content.settings.apply {
                javaScriptEnabled = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                setAppCachePath(holdingActivity.cacheDir.path)
                setAppCacheEnabled(true)
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }
            web_readme_content.apply {
                webViewClient = WebViewClient()
                scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                setInitialScale(1)
               // loadUrl("about:blank")
                loadDataWithBaseURL(baseUrl, Utils.generateHtmlSourceHtml(it, "", ""), "text/html", "UTF-8", "")
            }
            holdingActivity.toast("Double tab to zoom")*/
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        web_readme_content?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

}
