/*
package com.jk.gogit.ui.view.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.R.id.tabs
import com.jk.gogit.SearchActivity
import com.jk.gogit.ui.view.RepoDetailsActivity
import kotlinx.android.synthetic.main.fragment_about_repo.*


*/
/**
 * A simple [Fragment] subclass.class
 *
 *//*

class AboutRepo : Fragment() {

    lateinit var holdingActivity: RepoDetailsActivity
    val repo by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("id") }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as RepoDetailsActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_repo, container, false)
    }

    fun getViewPager(): ViewPager {
        return container
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container?.adapter = AboutRepoPageAdapter()
        tabs?.setupWithViewPager(container)
        container.offscreenPageLimit = 3
           }

    inner class AboutRepoPageAdapter : FragmentPagerAdapter(childFragmentManager) {
        val registeredFragments = SparseArray<Fragment>()
        override fun getItem(position: Int): Fragment? {
            val bundle = Bundle()
            // val dataString = Gson().toJson(it)
            //bundle.putString("data", dataString)

            bundle.putString("id", this@AboutRepo.owner)
            bundle.putString("repoName", this@AboutRepo.repo)

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
                5 -> {
                    val fragment = ContributorsFragment()
                    fragment.arguments = bundle
                    return fragment
                }
                else ->
                    null
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment

        }

        override fun getCount(): Int {
            return 6
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
}
*/
