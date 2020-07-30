package com.jk.gogit.db.suggestions

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [(Suggestion::class)], version = 2)
abstract class SuggestionDB : RoomDatabase() {
    abstract fun suggestionDao(): SuggestionDao

    companion object {
        val MIGRATION1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                print(database.version)
            }

        }
    }
}