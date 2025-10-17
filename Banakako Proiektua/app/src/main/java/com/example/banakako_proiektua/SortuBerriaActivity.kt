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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.cardview.widget.CardView

class SortuBerriaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibitate_berriasortu)

        db = FirebaseFirestore.getInstance()

         val back: ImageButton = findViewById(R.id.btnBack)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val cardAgua: CardView = findViewById(R.id.habitoUra)
        val cardEjercicio: CardView = findViewById(R.id.habitoGym)
        val cardLeer: CardView = findViewById(R.id.habitoLiburua)

        // Asignamos listeners
        setUpHabitoClick(cardAgua, "Beber más agua")
        setUpHabitoClick(cardEjercicio, "Hacer ejercicio")
        setUpHabitoClick(cardLeer, "Leer un libro")
    }
    private fun setUpHabitoClick(view: CardView, habito: String) {
        view.setOnClickListener {
            val calendario = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                    guardarEnFirebase(habito, fechaSeleccionada)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun guardarEnFirebase(habito: String, fecha: String) {
        val datos = hashMapOf(
            "habito" to habito,
            "hasta" to fecha,
            "creado" to SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        )

        db.collection("habitos")
            .add(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "Hábito '$habito' añadido hasta $fecha", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}