package com.dicoding.android.fundamental.githubuser.data.entity

import androidx.room.*

@Entity(tableName = "favorite_user")
data class FavoriteUserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

)

