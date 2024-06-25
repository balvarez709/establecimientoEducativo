package com.example.establecimientoeducativo

import CustomSpinnerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InscripcionActivity : AppCompatActivity() {

    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion)

        prefs = Prefs(this)

        InitUI()
    }

    private fun InitUI(){
        val spMaterias =findViewById<Spinner>(R.id.spMaterias)
        val spLlamados = findViewById<Spinner>(R.id.spLlamados)
        val btEnviarInscripcion = findViewById<Button>(R.id.btEnviarInscripcion)


        val materias = arrayOf("Matemáticas", "Historia", "Ciencias","Biologia","Arte","Geografia","Musica")
        val llamados = arrayOf("Primer llamado", "Segundo llamado")

        val adapterMaterias = CustomSpinnerAdapter(this, materias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spMaterias.adapter = adapterMaterias

        val adapterLlamados = CustomSpinnerAdapter(this, llamados)
        adapterLlamados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLlamados.adapter = adapterLlamados

        btEnviarInscripcion.setOnClickListener {
            val materiaSeleccionada = spMaterias.selectedItem.toString()
            val llamadoSeleccionado = spLlamados.selectedItem.toString()
            saveInscripcion(materiaSeleccionada, llamadoSeleccionado) // Pass both arguments here
            enviarCorreo("","Inscripcion a ${materiaSeleccionada}", "Se inscribio correctamente a ${materiaSeleccionada}, ${llamadoSeleccionado}")
        }
    }

    private fun enviarCorreo(destinatario: String, asunto: String, mensaje: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
            putExtra(Intent.EXTRA_SUBJECT, asunto)
            putExtra(Intent.EXTRA_TEXT, mensaje)
        }
        try {
            startActivity(Intent.createChooser(intent, "Elige una aplicación de correo"))
        } catch (e: Exception) {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveInscripcion(materia: String, llamado: String) { // Function requires both arguments
        val inscripcion = "Materia: $materia, Llamado: $llamado"
        prefs.saveInscripcion(materia, llamado)
    }

    private fun sendInscripcionEmail(materia: String, llamado: String) {
        Toast.makeText(this, "Inscripción enviada: $materia - $llamado", Toast.LENGTH_SHORT).show()
    }
}