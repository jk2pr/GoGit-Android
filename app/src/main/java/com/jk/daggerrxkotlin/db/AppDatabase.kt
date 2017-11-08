package com.jk.daggerrxkotlin.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jk.daggerrxkotlin.api.User

/**
 * Created by Jitendra on 08/11/2017.
 */

    @Database(entities = arrayOf(User::class), version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }
