package com.dicoding.android.fundamental.githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.android.fundamental.githubuser.data.remote.APIConfig
import com.dicoding.android.fundamental.githubuser.data.response.GithubResponse
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.repo.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getLoading() = userRepository.isLoading
    fun getListUsers() = userRepository.users
    fun setUsers(username: String) {
        userRepository.setUsers(username)
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}