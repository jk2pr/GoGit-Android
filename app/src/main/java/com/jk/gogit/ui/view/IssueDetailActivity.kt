package com.jk.gogit.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.jk.gogit.R
import com.jk.gogit.model.Issue
import com.jk.gogit.model.TimeLine
import com.jk.gogit.ui.Type
import com.jk.gogit.ui.adapters.IssueTimeLineAdapter
import com.jk.gogit.utils.DateUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_issue_detail.*


class IssueDetailActivity : BaseActivity() {
    private var data = ArrayList<TimeLine>()

    private val issueNumber by lazy { intent.getIntExtra("issueNumber", 0) }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val owner = intent.getStringExtra("owner")
        val repo = intent.getStringExtra("repoName")
        var iData: Issue? = null
        recycle_issue_detail.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = IssueTimeLineAdapter()
            isNestedScrollingEnabled = false

        }

        enableHomeInToolBar("ISSUE## $issueNumber", true)
        subscriptions.add(model.getSingleIssue(owner, repo, issueNumber.toString())
                .flatMap {
                    iData = it
                    model.getIssueTimeline(owner, repo, issueNumber.toString())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //Original Issue
                    txt_issue_title.text = iData?.title
                    txt_label.apply {
                        setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_issue_white), null, null, null)
                        compoundDrawablePadding = 5
                        if (iData?.state!!.toLowerCase().contentEquals(getString(R.string.open).toLowerCase()))
                            setBackgroundResource(R.drawable.rounded_green_button)
                        else
                            setBackgroundResource(R.drawable.rounded_red_button)

                        //inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
                        text = with(iData?.state!!) {
                            replace(first(), first().toUpperCase())
                        }


                    }
                    txt_cs.text = String.format(resources.getString(R.string.issue_openned),
                            iData?.user?.login, DateUtil.getDateComparatively(iData?.createdAt!!))


                    //All Comments
                    data.add(makeCustomData(iData!!))
                    data.addAll(it.filter {
                        Type.values().contains(it.event)
                    })
                    (recycle_issue_detail.adapter as IssueTimeLineAdapter).addItems(data)

                }, {
                    onError(it)
                    it.printStackTrace()

                }, {

                }))

    }

    private fun makeCustomData(issue: Issue): TimeLine {
        val timeLine = TimeLine(
                issue, 0,
                "", "",
                null, null, null,
                "", "",
                "", "",
                null, "", "", "",
                null, "",
                "", "",
                null, null)
        timeLine.run {
            id = issue.id
            url = issue.url
            actor = TimeLine.Actor(
                    issue.user.login,
                    issue.user.id,
                    "",
                    issue.user.avatarUrl,
                    issue.user.gravatarId,
                    issue.user.url,
                    issue.user.htmlUrl,
                    issue.user.followersUrl,
                    issue.user.followingUrl,
                    issue.user.gistsUrl,
                    issue.user.starredUrl,
                    issue.user.subscriptionsUrl,
                    issue.user.organizationsUrl,
                    issue.user.reposUrl,
                    issue.user.eventsUrl,
                    issue.user.receivedEventsUrl,
                    issue.user.type,
                    issue.user.siteAdmin)
            label = label
            //event = issue.eventsUrl
            body = if (issue.body_html.isNullOrBlank()) {
                issue.body_html
            } else {
                issue.body
            }
            createdAt = issue.createdAt
            htmlUrl = issue.htmlUrl

        }
        return timeLine

    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_issue_detail
    }


}
