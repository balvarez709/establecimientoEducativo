package com.example.establecimientoeducativo

import CustomSpinnerAdapter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class PerfilActivity : AppCompatActivity() {

    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        prefs = Prefs(this)

        initUI()
    }


    private fun initUI(){
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val spTipoDocumento = findViewById<Spinner>(R.id.spTipoDocumento)
        val etDocumento = findViewById<EditText>(R.id.etDocumento)
        val btGuardar = findViewById<Button>(R.id.btGuardar)

        val tiposDocumento = arrayOf("DNI", "Pasaporte")
        val adapterTipoDocumento = CustomSpinnerAdapter(this, tiposDocumento)
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoDocumento.adapter = adapterTipoDocumento

        btGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val fechaNacimiento = etFechaNacimiento.text.toString()
            val tipoDocumento = spTipoDocumento.selectedItem.toString()
            val documento = etDocumento.text.toString()
            val user = prefs.getCurrentUser()

            if (user != null) {
                val perfil = Profile(nombre, apellido, fechaNacimiento, tipoDocumento, documento, user)
                if (validateFields(nombre, apellido, fechaNacimiento, documento)) {
                    saveProfileData(perfil)
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No hay un usuario en sesión", Toast.LENGTH_SHORT).show()
            }
        }



        loadProfileData(etNombre, etApellido, etFechaNacimiento, spTipoDocumento, etDocumento)
    }

    private fun validateFields(nombre: String, apellido: String, fechaNacimiento: String, documento: String): Boolean {
        return nombre.isNotEmpty() && apellido.isNotEmpty() && fechaNacimiento.isNotEmpty() && documento.isNotEmpty()
    }

    private fun saveProfileData(perfil: Profile) {
        prefs.saveProfile(perfil)
    }

    private fun loadProfileData(etNombre: EditText, etApellido: EditText, etFechaNacimiento: EditText, spTipoDocumento: Spinner, etDocumento: EditText) {
        val profile = prefs.getProfile()
        if (profile != null) {
            etNombre.setText(profile.nombre)
            etApellido.setText(profile.apellido)
            etFechaNacimiento.setText(profile.fechaNacimiento)
            etDocumento.setText(profile.documento)
            val adapter = spTipoDocumento.adapter as ArrayAdapter<String>
            val position = adapter.getPosition(profile.tipoDocumento)
            spTipoDocumento.setSelection(position)
        }
    }
}