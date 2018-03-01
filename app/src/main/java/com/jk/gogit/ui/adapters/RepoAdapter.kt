package com.jk.gogit.ui.adapters
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.model.Repo
import kotlinx.android.synthetic.main.repo_item.view.*

/**
 * Created by M2353204 on 02/08/2017.
 */
class RepoAdapter(val viewActions: onViewSelectedListener?) : RecyclerView.Adapter<RepoAdapter.ViewHolder>() {
    private var datas = ArrayList<Repo>()

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent?.context)
        val view = inflator.inflate(R.layout.repo_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.bind(datas[position])
    }

    fun getAllItems():List<Repo>{
        return datas
    }

    fun addItems(items: List<Repo>){
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
        fun bind(item: Repo) = with(itemView) {

            txt_display_name.text = item.fullName
            txt_bio.text = item.language
            //comments.text = """${12} comments"""
            txt_repoStars.text = item.stargazersCount.toString()
            txt_repoForks.text=item.forksCount.toString()


            itemView.setOnClickListener {
                viewActions?.onItemSelected(item.url)
            }
        }
    }
}