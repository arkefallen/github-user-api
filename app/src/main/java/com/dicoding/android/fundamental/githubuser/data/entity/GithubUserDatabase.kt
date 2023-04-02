package com.dicoding.android.fundamental.githubuser.data.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDAO() : FavoriteUserDAO

    companion object {
        @Volatile
        private var instance : GithubUserDatabase? = null

        fun getInstance(context: Context) : GithubUserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserDatabase::class.java,
                    "GithubUser.db"
                ).build()
            }
    }
}