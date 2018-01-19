package com.jk.daggerrxkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.intentFor
import kotlin.jk.com.dagger.R


class Splash : AppCompatActivity(), OnClickListener {

    override fun onClick(v: View?) {
        when {v?.id == R.id.button2 ->
            run {
                val clientId = getString(R.string.client_id)
                val secretId = getString(R.string.client_secret)
                val url = "${getString(R.string.github_login_url)}?scope=user:email user:follow public_repo &client_id=$clientId&secretId=$secretId"
                openDialog(url)
            }
            else -> {
                startActivity(intentFor<MainActivity>())
                finish()

            }

        }

    }

    private val SPLASH_DELAY: Long = 8000 //3 seconds
    private val REDIRECT_URL_CALLBACK = "https://daggerrxkotlin.firebaseapp.com/__/auth/handler"
    val mDelayHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animateLogoImage()

    }

    private fun animateLogoImage() {
        imageView.animate().apply {
            alpha(1.0f)
            duration = 5000
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    run {
                        super.onAnimationEnd(animation)
                        moveLogoToTop()
                    }
                }
            })
        }
    }

    private fun moveLogoToTop() {
        imageView.animate().apply {
            translationY(-500f)
            duration = 1000
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    run {
                        super.onAnimationEnd(animation)
                        moveButtonsBelowLogo()
                    }
                }
            })
        }
    }

    private fun moveButtonsBelowLogo() {

        button2.animate().apply {
            duration = 1000
            alpha(1.0f)
            translationY(-((imageView.bottom / 2).toFloat() / 2))
        }
        button3.animate().apply {
            duration = 1000
            alpha(1.0f)
            translationY(-((imageView.bottom / 2.toFloat() / 2) - 150))
        }

    }

    private val mRunnable = Runnable {
        if (!isFinishing) {
            button2.setOnClickListener(this)
            button3.setOnClickListener(this)
            //startActivity(intentFor<LoginActivity>())
            //finish()


        }
    }

    fun openDialog(url: String) {


        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.web_dialog)
        builder.setTitle(R.string.app_short_name)
      //  val dialog = Dialog(this, R.style.Dialog_Alert)
        val view = layoutInflater.inflate(R.layout.web_dialog, null)
       /* dialog.setContentView(view)
        dialog.setTitle(R.string.app_short_name)
        dialog.show()*/
        val webView0 = view.findViewById<WebView>(R.id.webview)
        webView0.loadUrl(url)
        webView0.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith(REDIRECT_URL_CALLBACK)) {
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
                    val state = uri.getQueryParameter("state")
                    //       sendPost(code, state)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        builder.show()


    }

    override fun onResume() {
        super.onResume()

        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
    }

    override fun onPause() {
        super.onPause()
        mDelayHandler.removeCallbacks(mRunnable)
    }

}
