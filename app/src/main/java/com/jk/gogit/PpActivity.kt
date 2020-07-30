package com.jk.gogit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.*
import com.jk.gogit.ui.view.BaseActivity
import kotlinx.android.synthetic.main.activity_pp.*

class PpActivity : BaseActivity() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_pp
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        for (i in 0 until menu!!.size())
            menu.getItem(i)?.isVisible = false
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(getString(R.string.app_name), true)
        web_view.apply {
            //layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, (height/1.5).toInt())
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    //hide loading
                    showLoader(false)
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    //Show loading
                    showLoader(true)
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    super.onReceivedError(view, request, error)
                    //   val data ="<html><head><style type=\"text/css\">body{margin:0 auto;text-align:center;}</style></head><body>arrayListAds.get(0).getUrl()</body></html>"
                    view.loadUrl("about:blank")
                    //showLoader(false)
                    showError()
                }
            }
            this.settings.apply {

                javaScriptEnabled = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                setAppCachePath(cacheDir.path)
                setAppCacheEnabled(true)
                cacheMode = WebSettings.LOAD_NO_CACHE
                loadWithOverviewMode = true
                useWideViewPort = false
                builtInZoomControls = false
                displayZoomControls = false
            }
            loadUrl("https://jk2pr.github.io/")
        }
        btn_ok.setOnClickListener {
            onBackPressed()
        }

    }

    fun showError() {
        txt_view_empty.visibility = View.VISIBLE
        val emo = String(Character.toChars(0x1F614))
        val exist = txt_view_empty.text
        val final = exist.toString().plus("\n\n").plus(emo)
        txt_view_empty.text = final
        btn_ok?.visibility = View.VISIBLE
        //hide progress and webview
        web_view?.visibility = View.INVISIBLE
        progressbar?.visibility = View.INVISIBLE

    }

    fun showLoader(isLoading: Boolean) {
        if (isLoading) {
            web_view?.visibility = View.INVISIBLE
            btn_ok?.visibility = View.INVISIBLE
            progressbar?.visibility = View.VISIBLE
        } else {
            web_view?.visibility = View.VISIBLE
            btn_ok?.visibility = View.VISIBLE
            progressbar.visibility = View.INVISIBLE
        }


    }
}
