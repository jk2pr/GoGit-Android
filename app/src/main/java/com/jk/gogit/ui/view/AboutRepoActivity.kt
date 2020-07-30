package com.jk.gogit.ui.view

import android.app.Dialog
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.jk.gogit.R
import com.jk.gogit.model.Branch
import com.jk.gogit.model.RepositoryDetails
import com.jk.gogit.ui.view.fragments.*
import com.jk.gogit.utils.NavUtils.redirectToOrganisation
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_about_repo.*
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional

class AboutRepoActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeRefresh.isEnabled = verticalOffset == 0
    }


    /*val repoName by lazy { intent?.extras?.getString("repoName") }
    val owner by lazy { intent?.extras?.getString("owner") }

*/
    private val itt: RepositoryDetails by lazy { Gson().fromJson(intent.getStringExtra("repoDetail"), RepositoryDetails::class.java) }
    private val issueFilterDialog by lazy {
        val dialogLayout = View.inflate(this, R.layout.filter_issue_dialog, null)
        AlertDialog.Builder(this).apply {
            setView(dialogLayout)
        }.create()
    }
    private val commitBranchFilterDialog by lazy {
        val dialogLayout = View.inflate(this, R.layout.filter_commit_dialog, null)
        AlertDialog.Builder(this).apply {
            setView(dialogLayout)
        }.create()
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_about_repo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableHomeInToolBar(getString(R.string.about), true)
        container?.adapter = AboutRepoPageAdapter()
        tabs?.setupWithViewPager(container)
        container.offscreenPageLimit = tabs.tabCount
        val onPageListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                apply {
                    when (position) {
                        0, 4 ->
                            showFab(true)
                        else ->
                            showFab(false)
                    }
                }
            }
        }
        container.addOnPageChangeListener(onPageListener)
        container.post {
            onPageListener.onPageSelected(0)
        }
        float_filter_issue.setOnClickListener {
            if (it.isShown)
                openPopUp()
        }
        swipeRefresh.setOnRefreshListener {
            updateUi()
        }
        updateUi()


    }

    private fun updateUi() {
        resetDialog()
        txt_repo_name.text = itt.forksUrl
        if (itt.description.isNullOrEmpty()) {
            txt_repo_description.setTextColor(ContextCompat.getColor(this, R.color.colorTextDark))
            txt_repo_description.text = resources.getString(R.string.no_description_provided)
        } else {
            txt_repo_description.text = itt.description
            txt_repo_description.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        }
        val span = Spannable.Factory.getInstance().newSpannable(itt.fullName)
        span.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                if (itt.organization != null)
                    redirectToOrganisation(this@AboutRepoActivity, itt.organization!!.login)
                else
                    redirectToProfile(this@AboutRepoActivity, itt.owner.login)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@AboutRepoActivity, android.R.color.white)
                ds.isUnderlineText = true
                //ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
        }, 0, itt.owner.login.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_repo_name.text = span
        txt_repo_name.movementMethod = LinkMovementMethod.getInstance()

        container?.adapter?.notifyDataSetChanged()
        swipeRefresh.isRefreshing = false
    }

    fun getSelectedFilterType(): StringBuffer? {
        val radioGroup = issueFilterDialog.findOptional<RadioGroup>(R.id.radio_1)
        if (radioGroup != null)
            for (i in 0 until radioGroup.childCount) {
                val item = radioGroup.getChildAt(i) as RadioButton
                if (item.isChecked) {
                    return StringBuffer(item.text.toString().trim().toLowerCase())
                }
            }
        return StringBuffer("all")
    }


    fun getSelectedBranch(): StringBuffer {
        val radioGroup = commitBranchFilterDialog.findOptional<RadioGroup>(R.id.rg_filter_commit)
        radioGroup?.let {
            if (it.tag != null)
                return StringBuffer(it.tag.toString())
        }
        val defaultSelection = itt.defaultBranch//"master"
        radioGroup?.tag = defaultSelection
        return StringBuffer(defaultSelection)
    }


    fun showFab(isShown: Boolean) {
        when {
            isShown ->
                float_filter_issue.show()
            else ->
                float_filter_issue.hide()
        }
    }

    private fun getCurrentFragmentOfAboutRepo(): androidx.fragment.app.Fragment? {

        return (container.adapter as AboutRepoPageAdapter).registeredFragments[container.currentItem]

    }

    private fun openPopUp() {
        val currentFragment = getCurrentFragmentOfAboutRepo()
        if (currentFragment is IssueFragment) {
            if (issueFilterDialog.isShowing)
                return
            issueFilterDialog.show()
            val radioGroup = issueFilterDialog.find<RadioGroup>(R.id.radio_1)
            for (i in 0 until radioGroup.childCount) {
                val item = (radioGroup.getChildAt(i) as RadioButton)
                if (item.text.toString().contentEquals(getSelectedFilterType().toString())) {
                    item.isChecked = true
                }
                item.setOnCheckedChangeListener { buttonView, isChecked ->
                    onRadioSelected(issueFilterDialog, buttonView, isChecked)
                }
            }
        } else if (currentFragment is CommitsFragment) {
            if (commitBranchFilterDialog.isShowing)
                return
            commitBranchFilterDialog.show()
            val progressBar = commitBranchFilterDialog.findOptional<ProgressBar>(R.id.progressbar)
            progressBar?.visibility = View.VISIBLE
            subscriptions.add(model.getAllBranchOfRepo(itt.owner.login, itt.name) //Get Repo Details
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        progressBar?.visibility = View.GONE
                        setUpBranches(it)
                    }, {
                        onError(it)
                    }))


        }
    }

    private fun onRadioSelected(d: Dialog, item: View, isCheck: Boolean) {
        val issueFragment = getCurrentFragmentOfAboutRepo()
        if (issueFragment is IssueFragment) {
            if (isCheck) {
                when (item.id) {
                    R.id.radio_issue_all ->
                        issueFragment.onFilterSelected("all")
                    R.id.radio_open ->
                        issueFragment.onFilterSelected("open")
                    R.id.radio_close ->
                        issueFragment.onFilterSelected("closed")
                    else -> {
                    }
                }
                item.postDelayed({ d.cancel() }, 50)
            }
        }
    }

    private fun setUpBranches(it: List<Branch>) {
        (commitBranchFilterDialog.findOptional<RadioGroup>(R.id.rg_filter_commit))?.apply {
            removeAllViews()
            for (branch in it) {
                val r = RadioButton(this@AboutRepoActivity)
                addView(r)
                r.text = branch.name
                if (branch.name.contentEquals(getSelectedBranch().toString()))
                    r.isChecked = true
                r.setOnCheckedChangeListener { _, isChecked ->
                    val commitsFragment = getCurrentFragmentOfAboutRepo()
                    if (commitsFragment is CommitsFragment) {
                        if (isChecked) {
                            commitsFragment.onFilterSelected(branch.name)
                            this.tag = branch.name
                            postDelayed({ commitBranchFilterDialog.cancel() }, 50)
                        }
                    }
                }

            }
        }
    }

    private fun resetDialog() {
        val radioGroupC = commitBranchFilterDialog.findOptional<RadioGroup>(R.id.rg_filter_commit)
        radioGroupC?.let {
            it.tag = null
        }
        val radioGroupI = issueFilterDialog.findOptional<RadioGroup>(R.id.radio_1)
        radioGroupI?.let {
            (it.getChildAt(0) as RadioButton).isChecked = true
            (it.getChildAt(0) as RadioButton).tag = null
        }
    }


    inner class AboutRepoPageAdapter : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
        val registeredFragments = SparseArray<androidx.fragment.app.Fragment>()
        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            // val dataString = Gson().toJson(it)
            //bundle.putString("data", dataString)

            bundle.putString("owner", this@AboutRepoActivity.itt.owner.login)
            bundle.putString("repoName", this@AboutRepoActivity.itt.name)

            return when (position) {
                0 -> {
                    val fragment = IssueFragment()
                    bundle.putInt("index", 0)
                    fragment.arguments = bundle
                    return fragment
                }
                1 -> {
                    val fragment = WsFragment()
                    bundle.putInt("index", 1)
                    fragment.arguments = bundle
                    return fragment
                }
                2 -> {
                    val fragment = WsFragment()
                    bundle.putInt("index", 2)
                    fragment.arguments = bundle
                    return fragment
                }
                3 -> {
                    val fragment = ForkedByUserFragment()
                    bundle.putInt("index", 3)
                    fragment.arguments = bundle
                    return fragment
                }
                4 -> {
                    val fragment = CommitsFragment()
                    fragment.arguments = bundle
                    return fragment
                }
                //Case 5
                else -> {
                    val fragment = ContributorsFragment()
                    fragment.arguments = bundle
                    return fragment
                }


            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as androidx.fragment.app.Fragment
            registeredFragments.put(position, fragment)
            return fragment

        }

        override fun getCount(): Int {
            return 6
        }

        override fun getItemPosition(int: Any): Int {
            return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 ->
                    "Issues"
                1 ->
                    "Watch"
                2 ->
                    "Stars"
                3 ->
                    "Forks"
                4 ->
                    "Commits"
                5 ->
                    "Contributors"

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
