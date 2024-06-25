package com.example.establecimientoeducativo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.establecimientoeducativo.MainActivity
import kotlinx.coroutines.MainCoroutineDispatcher

class RecuperarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var prefs: Prefs

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)
        val editTextTextEmailAddress = findViewById<EditText>(R.id.etEmail)
        val btEnviar = findViewById<Button>(R.id.btSendEmail)
        val btVolver = findViewById<Button>(R.id.btBackToLogin)

        prefs = Prefs(this)
        btEnviar.setOnClickListener {
            val destinatario = editTextTextEmailAddress.text.toString()
            val mensaje = "Su contraseña es ${prefs.getPass()}"
            val asunto = "Recuperar Contraseña"
            enviarCorreo(destinatario,asunto, mensaje)
        }

        btVolver.setOnClickListener{
            backToLogin();
        }

    }

    //Envio de correo con INTENT (utilizamos para enviar una aplicación de correo que se
    //encuentre instalada en el teléfono).
    private fun enviarCorreo(destinatario: String, asunto: String, mensaje: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Solo aplicaciones de correo deben manejar esto
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

    private fun backToLogin(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}
