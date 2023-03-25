package com.dicoding.android.fundamental.githubuser

import androidx.viewbinding.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    companion object {
        fun getService() : APIService {
            val authInterceptor = Interceptor {
                val chainRequest = it.request()
                val requestHeaders = chainRequest.newBuilder()
                    .addHeader("Authorization","token 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")
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
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}