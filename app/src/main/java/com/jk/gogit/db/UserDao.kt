package com.jk.gogit.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.jk.gogit.network.api.User
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

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user WHERE login LIKE :nameText")
    fun getUserList(nameText: String): Flowable<List<User>>
}