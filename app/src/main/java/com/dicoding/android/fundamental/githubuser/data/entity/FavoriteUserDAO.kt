package com.dicoding.android.fundamental.githubuser.data.entity

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.android.fundamental.githubuser.data.response.User

@Dao
interface FavoriteUserDAO {
    @Query("SELECT * FROM favorite_user")
    fun getUser() : LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM favorite_user WHERE username = :username")
    fun getUserByUsername(username: String) : LiveData<FavoriteUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: FavoriteUserEntity)

    @Query("DELETE FROM favorite_user WHERE username = :username")
    suspend fun deleteUser(username: String)
}