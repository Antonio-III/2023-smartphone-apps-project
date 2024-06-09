package com.example.simplenotesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var themeViewModel: ThemeViewModel

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeViewModel = ViewModelProvider(this).get(ThemeViewModel::class.java)
        themeViewModel.setTheme(isDarkModeEnabled())
        themeViewModel.isDarkMode.observe(this){isDarkMode ->
            applyTheme(isDarkMode)
        }

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        navController.addOnDestinationChangedListener{
            _, destination, _, ->
            when (destination.id){
                R.id.noteFragment -> supportActionBar?.title = "Notes"
                R.id.addEditNoteFragment -> supportActionBar?.title = "Write note"
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    private fun applyTheme(isDarkMode: Boolean){
        AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO)

        saveThemePreferences(isDarkMode)
    }

    private fun saveThemePreferences(isDarkMode: Boolean){
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit{
            putBoolean(KEY_IS_DARK_MODE, isDarkMode)
        }
    }
    private fun isDarkModeEnabled(): Boolean {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false)
    }
}