package com.example.simplenotesapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


val SPLASH_DURATION : Long = 3000 // in milliseconds
class SplashScreen : AppCompatActivity() {
    private lateinit var textEquinox: TextView
    private lateinit var textNotes: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, SPLASH_DURATION)

        textEquinox = findViewById(R.id.text_equinox)
        textNotes = findViewById(R.id.text_notes)

        val spanEquinox = SpannableString("Equinox")
        spanEquinox.setSpan(ForegroundColorSpan(Color.WHITE), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textEquinox.text = spanEquinox


        val spanNotes = SpannableString("notes")
        spanNotes.setSpan(ForegroundColorSpan(Color.WHITE), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textNotes.text = spanNotes
    }
}