package com.dicoding.android.fundamental.githubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.android.fundamental.githubuser.R
import com.dicoding.android.fundamental.githubuser.data.ThemePreferences
import com.dicoding.android.fundamental.githubuser.databinding.ActivityThemeBinding
import com.dicoding.android.fundamental.githubuser.databinding.ActivityUserDetailBinding
import com.dicoding.android.fundamental.githubuser.databinding.FragmentUserBinding
import com.dicoding.android.fundamental.githubuser.databinding.ItemUserBinding
import com.dicoding.android.fundamental.githubuser.ui.viewmodel.ThemeViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeActivity : AppCompatActivity() {
    private lateinit var themeBinding: ActivityThemeBinding
    private lateinit var itemBinding : ItemUserBinding
    private lateinit var detailBinding : ActivityUserDetailBinding
    private lateinit var fragmentUserBinding : FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        fragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)
        themeBinding = ActivityThemeBinding.inflate(layoutInflater)
        itemBinding = ItemUserBinding.inflate(layoutInflater)
        setContentView(themeBinding.root)

        val preferences = ThemePreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this).get(ThemeViewModel::class.java)
        themeViewModel.setPreferences(preferences)

        themeViewModel.getThemeSetting()?.observe(
            this, {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    themeBinding.switchBtn.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    themeBinding.switchBtn.isChecked = false
                }
            }
        )

        themeBinding.switchBtn.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }
}