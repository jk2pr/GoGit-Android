package com.jk.gogit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.Users
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter(val viewActions: OnViewSelectedListener) : androidx.recyclerview.widget.RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var data = ArrayList<Users>()

    interface OnViewSelectedListener {
        fun onItemSelected(login: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
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

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: Users) = with(itemView) {
            if (item.type!!.toLowerCase().contentEquals("Anonymous".toLowerCase())) {
                if (item.login.isNullOrEmpty())
                    txt_display_name?.text = item.type
                else
                    txt_display_name?.text = item.login
                if (item.avatar_url.isNullOrEmpty())
                    img_user.setImageResource(R.drawable.ic_unknown)
                else
                    img_user?.loading(item.avatar_url)
                itemView.setOnClickListener {
                    Snackbar.make(itemView, "This is a anonymous user ", Snackbar.LENGTH_LONG).show()
                }

            } else {
                img_user?.loading(item.avatar_url!!)
                txt_display_name?.text = item.login
                itemView.setOnClickListener {
                    viewActions.onItemSelected(item.login!!)
                }
            }
        }
    }

}