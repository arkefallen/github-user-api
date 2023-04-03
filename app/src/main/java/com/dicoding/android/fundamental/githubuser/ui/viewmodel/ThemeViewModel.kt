package com.dicoding.android.fundamental.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.android.fundamental.githubuser.data.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel() : ViewModel() {
    private var preferences : ThemePreferences? = null

    fun setPreferences(pref: ThemePreferences) {
        preferences = pref
    }

    fun getThemeSetting() : LiveData<Boolean>? {
        return preferences?.getThemeSetting()?.asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive : Boolean) {
        viewModelScope.launch {
            preferences?.saveThemeSetting(isDarkModeActive)
        }
    }
}