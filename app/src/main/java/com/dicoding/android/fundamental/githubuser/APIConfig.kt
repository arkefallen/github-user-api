package com.dicoding.android.fundamental.githubuser


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.dicoding.android.fundamental.githubuser.BuildConfig

class APIConfig {
    companion object {
        fun getService() : APIService {
            val authInterceptor = Interceptor {
                val chainRequest = it.request()
                val token = BuildConfig.KEY
                val requestHeaders = chainRequest.newBuilder()
                    .addHeader("Authorization","token ${token}")
                    .build()
                it.proceed(requestHeaders)
            }

            val loggingInterceptor = if ( BuildConfig.DEBUG ) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_GITHUB_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}