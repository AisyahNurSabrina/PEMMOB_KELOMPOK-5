package com.pemmob.foundu.data.local

import android.content.Context

object UserSession {
    var name: String = ""
    var email: String = ""
    var role: String = "mahasiswa" // "admin" or "mahasiswa"

    private const val PREFS_NAME = "foundu_users"

    fun loginAdmin(emailInput: String, passwordInput: String): Boolean {
        val normalizedEmail = emailInput.trim().lowercase()

        if (normalizedEmail == "admin@adm.unsoed.ac.id" && passwordInput == "administrator") {
            name = "Admin"
            email = "admin@adm.unsoed.ac.id"
            role = "admin"
            return true
        }

        return false
    }

    fun loginMahasiswa(context: Context, emailInput: String, passwordInput: String): Boolean {
        val normalizedEmail = emailInput.trim().lowercase()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val savedPassword = prefs.getString("password_$normalizedEmail", null)
        val savedName = prefs.getString("name_$normalizedEmail", null)

        if (savedPassword == null || savedName == null) return false
        if (savedPassword != passwordInput) return false

        name = savedName
        email = normalizedEmail
        role = "mahasiswa"
        return true
    }

    fun registerMahasiswa(
        context: Context,
        nameInput: String,
        emailInput: String,
        passwordInput: String
    ): Boolean {
        val normalizedEmail = emailInput.trim().lowercase()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (prefs.contains("password_$normalizedEmail")) return false

        prefs.edit()
            .putString("name_$normalizedEmail", nameInput.trim())
            .putString("password_$normalizedEmail", passwordInput)
            .apply()

        name = nameInput.trim()
        email = normalizedEmail
        role = "mahasiswa"
        return true
    }

    fun logout() {
        name = ""
        email = ""
        role = "mahasiswa"
    }
}
