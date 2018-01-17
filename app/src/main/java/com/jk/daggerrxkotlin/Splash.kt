package com.jk.daggerrxkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.os.Bundle
import android.os.Handler

import android.support.v7.app.AppCompatActivity
import android.util.Half.toFloat
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.jk.com.dagger.R



class Splash : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    val mDelayHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animateLogoImage()

    }

    private fun animateLogoImage() {
        imageView.animate()
                .alpha(1.0f)
                .setDuration(5000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator)
                    {
                    run {
                        super.onAnimationEnd(animation)
                        moveLogoToTop()
                    }
                    }
                })
    }

    private fun moveLogoToTop() {
        imageView.animate().apply {
            translationY(-500f)
                    .setDuration(1000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator)
                        {
                            run {
                                super.onAnimationEnd(animation)
                                moveButtonsBelowLogo()
                            }
                        }
                    })
        }
    }
    private fun moveButtonsBelowLogo(){

        button2.animate().apply {
            setDuration(1000)
            alpha(1.0f)
            translationY(-((imageView.bottom/2).toFloat()/2))
        }
        button3.animate().apply {
            setDuration(1000)
            alpha(1.0f)
            translationY(-((imageView.bottom/2.toFloat()/2)-150))
        }

    }

    private val mRunnable = Runnable {
        if (!isFinishing) {
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
