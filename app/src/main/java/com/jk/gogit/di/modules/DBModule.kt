package com.jk.gogit.di.modules

import android.content.Context
import androidx.room.Room
import com.jk.gogit.db.AppDatabase
import com.jk.gogit.db.suggestions.DBCONSTANTS
import com.jk.gogit.db.suggestions.SuggestionDB
import com.jk.gogit.db.suggestions.SuggestionDB.Companion.MIGRATION1_2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jitendra on 08/11/2017.
 */

@Module
class DBModule(val app: Context) {
    @Provides
    @Singleton
    fun getAppDB(): AppDatabase {
        return Room.databaseBuilder(app,
                AppDatabase::class.java, "database-name").build()

    }

    @Provides
    @Singleton
    fun getSuggestionDB(): SuggestionDB {
        return Room.databaseBuilder(app,
                SuggestionDB::class.java, DBCONSTANTS.DB_NAME).addMigrations(MIGRATION1_2).build()

    }
}