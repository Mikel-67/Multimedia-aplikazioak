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
import kotlin.jvm.java
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var layoutHabitosDelDia: LinearLayout
    private lateinit var tvFecha: TextView
    private lateinit var db: FirebaseFirestore
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias
        tvFecha = findViewById(R.id.tvFecha)
        calendarView = findViewById(R.id.calendarView)
        layoutHabitosDelDia = findViewById(R.id.layoutAbajo)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()

        // Fecha de hoy
        val hoy = formatoFecha.format(Date())
        tvFecha.text = hoy

        // Cargar hábitos del día actual al iniciar
        cargarHabitosDelDia(hoy)

        // Escuchar cambios en el calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            tvFecha.text = fechaSeleccionada
            cargarHabitosDelDia(fechaSeleccionada)
        }

        // Botón + para abrir SortuBerriaActivity
        fabAdd.setOnClickListener {
            val intent = Intent(this, SortuBerriaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarHabitosDelDia(fecha: String) {
        layoutHabitosDelDia.removeAllViews()

        db.collection("habitos")
            .whereEqualTo("hasta", fecha)
            .get()
            .addOnSuccessListener { documentos ->
                if (documentos.isEmpty) {
                    val texto = TextView(this)
                    texto.text = "No hay hábitos para $fecha"
                    texto.setTextColor(getColor(android.R.color.darker_gray))
                    texto.textSize = 14f
                    texto.setPadding(8, 8, 8, 8)
                    layoutHabitosDelDia.addView(texto)
                } else {
                    for (doc in documentos) {
                        val habito = doc.getString("habito") ?: "Sin nombre"
                        val texto = TextView(this)
                        texto.text = "• $habito"
                        texto.setTextColor(getColor(android.R.color.white))
                        texto.textSize = 16f
                        texto.setPadding(8, 8, 8, 8)
                        layoutHabitosDelDia.addView(texto)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar hábitos", Toast.LENGTH_SHORT).show()
            }
    }
}