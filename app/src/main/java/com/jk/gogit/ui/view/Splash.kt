package com.jk.gogit.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.jk.gogit.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.displayMetrics
import java.math.BigInteger
import java.security.SecureRandom


class Splash : BaseActivity(), OnClickListener, AnkoLogger {


    private val SPLASH_DELAY: Long = 8000 //8 seconds
    private val REDIRECT_URL_CALLBACK = "https://gogit-5a346.firebaseapp.com/__/auth/handler"
    val mDelayHandler = Handler()

    var signed = false
    private val random = SecureRandom()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  MyApplication.appComponent.inject(this)

        if (mAuth.currentUser == null)
            animateLogoImage()
        else {
            redirectToHome(mAuth.currentUser)
        }
    }

    override fun getLayoutResourceId(): Int {
      return R.layout.activity_splash
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
            translationY(-(((displayMetrics.heightPixels / 2)) / 2).toFloat())
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

    override fun onClick(v: View?) {
        when {v?.id == R.id.button2 ->
            run {
                val clientId = getString(R.string.client_id)
                val secretId = getString(R.string.client_secret)
                val url = getString(R.string.github_login_url) +
                        "?scope=user:email user:follow public_repo &client_id=" +
                        "$clientId&secretId=$secretId&state=${getRandomString()}"
                openDialog(url)
            }
            else -> {
                redirectToHome(null)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openDialog(url: String) {
        val builder = Dialog(this, R.style.Dialog_Alert)
        val dialoglayout = layoutInflater.inflate(R.layout.web_dialog, null)
        builder.apply {
            setContentView(dialoglayout)
            setTitle(getString(R.string.app_name))
        }
        val web=dialoglayout.findViewById<WebView>(R.id.webview)
        builder.setOnDismissListener({
            val cookieManager = android.webkit.CookieManager.getInstance()
            cookieManager.removeAllCookies(null)
          web.apply {
              clearCache(true)
              clearFormData()
              clearHistory()
              clearMatches()



          }
        })
       web.run {
            settings.javaScriptEnabled = true
            loadUrl(url)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.startsWith(REDIRECT_URL_CALLBACK)) {
                        val uri = Uri.parse(url)
                        val code = uri.getQueryParameter("code")
                        val state = uri.getQueryParameter("state")
                        val accessTokenUrl = "https://github.com/login/oauth/access_token"
                    val subscrib=api.getAccessToken(
                                accessTokenUrl,
                                getString(R.string.client_id),
                                getString(R.string.client_secret),
                                REDIRECT_URL_CALLBACK,
                                state,
                                code
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { acessToken ->
                                            signInWithToken(acessToken.accessToken)
                                        }, { e ->
                                    e.printStackTrace()

                                })
                        subscriptions.add(subscrib)


                        builder.dismiss()
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
        }

        builder.show()


    }

    fun signInWithToken(token: String) {
        val credential = GithubAuthProvider.getCredential(token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    debug("signInWithCredential:onComplete:" + task.isSuccessful)

                    if (!task.isSuccessful) {
                        task.exception?.printStackTrace()
                    } else {
                        val preference = baseContext.getSharedPreferences("AccessToken", Context.MODE_PRIVATE)
                        preference.edit().putString("AccessToken", token).apply()
                        val user = task.result.user
                        redirectToHome(user)
                    }
                }
    }

    private fun getRandomString(): String {
        return BigInteger(130, random).toString(32)
    }

    private fun redirectToHome(user: FirebaseUser?) {
        if (user != null) {
           // val loggedInUser = LoggedInUser(user.phoneNumber, user.displayName, user.email, user.photoUrl.toString())
            //startActivity(intentFor<UserProfileActivity>(("user" to loggedInUser)))
            //startActivity(intentFor<UserProfileActivity>())
            val intent = Intent(this, UserProfileActivity::class.java)
           // intent.putExtra("user" , loggedInUser)
            startActivity(intent)
        }
        finish()
    }

    override fun onResume() {
        super.onResume()
        mAuth.addAuthStateListener {
            val user = it.currentUser
            signed = user != null
        }
        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
    }

    override fun onPause() {
        super.onPause()
        mDelayHandler.removeCallbacks(mRunnable)
        subscriptions.clear()
    }

}
