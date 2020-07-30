package com.jk.gogit.ui.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jk.gogit.R
import com.jk.gogit.db.suggestions.DBCONSTANTS

/*Context context, int layout, Cursor c, String[] from,
int[] to, int flags*/
class SearchSuggestionAdapter( context: Context?, layout: Int, c: Cursor?, from: Array<String>, to: IntArray, autoRequery: Int) :
        androidx.cursoradapter.widget.SimpleCursorAdapter(context, layout, c, from, to, autoRequery) {

    init {

    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup): View {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_suggestion, parent, false)
        return view
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor) {
        val value = cursor.getString(cursor.getColumnIndexOrThrow(DBCONSTANTS.COLUMN_NAME))
        view?.findViewById<TextView>(R.id.txt_s_name)?.text = value
       /* view?.setOnClickListener {
            onItemClick.onSuggestionClick(value)
        }*/
    }

}