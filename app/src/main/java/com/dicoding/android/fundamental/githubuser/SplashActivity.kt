package com.dicoding.android.fundamental.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler



class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        Handler().postDelayed(
            Runnable() {
                run {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }, TIME_DELAY
        )
    }

    companion object {
        private val TIME_DELAY = 3000L
    }
}