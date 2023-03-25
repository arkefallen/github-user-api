package com.dicoding.android.fundamental.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _usersLiveData = MutableLiveData<List<User>>()
    val users : LiveData<List<User>> = _usersLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoadingLiveData

    private val _usernameLiveData = MutableLiveData<String>()
    val username : LiveData<String> = _usernameLiveData

    companion object {
        private val TAG = this::class.java.simpleName
        private const val DEFAULT_QUERY = "jay"
    }

    init {
        getUsers(null)
    }

    fun getUsers(username: String?) {
        _isLoadingLiveData.value = true

        val client = if (username == null) {
            APIConfig.getService().getGithubUsers(DEFAULT_QUERY)
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
                            _usersLiveData.value = responseBody.items
                        }
                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )
    }
}