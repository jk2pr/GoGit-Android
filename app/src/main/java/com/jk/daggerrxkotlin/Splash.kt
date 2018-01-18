package com.jk.daggerrxkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.browse
import kotlin.jk.com.dagger.R


class Splash : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 8000 //3 seconds
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
            button2.setOnClickListener({ v ->
                run {
                    val clientId = getString(R.string.client_id)
                    val secretId = getString(R.string.client_secret)
                    val url = "${getString(R.string.github_login_url)}?scope=user:email user:follow public_repo &client_id=$clientId&secretId=$secretId"
                    browse(url)
                } //   startActivity(intentFor<MainActivity>())
                //  finish()


            })
            //startActivity(intentFor<LoginActivity>())
            //finish()


        }
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
