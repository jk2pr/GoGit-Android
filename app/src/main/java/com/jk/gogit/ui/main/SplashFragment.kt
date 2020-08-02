package com.jk.gogit.ui.main

import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.R
import com.jk.gogit.utils.NavUtils.redirectToHome
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val splashDelay: Long = 3000 //3 seconds
    private val mDelayHandler = Handler()

    @Inject
    lateinit var mAuth: FirebaseAuth

    private val mRunnable = Runnable {
        if (activity?.isFinishing == false) {

            when (/*intent.dataString != null -> {
                        //Handle
                        val dataString = intent?.dataString?.removeSuffix("/")?.toLowerCase(Locale.getDefault())
                        val baseUrl = arrayOf(
                                "github.com/",
                                "http://github.com/",
                                "https://github.com/",
                                "http://github.com",
                                "https://github.com")
                        if (baseUrl.contains(dataString)) {
                            // url is not have any relative url
                            redirectToNoLoginMainActivity(this)
                        } else
                            if (dataString!=null) {
                                if (Utils.isUserUrl(dataString)) {
                                    //Redirect to USer    \
                                    val uName = Utils.getUserFromUrl(dataString)
                                    if (!uName.isNullOrBlank())
                                        redirectToProfile(this, uName)
                                } else if (Utils.isRepoUrl(dataString)) {
                                    //Redirect to Repo
                                    val repo = Utils.getRepoFullNameFromUrl(dataString)
                                    if (!repo.isNullOrBlank())
                                        redirectToRepoDetails(this, repo)
                                } else if (Utils.isIssueUrl(dataString)) {
                                    val dataArray = Utils.getIssuePath(dataString)
                                    redirectToIssueDetails(this, dataArray[0], dataArray[1], dataArray[2].toInt())
                                } else {
                                    analytics.logEvent(dataString, null)
                                    toast("url is Invalid, can,t open now")
                                }
                            }
                        finish()
                    }*/
                mAuth.currentUser) {
                null -> findNavController().navigate(R.id.fragment_login)
                else -> redirectToHome(requireActivity(), mAuth.currentUser)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mDelayHandler.postDelayed(mRunnable, splashDelay)
    }

    override fun onPause() {
        super.onPause()
        mDelayHandler.removeCallbacks(mRunnable)
    }


}