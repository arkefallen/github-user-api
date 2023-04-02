package com.dicoding.android.fundamental.githubuser.di

import android.content.Context
import com.dicoding.android.fundamental.githubuser.data.entity.GithubUserDatabase
import com.dicoding.android.fundamental.githubuser.data.remote.APIConfig
import com.dicoding.android.fundamental.githubuser.repo.UserRepository

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val apiService = APIConfig.getService()
        val database = GithubUserDatabase.getInstance(context)
        val favoriteUserDAO = database.favoriteUserDAO()
        return UserRepository.getInstance(apiService, database, favoriteUserDAO)
    }
}