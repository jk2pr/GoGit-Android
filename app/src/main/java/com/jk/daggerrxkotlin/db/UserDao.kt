package com.jk.daggerrxkotlin.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.jk.daggerrxkotlin.api.User
import io.reactivex.Flowable

/**
 * Created by Jitendra on 08/11/2017.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllPeople(): Flowable<List<User>>
    @Insert
    fun insert(user: List<User>)

  /*  @Query("SELECT * FROM user WHERE login LIKE :nameText")
    fun getUserList(text: String): Flowable<List<User>>*/
}