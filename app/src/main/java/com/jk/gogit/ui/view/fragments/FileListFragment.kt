package com.jk.gogit.ui.view.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jk.gogit.R
import com.jk.gogit.model.File
import com.jk.gogit.ui.adapters.FileAdapter
import com.jk.gogit.ui.view.FullFileActivity
import com.jk.gogit.ui.view.RepoDetailsActivity
import com.jk.gogit.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_file_list.*
import kotlinx.android.synthetic.main.item_file_path.view.*
import org.jetbrains.anko.intentFor


class FileListFragment : androidx.fragment.app.Fragment(), FileAdapter.onViewSelectedListener, FilePathAdapter.OnPathSelectListener {
    //val it: RepositoryDetails by lazy { Gson().fromJson(arguments?.getString("data"), RepositoryDetails::class.java) }
    val holdingActivity by lazy { activity as RepoDetailsActivity }
    val repoName by lazy { arguments?.getString("repoName") }
    val owner by lazy { arguments?.getString("owner") }
    override fun onPathSelected(index: Int, file: FileListFragment.PathToFile): Boolean {
        /* if (index==0)
             txt_read_me.visibility=View.VISIBLE*/
        val ada = (recyclerView_file_path.adapter as FilePathAdapter)
        //Remove from filepath adapter horizontal list
        val b = ada.removeTopItem(index + 1)
        val fAdapter = (recyclerView_file.adapter as FileAdapter)
        //clear items from file adapter add older list and notify for changes
        fAdapter.clearItems()
        fAdapter.addItems(file.file)
        return b == null

    }

    inner class PathToFile {
        var path: String = "."
        var file: List<File> = ArrayList()
    }


    override fun onItemSelected(file: File) {
        /*if(txt_read_me?.visibility == View.VISIBLE)
        {
            txt_read_me.visibility=View.GONE
        }*/
        if (Utils.isDir(file)) {
            doRequest(file.path)
        } else
            startActivity(activity?.intentFor<FullFileActivity>(
                    ("path" to file.path),
                    ("gitUrl" to file.gitUrl),
                    ("login" to owner),
                    ("repoName" to repoName),
                    ("file_sha" to file.sha)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file_list, container, false)
    }

    fun showLoader(isLoading: Boolean) {
        progressbar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        main_content?.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_file.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = FileAdapter(this@FileListFragment)
            //  val selectedUser = arguments?.getString("id") as String
        }
        val lManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        lManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
        lManager.stackFromEnd = true
        // lManager.reverseLayout = true
        recyclerView_file_path.apply {
            setHasFixedSize(true)
            layoutManager = lManager
            adapter = FilePathAdapter(this@FileListFragment)
        }
        doRequest("")

    }

    private fun doRequest(path: String) {
        val model = holdingActivity.model
        holdingActivity.subscriptions.add(model.getFile(owner!!, repoName!!, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoader(false)
                    (recyclerView_file.adapter as FileAdapter).apply {
                        clearItems()
                        addItems(it)
                        val current = PathToFile()
                        current.path = path
                        current.file = it
                        (recyclerView_file_path.adapter as FilePathAdapter).apply {
                            addItemIfNot(current)
                            recyclerView_file_path.post {
                                recyclerView_file_path.smoothScrollToPosition(itemCount - 1)
                                recyclerView_file.minimumHeight = recyclerView_file.height
                            }
                        }
                    }

                }, {
                    holdingActivity.onError(it)
                    showLoader(false)
                }))
    }


}


class FilePathAdapter(val viewActions: OnPathSelectListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<FilePathAdapter.ViewHolder>() {

    var dataList = ArrayList<FileListFragment.PathToFile>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilePathAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_path, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    fun removeTopItem(fromIndex: Int): FileListFragment.PathToFile? {
        var dataRemoved: FileListFragment.PathToFile? = null
        for (item in (dataList.lastIndex) downTo fromIndex) {
            if (item >= dataList.size)
                return null
            dataRemoved = dataList.removeAt(item)
        }
        if (dataRemoved != null)
            notifyDataSetChanged()
        return dataRemoved

    }


    fun addItemIfNot(d: FileListFragment.PathToFile) {
        if (dataList.contains(d))
            return
        dataList.add(d)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FilePathAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])

    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: FileListFragment.PathToFile) = with(itemView) {
            var finalText = item.path
            if (item.path.contains("/")) {
                val lastIndex = item.path.lastIndexOf("/")
                finalText = item.path.substring(lastIndex).removePrefix("/")
            }
            txt_path.text = finalText
            txt_path.tag = item.file
            itemView.setOnClickListener {
                viewActions?.onPathSelected(dataList.indexOf(item), item)
            }
        }
    }


    interface OnPathSelectListener {
        fun onPathSelected(index: Int, file: FileListFragment.PathToFile): Boolean

    }


}
