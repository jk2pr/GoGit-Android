package com.jk.gogit.db.suggestions

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SuggestionDao {

    @Query("SELECT * FROM Suggestion")
    fun getAllSuggestion(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(suggestion: Suggestion): Long

    @Query("DELETE FROM Suggestion")
    fun deleteAllSuggestion()

    @Query("SELECT * FROM Suggestion WHERE Name LIKE :nameText")
    fun getSuggestionList(nameText: String): Cursor

    @Query("SELECT * FROM Suggestion ")
    fun getAllSuggestionList(): Cursor

}
