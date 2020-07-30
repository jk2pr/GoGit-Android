package com.jk.gogit

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.SparseArray
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.jk.gogit.R.id.*
import com.jk.gogit.callbacks.OnFilterSelectedListener
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.OrgProfile
import com.jk.gogit.model.Repo
import com.jk.gogit.model.UserProfile
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.fragments.OrgPeopleFragment
import com.jk.gogit.ui.view.fragments.OrgRepoFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_org_profile.*
import kotlinx.android.synthetic.main.include_profile_header.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional

class OrgProfileActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeRefresh.isEnabled = verticalOffset == 0
    }

    private val dialog by lazy {
        val dialogLayout = View.inflate(this@OrgProfileActivity, R.layout.filter_org_repo, null)
        AlertDialog.Builder(this).apply {
            setView(dialogLayout)
        }.create()
    }

    var data: MutableMap<UserProfile, List<Repo>> = mutableMapOf()
    var selectedUser: String = "N/A"
    val onPageListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            when (position) {
                0 ->
                    showFab(true)
                else ->
                    showFab(false)
            }
        }

    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_org_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_follow?.visibility = View.GONE
        selectedUser = intent.getStringExtra("id")
        container?.adapter = PageAdapter(supportFragmentManager)
        tabs?.setupWithViewPager(container)
        float_search.setOnClickListener {
            if (it.isShown)
                openPopUp()
        }
        container.addOnPageChangeListener(onPageListener)

        container.post {
            onPageListener.onPageSelected(0)
        }

        swipeRefresh?.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_blue_bright),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_red_light))

        swipeRefresh?.setOnRefreshListener {
            updateUser()
        }

        updateUser()
    }

    private fun openPopUp() {
        if (dialog.isShowing)
            return
        dialog.show()
        /*val privateRadio = dialog.find<RadioButton>(R.id.radio_private)
        val publicRadio = dialog.find<RadioButton>(R.id.radio_public)
         if (isProfileSameAsLogin(selectedUser)) {
             privateRadio.visibility = View.VISIBLE
             publicRadio.visibility = View.VISIBLE
         } else {
             privateRadio.visibility = View.GONE
             publicRadio.visibility = View.GONE
         }*/
        val radioGroup = dialog.find<RadioGroup>(R.id.radio_1)
        for (i in 0 until radioGroup.childCount) {
            val item = (radioGroup.getChildAt(i) as RadioButton)
            if (item.text.toString().contentEquals(getSelectedFilterType().toString())) {
                item.isChecked = true
            }
            item.setOnCheckedChangeListener { buttonView, isChecked ->
                onRadioSelected(dialog, buttonView, isChecked)
            }
        }
    }

    private fun onRadioSelected(d: Dialog, item: View, isCheck: Boolean) {

        val a = (container.adapter as PageAdapter).registeredFragments[container.currentItem]
        if (a is OnFilterSelectedListener)
            if (isCheck) {
                when (item.id) {
                    radio_all ->
                        a.onFilterSelected("all")
                    radio_public ->
                        a.onFilterSelected("public")
                    radio_private ->
                        a.onFilterSelected("private")
                    radio_forks ->
                        a.onFilterSelected("forks")
                    radio_source ->
                        a.onFilterSelected("source")
                    radio_member ->
                        a.onFilterSelected("member")
                    else -> {
                    }
                }
                item.postDelayed({ d.cancel() }, 50)
            }
    }

    fun getSelectedFilterType(): StringBuffer? {
        val radioGroup = dialog.findOptional<RadioGroup>(R.id.radio_1)
        if (radioGroup != null)
            for (i in 0 until radioGroup.childCount) {
                val item = radioGroup.getChildAt(i) as RadioButton
                if (item.isChecked) {
                    return StringBuffer(item.text.trim())
                }
            }
        return StringBuffer("all")
    }

    fun showFab(isShown: Boolean) {

        if (isShown) float_search.show() else float_search.hide()
    }

    private fun removeAnimationIfAny() {
        txt_login.clearAnimation()
        txt_bio.clearAnimation()
        txt_company.clearAnimation()
        txt_location.clearAnimation()
        txt_email.clearAnimation()
        txt_blog.clearAnimation()
        btn_follow?.clearAnimation()
    }

    private fun createAnimation(): Animation {
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 1200
        fadeIn.fillAfter = true
        return fadeIn
    }


    private fun updateOrg(org: OrgProfile) {

        // showLoader(false)
        removeAnimationIfAny()
        container?.adapter?.notifyDataSetChanged()
        container.post {
            onPageListener.onPageSelected(0)
        }
        val fadeIn = createAnimation()
        profile?.loading(org.avatarUrl!!)
        enableHomeInToolBar(org.name, true)
        /* if (org.name.isNullOrEmpty()) txt_displayname.visibility = View.GONE else {
             txt_displayname.apply {
                 visibility = View.VISIBLE
                 startAnimation(fadeIn)
                 text = org.name
             }
         }*/
        txt_login.startAnimation(fadeIn)
        txt_login.visibility = View.VISIBLE
        txt_login.text = org.login

        if (org.description.isNullOrEmpty()) txt_bio?.visibility = View.GONE else {
            txt_bio.apply {
                startAnimation(fadeIn)
                visibility = View.VISIBLE
                text = org.description
            }
        }
        if (org.company.isNullOrEmpty()) txt_company.visibility = View.GONE else {
            txt_company?.apply {
                startAnimation(fadeIn)
                visibility = View.VISIBLE
                text = org.company
            }
        }
        if (org.location.isNullOrEmpty()) txt_location.visibility = View.GONE else {
            txt_location?.apply {
                startAnimation(fadeIn)
                visibility = View.VISIBLE
                text = org.location
            }
        }
        if (org.email.isNullOrEmpty()) txt_email.visibility = View.GONE else {
            txt_email?.apply {
                startAnimation(fadeIn)
                visibility = View.VISIBLE
                val span = Spannable.Factory.getInstance().newSpannable(org.email)
                span.setSpan(object : ClickableSpan() {
                    override fun onClick(v: View) {
                        email(org.email, "Send via GoGit Android", "")
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        // ds.color = ContextCompat.getColor(img_actor.context, R.color.colorPrimaryDark)
                        ds.isUnderlineText = true
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                }, 0, org.email.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                text = span
                movementMethod = LinkMovementMethod.getInstance()
            }

        }
        if (org.blog.isNullOrEmpty()) txt_blog.visibility = View.GONE else {
            txt_blog?.apply {
                startAnimation(fadeIn)
                visibility = View.VISIBLE
                // text = org.blog
                val span = Spannable.Factory.getInstance().newSpannable(org.blog)
                span.setSpan(object : ClickableSpan() {
                    override fun onClick(v: View) {
                        browse(org.blog)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        // ds.color = ContextCompat.getColor(img_actor.context, R.color.colorPrimaryDark)
                        ds.isUnderlineText = true
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                }, 0, org.blog.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                text = span
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
        //txt_organisations?.text = org.organizationsUrl
    }

    private fun updateUser() {
        val u = model.getOrgProfile(selectedUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            updateOrg(it)
            swipeRefresh.isRefreshing = false
        }, { it ->
            swipeRefresh.isRefreshing = false
            onError(it)

        }, {

        })
        subscriptions.add(u)
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_your_profile)?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        appbar?.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appbar?.removeOnOffsetChangedListener(this)
    }

    inner class PageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        var registeredFragments = SparseArray<Fragment>()


        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString("id", selectedUser)
            return when(position){
                0 -> {
                    val fragment = OrgRepoFragment()
                    fragment.arguments = bundle
                    fragment
                }
                else -> {
                    val fragment = OrgPeopleFragment()
                    fragment.arguments = bundle
                    fragment
                }
            }

        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as androidx.fragment.app.Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            registeredFragments.remove(position)

        }

        override fun getCount(): Int {
            return 2
        }

        override fun getItemPosition(int: Any): Int {
            return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 ->
                    "Repositories"
                1 ->
                    "People"
                else ->
                    null

            }
        }

    }
    /*   fun showLoader(isLoading: Boolean) {
        if (isLoading) {
             recyclerView?.visibility = View.GONE
             progressbar?.visibility = View.VISIBLE

         } else {
             recyclerView?.visibility = View.VISIBLE
             progressbar?.visibility = View.GONE
         }

    }*/
}


