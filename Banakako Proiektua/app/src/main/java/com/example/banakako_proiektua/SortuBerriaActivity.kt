package com.example.banakako_proiektua

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ProgressBar
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.widget.ImageButton
import kotlin.jvm.java

class SortuBerriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibitate_berriasortu)
         val back: ImageButton = findViewById(R.id.btnBack)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}