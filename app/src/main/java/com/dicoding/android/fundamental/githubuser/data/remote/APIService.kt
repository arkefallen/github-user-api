package com.dicoding.android.fundamental.githubuser.data.remote

import com.dicoding.android.fundamental.githubuser.data.response.GithubResponse
import com.dicoding.android.fundamental.githubuser.data.response.User
import com.dicoding.android.fundamental.githubuser.data.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("search/users")
    fun getGithubUsers(
        @Query("q") q : String
    ) : Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username : String
    ) : Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<List<User>>
}