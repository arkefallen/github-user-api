package com.dicoding.android.fundamental.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val _userFollowersLiveData = MutableLiveData<List<User>>()
    val userFollowers : LiveData<List<User>> = _userFollowersLiveData

    private val _userFollowingLiveData = MutableLiveData<List<User>>()
    val userFollowing : LiveData<List<User>> = _userFollowingLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoadingLiveData

    private val _userFullnameLiveData = MutableLiveData<String>()
    val userFullname : LiveData<String> = _userFullnameLiveData

    private val _profileImageLiveData = MutableLiveData<String>()
    val profileImage : LiveData<String> = _profileImageLiveData

    private val _totalFollowersLiveData = MutableLiveData<Int>()
    val totalFollowers : LiveData<Int> = _totalFollowersLiveData

    private val _totalFollowingLiveData = MutableLiveData<Int>()
    val totalFollowing : LiveData<Int> = _totalFollowingLiveData

    private val _usernameLiveData = MutableLiveData<String>()
    val username: LiveData<String> = _usernameLiveData

    init {
        getUser(null)
    }

    fun getUser(username: String?) {

        _isLoadingLiveData.value = true

        val client = if (username == null) {
            APIConfig.getService().getUserDetail("admin")
        } else {
            APIConfig.getService().getUserDetail(username)
        }

        client.enqueue(
            object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _userFullnameLiveData.value = responseBody.name
                            _usernameLiveData.value = responseBody.login
                            _profileImageLiveData.value = responseBody.avatarUrl
                            _totalFollowersLiveData.value = responseBody.followers
                            _totalFollowingLiveData.value = responseBody.following
                        }
                        else {
                            Log.e(TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )

    }

    fun getFollowers(username: String?) {

        _isLoadingLiveData.value = true

        val client = if (username == null) {
            APIConfig.getService().getFollowers("admin")
        } else {
            APIConfig.getService().getFollowers(username.toString())
        }

        client.enqueue(
            object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                           _userFollowersLiveData.value = responseBody
                        }
                        else {
                            Log.e(TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )

    }

    fun getFollowing(username: String?) {

        _isLoadingLiveData.value = true

        val client = if (username == null) {
            APIConfig.getService().getFollowing("admin")
        } else {
            APIConfig.getService().getFollowing(username.toString())
        }

        client.enqueue(
            object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _userFollowingLiveData.value = responseBody
                        }
                        else {
                            Log.e(TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )

    }

}