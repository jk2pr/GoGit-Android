package com.jk.gogit.ui.view

import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.jk.gogit.R
import com.jk.gogit.exception.NotStarringException
import com.jk.gogit.exception.NotSubscribedException
import com.jk.gogit.exception.StarringException
import com.jk.gogit.exception.SubscribedException
import com.jk.gogit.model.RepositoryDetails
import com.jk.gogit.ui.view.fragments.FileListFragment
import com.jk.gogit.ui.view.fragments.ReadMeFragment
import com.jk.gogit.utils.NavUtils.redirectToAboutRepo
import com.jk.gogit.utils.NavUtils.redirectToOrganisation
import com.jk.gogit.utils.NavUtils.redirectToProfile
import com.jk.gogit.utils.NavUtils.redirectToRepoDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_repo_details.*
import org.jetbrains.anko.toast

class RepoDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeRefresh.isEnabled = verticalOffset == 0
    }


    private val repoOwner by lazy { intent.getStringExtra("repoOwner") }
    private val owner by lazy { repoOwner.split("/")[0] }
    private val repoName by lazy { repoOwner.split("/")[1] }
    private var isStarring = false
    private var isWatching = false

    private var itt: RepositoryDetails? = null


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_repo_details
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(getString(R.string.files), true)
        container?.adapter = RepoPageAdapter(supportFragmentManager)
        tabs?.setupWithViewPager(container)

        container.offscreenPageLimit = tabs.tabCount
        /* val length = tabs.getTabCount()
         for (i in 0..length) {
             val text = TextView(this)
             text.text = (container.adapter as RepoPageAdapter).getPageTitle(i)
             tabs.getTabAt(i)?.customView = text
         }*/

        /* val onPageListener = object : ViewPager.OnPageChangeListener {
             override fun onPageScrollStateChanged(state: Int) {}
             override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
             override fun onPageSelected(position: Int) {


                 *//* val cx = appbar.width / 2
                 val cy = appbar.height / 2


                 val currentTab = tabs.getTabAt(position)
                 if (currentTab!!.customView != null) {
                     var outLocation = intArrayOf(0,0)
                     currentTab.customView!!.getLocationOnScreen(outLocation)

                     val tabX = outLocation[0]+(currentTab.customView!!.width/2)
                     val tabY = outLocation[1]

                     val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble())+1.2
                     val anim = ViewAnimationUtils.createCircularReveal(appbar, tabX, tabY,
                             0.0f, finalRadius.toFloat())
                     appbar.setBackgroundColor(Color.YELLOW)
                     anim.start();
                 }*//*
                when (position) {
                    0, 1 ->
                        showFab(false)
                    else -> {
                        val aboutRepoFrag = (container.adapter as? RepoPageAdapter)
                                ?.registeredFragments?.get(position) as? AboutRepo
                        val fragment = (aboutRepoFrag?.getViewPager()?.adapter as? AboutRepo.AboutRepoPageAdapter)
                                ?.registeredFragments?.get(aboutRepoFrag.getViewPager().currentItem)
                        if ((fragment is IssueFragment) or (fragment is CommitsFragment))
                            showFab(true) //Issue Fragment
                        else
                            showFab(false) // Others
                    }
                }
            }
        }
        container.addOnPageChangeListener(onPageListener)*/
        float_filter_issue.show()
        float_filter_issue.setOnClickListener {
            if (it.isShown) {
                if (itt != null) {
                    val repoDetail = Gson().toJson(itt)
                    redirectToAboutRepo(this, repoDetail)
                }
            }
        }
        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_blue_bright),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_red_light))

        swipeRefresh?.setOnRefreshListener {
            updateRepo()
        }
        updateRepo()
    }


    private fun updateRepo() {
        if (repoOwner != null) {
            //  showLoader(true)
            if (swipeRefresh.isRefreshing)
                container?.adapter?.notifyDataSetChanged()
            subscriptions.add(model.getRepoDetails(owner, repoName) //Get Repo Details
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefresh.isRefreshing = false
                        updateUi(it)
                    }, {
                        onError(it)
                        it.printStackTrace()
                        swipeRefresh.isRefreshing = false
                    }))
            if (getLoginData()?.login.isNullOrEmpty()) {
                //Not a logged in user
            } else {
                subscriptions.add(model.ifIamStaring(owner, repoName) //Check for stars
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                        }, {
                            handleStarringStuffs(it, false)
                        }))
                subscriptions.add(model.ifIamWatching(owner, repoName) //Check for subscription/Watchers
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                        }, {
                            handleSubscriptionStuffs(it, false)
                        }))

            }
        }
    }

    private fun handleStarringStuffs(it: Throwable, isWriting: Boolean) {
        when (it) {
            is StarringException -> {
                isStarring = true
                if (isWriting) toast(getString(R.string.now_u_r_starrer))
            }
            is NotStarringException -> {
                isStarring = false
                if (isWriting) toast(getString(R.string.now_u_r_not_starrer))
            }
            else -> onError(it)
        }
        invalidateOptionsMenu()
    }

    private fun handleSubscriptionStuffs(it: Throwable, isWriting: Boolean) {
        when (it) {
            is SubscribedException -> {
                isWatching = true
                if (isWriting) toast(getString(R.string.now_u_r_watching_this_repo))
            }
            is NotSubscribedException -> {
                isWatching = false
                if (isWriting) toast(getString(R.string.now_u_r_not_watching_this_repo))
            }
            else -> onError(it)
        }
        invalidateOptionsMenu()
    }

    private fun updateUi(it: RepositoryDetails) {
        this.itt = it
        if (!it.parent?.fullName.isNullOrBlank())
            txt_repo_parent.apply {
                visibility = View.VISIBLE
                val textString = String.format(resources.getString(R.string.forked_from, it.parent?.fullName))
                val span = Spannable.Factory.getInstance().newSpannable(textString)
                span.setSpan(object : ClickableSpan() {
                    override fun onClick(v: View) {
                        redirectToRepoDetails(this@RepoDetailsActivity, it.parent?.fullName)
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = ContextCompat.getColor(this@RepoDetailsActivity, android.R.color.white)
                        ds.isUnderlineText = true
                        //ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                }, textString.indexOf(it.parent?.fullName!!), textString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                text = span
                movementMethod = LinkMovementMethod.getInstance()

            }
        else {
            txt_repo_parent.visibility = View.GONE
        }
        txt_repo_name.text = it.fullName
        if (it.description.isNullOrEmpty()) {
            txt_repo_description.setTextColor(ContextCompat.getColor(this@RepoDetailsActivity, R.color.colorTextDark))
            txt_repo_description.text = resources.getString(R.string.no_description_provided)
        } else {
            txt_repo_description.text = it.description
            txt_repo_description.setTextColor(ContextCompat.getColor(this@RepoDetailsActivity, android.R.color.white))
        }
        val span = Spannable.Factory.getInstance().newSpannable(it.fullName)
        span.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                if (it.organization != null)
                    redirectToOrganisation(this@RepoDetailsActivity, it.organization.login)
                else
                    redirectToProfile(this@RepoDetailsActivity, it.owner.login)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@RepoDetailsActivity, android.R.color.white)
                ds.isUnderlineText = true
                //ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
        }, 0, owner.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_repo_name.text = span
        txt_repo_name.movementMethod = LinkMovementMethod.getInstance()
        swipeRefresh.isRefreshing = false
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val starItem = menu?.findItem(R.id.action_star)
        val watchItem = menu?.findItem(R.id.action_watch)
        if (getLoginData()?.login.isNullOrEmpty()) {
            //Not a logged in user
            starItem?.isVisible = false
            watchItem?.isVisible = false
        } else {
            starItem?.isVisible = true
            watchItem?.isVisible = true

            if (isStarring) {
                starItem?.isChecked = true
                starItem?.setIcon(R.drawable.ic_star_white_fill)
            } else {
                starItem?.isChecked = false
                starItem?.setIcon(R.drawable.ic_star_border_white)
            }
            if (isWatching) {
                watchItem?.isChecked = true
                watchItem?.setIcon(R.drawable.ic_watching_on)
            } else {
                watchItem?.isChecked = false
                watchItem?.setIcon(R.drawable.ic_watching_off)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_star -> {
                if (item.isChecked) {
                    item.isChecked = false
                    item.setIcon(R.drawable.ic_star_border_white)
                } else {
                    item.isChecked = true
                    item.setIcon(R.drawable.ic_star_white_fill)
                }
                doStar(item.isChecked)
                true
            }
            R.id.action_watch -> {
                if (item.isEnabled) {

                }
                if (item.isChecked) {
                    item.isChecked = false
                    item.setIcon(R.drawable.ic_watching_off)
                } else {
                    item.isChecked = true
                    item.setIcon(R.drawable.ic_watching_on)
                }
                doWatch(item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doWatch(isWatching: Boolean) {
        if (isWatching)
            subscriptions.add(model.doWatch(owner, repoName, true, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        print(it)
                    }, {
                        handleSubscriptionStuffs(it, true)
                    }))
        else
            subscriptions.add(model.undoWatch(owner, repoName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                    }, {
                        handleSubscriptionStuffs(it, true)
                    }))


    }


    private fun doStar(isStarring: Boolean) {
        if (isStarring)
            subscriptions.add(model.doStaring(owner, repoName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                    }, {
                        handleStarringStuffs(it, true)
                    }))
        else
            subscriptions.add(model.unDoStaring(owner, repoName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                    }, {
                        handleStarringStuffs(it, true)
                    }))


    }

    inner class RepoPageAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {
        var registeredFragments = SparseArray<androidx.fragment.app.Fragment>()
        override fun getItem(position: Int):Fragment {
            val bundle = Bundle()
            bundle.putString("owner", owner)
            bundle.putString("repoName", repoName)
            //  val dataString = Gson().toJson(it)
            return when (position) {

                0 -> {
                    val fragment = ReadMeFragment()
                    // bundle.putString("data", dataString)
                    fragment.arguments = bundle
                    fragment
                }
                else -> {
                    val fragment = FileListFragment()
                    // bundle.putString("data", dataString)
                    fragment.arguments = bundle
                    fragment
                }
            /* 2 -> {
                 val fragment = AboutRepo()
                 // bundle.putString("data", dataString)
                 fragment.arguments = bundle
                 fragment
             }*/
            }

        }

        override fun getItemPosition(int: Any): Int {
            return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as androidx.fragment.app.Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getCount(): Int {
            return 2
        }


        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 ->
                    "ReadMe"
                1 ->
                    "Files"

                else ->
                    null

            }
        }

    }

    override fun onResume() {
        super.onResume()
        appbar?.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appbar?.removeOnOffsetChangedListener(this)
    }
}
