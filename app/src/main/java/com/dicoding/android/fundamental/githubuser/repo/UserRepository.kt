package com.dicoding.android.fundamental.githubuser.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.android.fundamental.githubuser.data.entity.FavoriteUserDAO
import com.dicoding.android.fundamental.githubuser.data.entity.GithubUserDatabase
import com.dicoding.android.fundamental.githubuser.data.remote.APIConfig
import com.dicoding.android.fundamental.githubuser.data.remote.APIService
import com.dicoding.android.fundamental.githubuser.data.response.GithubResponse
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.data.response.UserDetailResponse
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.UserDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: APIService,
    private val database: GithubUserDatabase,
    private val favoriteUserDAO: FavoriteUserDAO
) {
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

    private val _usersLiveData = MutableLiveData<List<User>>()
    val users : LiveData<List<User>> = _usersLiveData

    fun setUsers(username: String?) {
        _isLoadingLiveData.value = true

        val client = if (username == null) {
            APIConfig.getService().getGithubUsers("jay")
        } else {
            APIConfig.getService().getGithubUsers(username)
        }

        client.enqueue(
            object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _usersLiveData.value = responseBody.items!!
                        }
                    } else {
                        Log.e(MainViewModel.TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.e(MainViewModel.TAG, "onFailure: ${t.message}")
                }
            }
        )
    }

    fun findDetailUser(username: String?) {

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
                            Log.e(UserDetailViewModel.TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Log.e(UserDetailViewModel.TAG, "onFailure: ${t.message}")
                }
            }
        )
    }

    fun getFollowers(username: String) {

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
                            _userFollowersLiveData.value = responseBody!!
                        }
                        else {
                            Log.e(UserDetailViewModel.TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e(UserDetailViewModel.TAG, "onFailure: ${t.message}")
                }
            }
        )

    }

    fun getFollowing(username: String) {

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
                            _userFollowingLiveData.value = responseBody!!
                        }
                        else {
                            Log.e(UserDetailViewModel.TAG, "onResponse: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e(UserDetailViewModel.TAG, "onFailure: ${t.message}")
                }
            }
        )
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: APIService,
            database: GithubUserDatabase,
            favoriteUserDAO: FavoriteUserDAO
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, database, favoriteUserDAO)
            }.also { instance = it }
    }

}