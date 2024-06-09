package com.example.simplenotesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel: ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    fun toggleTheme(){
        _isDarkMode.value = _isDarkMode.value!=true
    }

    fun setTheme(isDark: Boolean){
        _isDarkMode.value = isDark
    }
}