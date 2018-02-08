package com.jk.daggerrxkotlin.ui.adapters
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.daggerrxkotlin.network.api.User
import com.jk.daggerrxkotlin.extensions.loading
import kotlinx.android.synthetic.main.news_item.view.*
import kotlin.jk.com.dagger.R

/**
 * Created by M2353204 on 02/08/2017.
 */
class DataAdapter(val viewActions: onViewSelectedListener) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    private var datas = ArrayList<User>()

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent?.context)
        val view = inflator.inflate(R.layout.news_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.bind(datas[position])
    }


    fun addItems(items: List<User>){
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
        fun bind(item: User) = with(itemView) {
            img_thumbnail.loading(item.avatar_url)
            description.text = item.html_url
            author.text = item.login
            //comments.text = """${12} comments"""
            time.text = item.type

            itemView.setOnClickListener {
                viewActions.onItemSelected(item.html_url)
            }
        }
    }
}