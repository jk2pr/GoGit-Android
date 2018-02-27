package com.jk.gogit.di.modules

import android.arch.persistence.room.Room
import com.jk.gogit.application.MyApplication
import com.jk.gogit.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jitendra on 08/11/2017.
 */

@Module
class DBModule(val app: MyApplication){
    @Provides
    @Singleton
    fun getAppDB(): AppDatabase  {
        return Room.databaseBuilder(app,
                AppDatabase::class.java, "database-name").build()

    }
}