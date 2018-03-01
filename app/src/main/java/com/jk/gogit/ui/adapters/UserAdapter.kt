package com.jk.gogit.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.network.api.User
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Users
import kotlinx.android.synthetic.main.news_item.view.*
import kotlinx.android.synthetic.main.user_item.view.*


class UserAdapter(val viewActions: OnViewSelectedListener) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var data = ArrayList<Users>()

    interface OnViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent?.context)
        val view = inflator.inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.bind(data[position])
    }


    fun addItems(items: List<Users>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Users) = with(itemView) {
            img_user.loading(item.avatar_url!!)
            txt_display_name.text=item.login
           // txt_bio.text = item.login
            //comments.text = """${12} comments"""
            //time.text = item.type

            itemView.setOnClickListener {
                viewActions.onItemSelected(item.html_url)
            }
        }
    }
}