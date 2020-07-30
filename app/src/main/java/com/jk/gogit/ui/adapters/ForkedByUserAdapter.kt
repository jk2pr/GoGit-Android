package com.jk.gogit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import com.jk.gogit.model.RepoForked
import kotlinx.android.synthetic.main.item_forked_by_user.view.*

class ForkedByUserAdapter(val viewActions: OnViewSelectedListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<ForkedByUserAdapter.ViewHolder>() {
        private var datas = ArrayList<RepoForked>()

        interface OnViewSelectedListener {
            fun onItemSelected(txtRepo: TextView, owner: String?)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflator = LayoutInflater.from(parent.context)
            val view = inflator.inflate(R.layout.item_forked_by_user, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(datas[position])
        }

        fun getAllItems(): List<RepoForked> {
            return datas
        }

        fun addItems(items: List<RepoForked>) {
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
            fun bind(item: RepoForked) = with(itemView) {
                img_user?.loading(item.owner?.avatarUrl!!)
                txt_display_name?.text=item.owner?.login
                itemView.setOnClickListener {
                    viewActions?.onItemSelected(txt_display_name, item.owner?.login)
                }
            }
        }
    }