package com.jk.gogit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Feed
import com.jk.gogit.utils.DateUtil
import kotlinx.android.synthetic.main.item_feed.view.*
import java.util.*


class FeedAdapter(val viewActions: OnViewSelectedListener) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_feed, viewGroup, false)
        return ViewHolder(view)

    }


    private var datas = ArrayList<Feed>()

    interface OnViewSelectedListener {
        fun onActorNameClicked(imageView: ImageView, loginId: String)
        fun onRepNameClicked(textView: TextView, owner: String)
    }


    fun addItems(items: List<Feed>) {
        datas.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        datas.clear()

    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: Feed) = with(itemView) {
            img_actor.loading(item.actor.avatar_url)
            val actorName = item.actor.display_login
            val repoName = item.repo.name
            val eventName = getEventNameByType(item.type)
            val longText = actorName + " " + eventName + " " + repoName
            val charSequence = DateUtil.getDateComparatively(item.created_at)
            txt_time.text = charSequence
            img_actor.setOnClickListener {
                viewActions.onActorNameClicked(img_actor, item.actor.login)
            }

          //  val start = longText.indexOf(actorName)
          //  val end = start + actorName.length
          //  val bss = StyleSpan(Typeface.BOLD)
            //  val span = Spannable.Factory.getInstance().newSpannable(longText)
            //  span.setSpan(bss,start, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            /* span.setSpan(object : ClickableSpan() {
                 override fun onClick(v: View) {
                     viewActions.onActorNameClicked(img_actor, item.actor.login)
                 }

                 override fun updateDrawState(ds: TextPaint) {
                     ds.color = ContextCompat.getColor(img_actor.context, R.color.colorPrimaryDark)
                     ds.isUnderlineText = false
                     ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                 }
             }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/

  //          val startRepo = longText.indexOf(repoName)
//            val endRepo = startRepo + repoName.length

            //   span.setSpan(bss,startRepo, endRepo,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            /*span.setSpan(object : ClickableSpan() {
               override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(img_actor.context, R.color.colorPrimaryDark)
                    ds.isUnderlineText = false
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            }, startRepo, endRepo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/
            txt_activity.text = longText
            //txt_activity.movementMethod = LinkMovementMethod.getInstance()

            itemView.setOnClickListener {
                viewActions.onRepNameClicked(txt_activity, item.repo.name)
            }

        }


    }

    fun getEventNameByType(eventName: String): String {
        return when (eventName) {
            "CreateEvent" ->
                "created a Repository"
            "ForkEvent" ->
                "forked a Repository"
            "WatchEvent" ->
                "started following"
            "PushEvent" ->
                "pushed to"
            "PullRequestEvent" ->
                "Closed pull request"
            "PublicEvent" ->
                "Made public"
            else -> {
                eventName
            }
        }
    }
}