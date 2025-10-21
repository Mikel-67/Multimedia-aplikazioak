package com.example.banakako_proiektua

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import android.widget.CheckBox
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var layoutMedicaciones: LinearLayout
    private lateinit var tvFecha: TextView
    private lateinit var db: FirebaseFirestore
    private var komprimatuta : Boolean = false
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFecha = findViewById(R.id.tvFecha)
        calendarView = findViewById(R.id.calendarView)
        layoutMedicaciones = findViewById(R.id.layoutAbajo)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        db = FirebaseFirestore.getInstance()

        val hoy = formatoFecha.format(Date())
        tvFecha.text = hoy

        cargarMedicacionesDelDia(hoy)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            tvFecha.text = fechaSeleccionada
            cargarMedicacionesDelDia(fechaSeleccionada)
        }

        fabAdd.setOnClickListener {
            val intent = Intent(this, SortuBerriaActivity::class.java)
            startActivity(intent)
        }

        val calendarBtn : FloatingActionButton = findViewById(R.id.btnToggleCalendar)
        calendarBtn.setOnClickListener {
            if (komprimatuta){
                calendarView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                komprimatuta = false
            }else{
                calendarView.layoutParams.height = 0
                komprimatuta = true
            }
            calendarView.requestLayout()
        }
    }

    private fun cargarMedicacionesDelDia(fecha: String) {
        val layoutMedicaciones: LinearLayout = findViewById(R.id.layoutAbajo)
        layoutMedicaciones.removeAllViews() // limpiar antes de cargar

        val db = FirebaseFirestore.getInstance()

        db.collection("medicaciones")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val nombre = doc.getString("izena") ?: continue
                    val dosis = doc.getString("dosia") ?: ""
                    val frecuencia = doc.getString("frekuentzia") ?: ""
                    val hora = doc.getString("ordua") ?: ""
                    val inicio = doc.getString("hasiera") ?: ""
                    val fin = doc.getString("bukaera") ?: ""
                    val ultimaFecha = doc.getString("azkenEgunaHartuta") ?: ""

                    if (correspondeAlDia(fecha, inicio, fin, frecuencia)) {
                        mostrarMedicacionEnCard(doc.id, nombre, dosis, hora, ultimaFecha, fecha)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar medicaciones: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
    private fun correspondeAlDia(
        fecha: String, inicio: String, fin: String, frecuencia: String
    ): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaActual = formato.parse(fecha)
        val fechaInicio = formato.parse(inicio)
        val fechaFin: Date? = if (fin != "Ez aukeratuta") {
            formato.parse(fin)
        } else {
            formato.parse("31/12/4000")
        }

        if (fechaActual == null || fechaInicio == null || fechaFin == null) return false

        if (fechaActual.before(fechaInicio) || fechaActual.after(fechaFin)) return false

        return when {
            frecuencia == "Egunero" -> true
            frecuencia == "Astero" -> {
                val diff = ((fechaActual.time - fechaInicio.time) / (1000 * 60 * 60 * 24)) % 7
                diff == 0L
            }
            frecuencia.endsWith("Egunean behin") -> {
                val numero = frecuencia.split(" ")[0]
                val dias = numero.toIntOrNull()
                if (dias == null || dias <= 0) return false

                val diff = (fechaActual.time - fechaInicio.time) / (1000 * 60 * 60 * 24)
                diff % dias == 0L
            }
            else -> false
        }
    }
    private fun mostrarMedicacionEnCard(
        docId: String,
        nombre: String,
        dosis: String,
        hora: String,
        ultimaFecha: String,
        fechaHoy: String
    ) {
        val layoutMedicaciones: LinearLayout = findViewById(R.id.layoutAbajo)

        val cardView = androidx.cardview.widget.CardView(this).apply {
            val margin = 16
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(margin, margin, margin, margin)
            layoutParams = params
            radius = 20f
            cardElevation = 6f
            setCardBackgroundColor(resources.getColor(android.R.color.white))
            preventCornerOverlap = true
            useCompatPadding = true
        }

        val ll = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(24, 24, 24, 24)
            gravity = Gravity.CENTER_VERTICAL
        }

        val tv = TextView(this).apply {
            text = "$nombre\n$dosis - $hora"
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val checkBox = CheckBox(this).apply {
            if (fechaHoy != SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) {
                isEnabled = false
            }
            isChecked = ultimaFecha >= fechaHoy
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val db = FirebaseFirestore.getInstance()
                val data = hashMapOf("azkenEgunaHartuta" to fechaHoy)
                db.collection("medicaciones").document(docId)
                    .update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "$nombre marcado como tomado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@MainActivity, "Error al marcar $nombre", Toast.LENGTH_SHORT).show()
                        checkBox.isChecked = false
                    }
            }
        }

        ll.addView(tv)
        ll.addView(checkBox)
        cardView.addView(ll)
        layoutMedicaciones.addView(cardView)
    }

}