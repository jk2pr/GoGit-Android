package com.jk.daggerrxkotlin.di.modules

import android.arch.persistence.room.Room
import com.jk.daggerrxkotlin.application.MyApplication
import com.jk.daggerrxkotlin.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jitendra on 08/11/2017.
 */

@Module
public class DBModule(val app: MyApplication){
    @Provides
    @Singleton
    fun getAppDB(): AppDatabase  {
        return Room.databaseBuilder(app,
                AppDatabase::class.java, "database-name").build();

    }
}