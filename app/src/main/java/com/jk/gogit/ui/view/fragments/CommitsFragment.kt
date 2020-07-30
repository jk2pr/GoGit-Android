package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jk.gogit.R
import com.jk.gogit.callbacks.OnFilterSelectedListener
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.commits.CommitData
import com.jk.gogit.ui.view.AboutRepoActivity
import com.jk.gogit.ui.view.BaseActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.utils.DateUtil
import com.jk.gogit.utils.NavUtils.redirectToProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commits.*
import kotlinx.android.synthetic.main.item_commit.view.*

class CommitsFragment : BaseFragment(), CommitAdapter.OnItemSelectListener, OnFilterSelectedListener {


    lateinit var holdingActivity: AboutRepoActivity
    // val it: RepositoryDetails by lazy { Gson().fromJson(arguments?.getString("data"), RepositoryDetails::class.java) }
    val repo by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("owner") }
    private val branch: StringBuffer by lazy { holdingActivity.getSelectedBranch() }
    override fun onItemClicked(id: String) {

        redirectToProfile(holdingActivity,id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as AboutRepoActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_commits.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = CommitAdapter(this@CommitsFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_commits.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_commits.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                        pageNumber++
                        loading = true
                        loadPage()
                    }
                }
            })
        }
        loadPage()
    }

    override fun onFilterSelected(name: String) {
        branch.delete(0, branch.length)
        branch.append(name)
        pageNumber = 1
        loadPage()
    }

    fun loadPage() {
        val model = holdingActivity.model
        if (pageNumber in 1..lastPage) {
            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(model.getCommits(owner!!,
                    repo!!,
                    branch.toString(),
                    pageNumber, BaseActivity.sMaxRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showLoader(false)
                        lastPage = holdingActivity.handlePagination(it.headers())
                        (recyclerView_commits?.adapter as CommitAdapter).apply {
                            if (pageNumber == 1)
                                clearItems()
                            addItems(it.body()!!)
                            loading = false
                        }
                    }, {
                        showLoader(false)
                        holdingActivity.onError(it)

                    }, {

                    }))
        }
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_commits?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

}

private open class CommitAdapter(val viewActions: OnItemSelectListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<CommitAdapter.ViewHolder>() {

    private var dataList = ArrayList<CommitData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_commit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addItems(items: List<CommitData>) {
        dataList.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        dataList.clear()

    }


    override fun onBindViewHolder(holder: CommitAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])

    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: CommitData) = with(itemView) {

            txt_commit_message?.text = item.commit.message
            item.author?.let {
                img_committer.setOnClickListener {_->
                    viewActions?.onItemClicked(item.author.login)
                }
                val span = Spannable.Factory.getInstance().newSpannable("${it.login} committed ${DateUtil.getDateComparatively(item.commit.committer.date)}")
                span.setSpan(object : ClickableSpan() {
                    override fun onClick(v: View) {
                        viewActions?.onItemClicked(it.login)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = ContextCompat.getColor(img_committer.context, R.color.colorTextDark)
                        ds.isUnderlineText = false
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                }, 0, it.login.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                txt_committer_name?.text = span
                txt_committer_name?.movementMethod = LinkMovementMethod.getInstance()

            }
            item.author?.avatarUrl?.let {
                img_committer?.loading(it)
            }

            txt_reply_count.text = item.commit.commentCount.toString()
            itemView.setOnClickListener {
                // viewActions?.onItemClicked(item)

            }
        }
    }


    interface OnItemSelectListener {
        fun onItemClicked(id: String)

    }
}