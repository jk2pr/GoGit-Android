package com.jk.gogit.db.suggestions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [(Index(value = [(DBCONSTANTS.COLUMN_NAME)], unique = true))])
data class Suggestion(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(index = true, name = DBCONSTANTS.COLUMN_ID)
        val sid: Long,

        @ColumnInfo(name = DBCONSTANTS.COLUMN_NAME)
        val sName: String
)
