package com.jk.gogit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.gogit.R
import com.jk.gogit.model.File
import com.jk.gogit.utils.Utils
import kotlinx.android.synthetic.main.item_file_list.view.*

class FileAdapter(val viewActions: FileAdapter.onViewSelectedListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    private var datas = ArrayList<File>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_file_list, parent, false)
        return ViewHolder(view)
    }

    fun addItems(items: List<File>) {
        datas.addAll(items.sortedWith(compareBy { it.type }))
        notifyDataSetChanged()
    }

    fun clearItems() {
        datas.clear()

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas.get(position))
    }

    interface onViewSelectedListener {
        fun onItemSelected(file: File)

    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: File) = with(itemView) {
            txt_folder_name.text = item.name
            if (Utils.isDir(item)) {
                img_folder.setImageResource((R.drawable.folder_selector))
                txt_file_size.text = null
            } else {
                img_folder.setImageResource((R.drawable.file_selector))
                txt_file_size.text = String.format(" %S %S", item.size, "KB")
            }
            itemView.setOnClickListener {
                viewActions?.onItemSelected(item)
            }
        }
    }
}