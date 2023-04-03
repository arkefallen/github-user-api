package com.dicoding.android.fundamental.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.android.fundamental.githubuser.data.entity.FavoriteUserEntity
import com.dicoding.android.fundamental.githubuser.repo.UserRepository

class FavoriteUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUser() : LiveData<List<FavoriteUserEntity>> = userRepository.getFavoriteUsers()
}