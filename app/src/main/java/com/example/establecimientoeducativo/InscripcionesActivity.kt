package com.example.establecimientoeducativo

import CustomSpinnerAdapter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InscripcionesActivity : AppCompatActivity() {

    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripciones)

        prefs = Prefs(this)
        val lvInscripciones = findViewById<ListView>(R.id.lvInscripciones)

        val currentUser = prefs.getCurrentUser() ?: return
        val allInscripciones = prefs.getInscripciones()
        val userInscripciones = allInscripciones.filter { it.startsWith("User: $currentUser") }
            .map { it.substringAfter("User: $currentUser, ") }.toTypedArray()

        val adapter = CustomSpinnerAdapter(this, userInscripciones)
        lvInscripciones.adapter = adapter
    }
}