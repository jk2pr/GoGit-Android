package com.jk.gogit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.jk.gogit.model.Notification
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(val viewActions: OnViewSelectedListener) : androidx.recyclerview.widget.RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var data = ArrayList<Notification>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }


    fun addItems(items: List<Notification>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()

    }

    override fun getItemCount(): Int {
        return data.size
    }
    interface OnViewSelectedListener {

        fun onNotificationClicked(item: Notification)
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: Notification) = with(itemView) {
            txt_repo_name.text = item.repository.fullName
            txt_notification_titile.text = item.subject.title
            when (item.subject.type) {
                "Issue" ->
                    img_notification_type.setImageResource(R.drawable.ic_issue_green)
                "PullRequest" ->
                    img_notification_type.setImageResource(R.drawable.ic_source_fork)

            }
            when (item.unread){
                true -> itemView.setBackgroundColor(ContextCompat.getColor(this.context,R.color.notification_template_icon_bg))
                false -> itemView.setBackgroundColor(ContextCompat.getColor(this.context,R.color.white))
            }
            itemView.setOnClickListener {
                viewActions.onNotificationClicked(item)
            }

        }
    }
}
