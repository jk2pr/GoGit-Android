package com.jk.gogit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jk.gogit.model.search.User

/**
 * Created by Jitendra on 08/11/2017.
 */

    @Database(entities = [(User::class)], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }
