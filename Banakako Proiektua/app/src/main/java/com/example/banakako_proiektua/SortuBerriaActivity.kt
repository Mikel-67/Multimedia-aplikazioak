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
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.view.View
import android.widget.EditText

class SortuBerriaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var etIzena: EditText
    private lateinit var etDosia: EditText
    private lateinit var frekuentzia: Spinner
    private lateinit var frekuentziaEgunak: EditText
    private lateinit var ordutegia: Spinner
    private lateinit var hasieraData: TextView
    private lateinit var bukaeraData: TextView
    private lateinit var gordeBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actibitate_berriasortu)

        db = FirebaseFirestore.getInstance()

         val back: ImageButton = findViewById(R.id.btnBack)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val hasieraDataBtn: Button = findViewById(R.id.btnHasieraData)
        val bukaeraDataBtn: Button = findViewById(R.id.btnBukaeraData)
        etIzena = findViewById(R.id.etIzena)
        etDosia = findViewById(R.id.etDosia)
        frekuentzia = findViewById(R.id.spinnerFrekuentzia)
        frekuentziaEgunak = findViewById(R.id.etEgunak)
        ordutegia = findViewById(R.id.spinnerOrdua)
        hasieraData = findViewById(R.id.tvHasieraData)
        bukaeraData = findViewById(R.id.tvBukaeraData)
        gordeBtn = findViewById(R.id.btnMedikazioaGorde)


        hasieraDataBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    hasieraData.text = fecha
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        bukaeraDataBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    bukaeraData.text = fecha
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        val frekuentziaList = listOf("Egunero", "Astero", "X Egunean behin")
        val adapterFrecuencia = ArrayAdapter(this, android.R.layout.simple_spinner_item, frekuentziaList)

        val ordutegiaList = listOf("Goizean", "Eguerdian", "Gauean")
        val adapterOrdutegia = ArrayAdapter(this, android.R.layout.simple_spinner_item, ordutegiaList)

        adapterFrecuencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frekuentzia.adapter = adapterFrecuencia
        adapterOrdutegia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ordutegia.adapter = adapterOrdutegia

        frekuentzia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val opcion = parent.getItemAtPosition(position).toString()
                if (opcion == "X Egunean behin") {
                    frekuentziaEgunak.visibility = View.VISIBLE
                } else {
                    frekuentziaEgunak.visibility = View.GONE
                    frekuentziaEgunak.text.clear()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        gordeBtn.setOnClickListener {
            setUpMedikazioaClick();
        }
    }
    private fun setUpMedikazioaClick() {
        val izenaDatua = etIzena.text.toString().trim()
        val dosiaDatua = etDosia.text.toString().trim()
        var frecuentziaDatua = frekuentzia.selectedItem.toString()
        val ordutegiaDatua = ordutegia.selectedItem.toString()
        val hasieraDataDatua = hasieraData.text.toString()
        var bukaeraDataDatua = bukaeraData.text.toString()
        val azkenEguna = null

        // Validaciones simples
        if (izenaDatua.isEmpty() || dosiaDatua.isEmpty() || hasieraDataDatua == "No seleccionada") {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (bukaeraDataDatua == "No seleccionada"){
            bukaeraDataDatua = ""
        }
        if (frecuentziaDatua == "X Egunean behin") {
            val dias = frekuentziaEgunak.text.toString().toIntOrNull()
            if (dias != null && dias > 0) {
                frecuentziaDatua = "$dias egunean behin"
            } else {
                Toast.makeText(this, "Ingresa un número válido de días", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Crear mapa para Firebase
        val medicacion = hashMapOf(
            "izena" to izenaDatua,
            "dosia" to dosiaDatua,
            "frekuentzia" to frecuentziaDatua,
            "ordua" to ordutegiaDatua,
            "hasiera" to hasieraDataDatua,
            "bukaera" to bukaeraDataDatua,
            "azkenEgunaHartuta" to azkenEguna
        )
        db.collection("medicaciones")
            .add(medicacion)
            .addOnSuccessListener {
                Toast.makeText(this, "Medicación guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}