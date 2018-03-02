package com.jk.gogit.ui.adapters
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Feed
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(val viewActions: onViewSelectedListener?) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var datas = ArrayList<Feed>()

    interface onViewSelectedListener {
        fun onItemSelected(id: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent?.context)
        val view = inflator.inflate(R.layout.feed_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.bind(datas[position])
    }

    fun getAllItems():List<Feed>{
        return datas
    }

    fun addItems(items: List<Feed>){
        datas.addAll(items)
        notifyDataSetChanged()
    }
    fun clearItems(){
        datas.clear()

    }
    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Feed) = with(itemView) {
            img_actor.loading(item.actor.avatar_url)

            val actorName=item.actor.display_login
            val repoName=item.repo.name
            val eventName=getEventNameByType(item.type)
            txt_activity.text= Html.fromHtml("<b>"+actorName+ "</b>"+ " " + eventName + " " + "<b>"+repoName+"</b>")
            img_actor.setOnClickListener {
                viewActions?.onItemSelected(item.actor.login)
            }
            itemView.setOnClickListener {
                //viewActions?.onItemSelected(item.url)
            }
        }
    }
    fun getEventNameByType(eventName:String): String{
        return when(eventName) {
            "CreateEvent" ->
                "created a Repository"
            "ForkEvent"->
                "forked a Repository"
            "WatchEvent" ->
                "started following"
            "PushEvent" ->
                "pushed to"
            else -> "unKnow"
        }
    }
}