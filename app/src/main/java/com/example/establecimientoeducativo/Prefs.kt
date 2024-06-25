package com.example.establecimientoeducativo

import android.content.Context
import android.content.SharedPreferences

data class Profile(val nombre: String, val apellido: String, val fechaNacimiento: String, val tipoDocumento: String, val documento: String, val user: String)
data class User(val user: String, val password: String)

class Prefs(context: Context) {

    val preferences: SharedPreferences = context.getSharedPreferences("establecimiento_educativo_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val users = getUsers().toMutableList()
        users.add(user)
        val userKeys = users.map { it.user }.toSet()
        preferences.edit().putStringSet("users", userKeys).apply()
        preferences.edit().putString("${user.user}.pass", user.password).apply()
    }

    private fun getUsers(): List<User> {
        val userKeys = preferences.getStringSet("users", emptySet()) ?: emptySet()
        val users = mutableListOf<User>()
        for (userKey in userKeys) {
            val password = preferences.getString("$userKey.pass", null)
            if (password != null) {
                users.add(User(userKey, password))
            }
        }
        return users
    }

    fun saveProfile(profile: Profile) {
        val userKey = profile.user
        preferences.edit().putString("$userKey.nombre", profile.nombre).apply()
        preferences.edit().putString("$userKey.apellido", profile.apellido).apply()
        preferences.edit().putString("$userKey.fechaNacimiento", profile.fechaNacimiento).apply()
        preferences.edit().putString("$userKey.tipoDocumento", profile.tipoDocumento).apply()
        preferences.edit().putString("$userKey.documento", profile.documento).apply()
    }

    fun getProfile(): Profile? {
        val currentUser = getCurrentUser() ?: return null // Obtiene el usuario de la sesión actual
        val userKey = currentUser
        val nombre = preferences.getString("$userKey.nombre", null) ?: return null
        val apellido = preferences.getString("$userKey.apellido", null) ?: return null
        val fechaNacimiento = preferences.getString("$userKey.fechaNacimiento", null) ?: return null
        val tipoDocumento = preferences.getString("$userKey.tipoDocumento", null) ?: return null
        val documento = preferences.getString("$userKey.documento", null) ?: return null
        return Profile(nombre, apellido, fechaNacimiento, tipoDocumento, documento, userKey)
    }

    fun saveProfileImageUri(user: String, imageUriString: String) {
        preferences.edit().putString("${user}.imageUri", imageUriString).apply()
    }

    fun getProfileImageUri(user: String): String? {
        return preferences.getString("${user}.imageUri", null)
    }

    fun getCurrentUser(): String? {
        return preferences.getString("current_user", null) // Obtiene el usuario de la sesión actual
    }

    fun getUser(user: String): User? {
        val password = preferences.getString("$user.pass", null) ?: return null
        return User(user, password)
    }

    fun getPass(): String? {
        return preferences.getString("pass", null)
    }

    fun clearUser() {
        preferences.edit().putString("current_user", null).apply() // Limpia la sesión actual
    }

    fun saveInscripcion(materia: String, llamado: String) {
        val currentUser = getCurrentUser() ?: return
        val inscripcion = "User: $currentUser, Materia: $materia, Llamado: $llamado"
        val inscripciones = getInscripciones().toMutableList()
        inscripciones.add(inscripcion)
        preferences.edit().putStringSet("inscripciones", inscripciones.toSet()).apply()
    }

    fun getInscripciones(): Set<String> {
        return preferences.getStringSet("inscripciones", emptySet()) ?: emptySet()
    }
}