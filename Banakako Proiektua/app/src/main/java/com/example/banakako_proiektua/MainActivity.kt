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

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var btnActivity: Button
    private lateinit var flowerImg: ImageView

    private var currentProgress = 0
    private var maxProgress = 5
    private var currentImg = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)
        btnActivity = findViewById(R.id.btnActivities)
        flowerImg = findViewById(R.id.flowerImg)

        progressBar.max = maxProgress
        progressBar.progress = currentProgress
        updateProgressText()

        btnActivity.setOnClickListener { simulateActivityDone() }
    }

    private fun simulateActivityDone() {
        currentProgress += 1
        if (currentProgress >= maxProgress) {
            currentProgress = 0;
            maxProgress *= 2;
            currentImg ++;
            when{
                currentImg == 2 -> flowerImg.setImageResource(R.drawable.flower_2)
                currentImg == 3 -> flowerImg.setImageResource(R.drawable.flower_3)
                currentImg == 4 -> flowerImg.setImageResource(R.drawable.flower_4)
                currentImg == 5 ->flowerImg.setImageResource(R.drawable.flower_5)
            }
        }
        progressBar.max = maxProgress
        progressBar.progress = currentProgress
        updateProgressText()
    }

    private fun updateProgressText() {
        progressText.text = "Progreso: $currentProgress / $maxProgress"
    }
}