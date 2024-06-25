package com.example.establecimientoeducativo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi

class PrincipalActivity : AppCompatActivity() {

    lateinit var prefs: Prefs
    private lateinit var onBackInvokedCallback: OnBackInvokedCallback

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principalactivity)

        // Manejo del evento "volver"
        onBackInvokedCallback = OnBackInvokedCallback {
            Toast.makeText(this, "Para cerrar sesión, use la opción del menú", Toast.LENGTH_SHORT).show()
        }
        onBackInvokedDispatcher.registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            onBackInvokedCallback
        )

        prefs = Prefs(this)
        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cerrar_sesion -> {
                cerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }}

    private fun cerrarSesion() {
        prefs.clearUser() // Cierra la sesión actual
        goToMainActivity() // Vuelve a la pantalla de inicio de sesión
    }

    private fun initUI() {
        val btInscripcionExamenes = findViewById<Button>(R.id.btInscripcionExamenes)
        btInscripcionExamenes.setOnClickListener { goToInscripcion() }

        val btMisInscripciones =findViewById<Button>(R.id.btMisInscripciones)
        btMisInscripciones.setOnClickListener { goToMisInscripciones() }

        val btPerfilAlumno = findViewById<Button>(R.id.btPerfilAlumno)
        btPerfilAlumno.setOnClickListener { goToPerfil() }

        val btAPIs = findViewById<Button>(R.id.btAPIs)
        btAPIs.setOnClickListener { goToAPIs() }

        val tvBienvenido = findViewById<TextView>(R.id.tvWelcome)
        val user = prefs.getCurrentUser() // Obtiene el usuario de la sesión actual
        tvBienvenido.text = "Bienvenido ${user}!"
    }

    private fun goToInscripcion() {
        startActivity(Intent(this, InscripcionActivity::class.java))
    }

    private fun goToMisInscripciones() {
        startActivity(Intent(this, InscripcionesActivity::class.java))
    }

    private fun goToPerfil() {
        startActivity(Intent(this, PerfilActivity::class.java))
    }

    private fun goToAPIs() {
        startActivity(Intent(this, APIs::class.java))
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}