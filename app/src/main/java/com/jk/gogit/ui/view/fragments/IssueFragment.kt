package com.jk.gogit.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.callbacks.OnFilterSelectedListener
import com.jk.gogit.model.Issue
import com.jk.gogit.spans.IssueLabelSpan
import com.jk.gogit.ui.view.AboutRepoActivity
import com.jk.gogit.ui.view.BaseActivity.Companion.VISIBLE_THRESHOLD
import com.jk.gogit.ui.view.BaseActivity.Companion.sMaxRecord
import com.jk.gogit.utils.DateUtil
import com.jk.gogit.utils.NavUtils.redirectToIssueDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_issues.*
import kotlinx.android.synthetic.main.item_issue.view.*

class IssueFragment : BaseFragment(), IssueAdapter.OnItemSelectListener, OnFilterSelectedListener {

    private lateinit var holdingActivity: AboutRepoActivity
    val repo by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("owner") }
    private val type by lazy { holdingActivity.getSelectedFilterType()!! }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        holdingActivity = activity as AboutRepoActivity
    }


    override fun onItemClicked(issue: Issue) {
        redirectToIssueDetails(holdingActivity, owner, repo, issue.number)
        /*   startActivity(holdingActivity.intentFor<IssueDetailActivity>
           (("owner" to owner),
                   ("repoName" to repo),
                   ("i_data" to Gson().toJson(issue))))*/
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_issues, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_issues.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = IssueAdapter(this@IssueFragment)
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView,
                                        dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = recyclerView_issues.layoutManager!!.itemCount
                    lastVisibleItem = (recyclerView_issues.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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

    fun loadPage() {
        val model = holdingActivity.model
        if (pageNumber in 1..lastPage) {
            if (pageNumber == 1)
                showLoader(true)
            holdingActivity.subscriptions.add(model.getIssues(owner!!,
                    repo!!,
                    "*",
                    type.toString(), pageNumber, sMaxRecord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showLoader(false)
                        lastPage = holdingActivity.handlePagination(it.headers())
                        if (it.body()!!.isEmpty()) {

                        }
                        // holdingActivity.showSnackForNoData()
                        (recyclerView_issues.adapter as IssueAdapter).apply {
                            if (pageNumber == 1)
                                clearItems()
                            addItems(it.body()!!)
                            loading = false

                        }
                    }, {
                        showLoader(false)
                        holdingActivity.onError(it)
                        it.printStackTrace()

                    }, {

                    }))
        }
    }

    override fun onFilterSelected(name: String) {
        type.delete(0, type.length)
        type.append(name)
        pageNumber = 1
        loadPage()
    }


    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        recyclerView_issues?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isAdded)
            if (isVisibleToUser)
                holdingActivity.showFab(true)
            else
                holdingActivity.showFab(false)
    }
}

private open class IssueAdapter(val viewActions: OnItemSelectListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    private var dataList = ArrayList<Issue>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_issue, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addItems(items: List<Issue>) {
        dataList.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        dataList.clear()

    }


    override fun onBindViewHolder(holder: IssueAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])

    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: Issue) = with(itemView) {

            if (item.title.isNotEmpty())
                txt_issue_title.text = item.title
            else
                txt_issue_title.visibility = View.GONE

            txt_reply_count.text = item.comments.toString()
            val sta = "#${item.number} opened on ${DateUtil.getDateComparatively(item.createdAt)} by ${item.user.login}"
            txt_issue_status.text = sta
            if (item.state.contentEquals("open"))
                img_issue.setImageResource(R.drawable.ic_issue_green)
            else
                img_issue.setImageResource(R.drawable.ic_issue_red)

            if (item.labels.isNotEmpty()) {
                for (label in item.labels) {
                    val text = SpannableStringBuilder(label.name)
                    text.setSpan(IssueLabelSpan(itemView.context, label),
                            0, label.name.length, 0)
                    txt_issue_label.text = text
                    // txt_issue_label.background= Color.parseColor(item.labels[0].color)
                }
            }
            itemView.setOnClickListener {
                viewActions?.onItemClicked(item)

            }
        }
    }


    interface OnItemSelectListener {
        fun onItemClicked(issue: Issue)

    }
}