package com.example.kotlinadb

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botoia: Button = findViewById(R.id.button_preview);
        botoia.setOnClickListener { onPreviewClicked() }
    }
    private fun onPreviewClicked (){

    }
}