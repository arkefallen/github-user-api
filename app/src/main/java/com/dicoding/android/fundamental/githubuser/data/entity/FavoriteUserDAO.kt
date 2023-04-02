package com.dicoding.android.fundamental.githubuser.data.entity

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.android.fundamental.githubuser.data.response.User

@Dao
interface FavoriteUserDAO {
    @Query("SELECT * FROM favorite_user")
    fun getUser() : LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(users: List<FavoriteUserEntity>)

}