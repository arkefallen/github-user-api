package com.dicoding.android.fundamental.githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.android.fundamental.githubuser.data.entity.FavoriteUserEntity
import com.dicoding.android.fundamental.githubuser.data.remote.APIConfig
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.data.response.UserDetailResponse
import com.dicoding.android.fundamental.githubuser.repo.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getLoading() = userRepository.isLoading
    fun getFullname() = userRepository.userFullname
    fun getUsername() = userRepository.username
    fun getTotalFollowers() = userRepository.totalFollowers
    fun getTotalFollowing() = userRepository.totalFollowing
    fun getProfileURL() = userRepository.profileImage
    fun getUserFollowers() = userRepository.userFollowers
    fun getUserFollowing() = userRepository.userFollowing

    fun setUserDetail(username: String?) {
        userRepository.findDetailUser(username)
    }
    fun setUserFollowersByUsername(username: String) {
        userRepository.getFollowers(username)
    }
    fun setUserFollowingByUsername(username: String) {
        userRepository.getFollowing(username)
    }
    fun insertFavoriteUser(username: String, avatarUrl: String, profileUrl: String) {
        val favUser = FavoriteUserEntity(
            username,
            avatarUrl,
            profileUrl
        )
        viewModelScope.launch {
            userRepository.insertFavoriteUser(favUser)
        }
    }

    fun deleteFavoriteUser(username: String) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(username)
        }
    }

    fun getFavUserByUname(username: String) : LiveData<FavoriteUserEntity> {
        return userRepository.getFavoriteUserByUsername(username)
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}