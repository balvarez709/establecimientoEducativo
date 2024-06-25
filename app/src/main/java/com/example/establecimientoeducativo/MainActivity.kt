package com.example.establecimientoeducativo

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        prefs = Prefs(this)
        val usuario1 = User("balvarez", "123")
        val usuario2 = User("jjasa", "1")
        val usuario3 = User("nbelen", "1")
        prefs.saveUser(usuario1)
        prefs.saveUser(usuario2)
        prefs.saveUser(usuario3)
        initUI()
    }

    private fun initUI() {
        val btLogin = findViewById<Button>(R.id.btLogin)
        btLogin.setOnClickListener { accessToDetail() }

        val tvForgotPassoword = findViewById<TextView>(R.id.tvForgotPassword)
        tvForgotPassoword.setOnClickListener { goToRecoverPassword() }
    }

    private fun accessToDetail() {
        val etUser = findViewById<TextView>(R.id.etUser).text.toString()
        val etPassword = findViewById<TextView>(R.id.etPassword).text.toString()

        if (etUser.isNotEmpty() && etPassword.isNotEmpty()) {
            val user = prefs.getUser(etUser)
            if (user != null && etUser == user.user && etPassword == user.password) {
                prefs.preferences.edit().putString("current_user", etUser).apply()
                goToPrincipalActivity()
            } else {
                Toast.makeText(this, "Error, usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Debe ingresar sus datos para continuar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToPrincipalActivity() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    private fun goToRecoverPassword() {
        startActivity(Intent(this, RecuperarActivity::class.java))
    }
}
