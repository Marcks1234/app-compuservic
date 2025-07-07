package com.example.app_compuservic.repositorios

import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireBaseAuthRepositorio {
    private val auth = Firebase.auth

    suspend fun loginUsuario(email: String, password: String): EstadoUsuario = withContext(
        Dispatchers.IO
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            EstadoUsuario.Exito
        } catch (e: Exception) {
            EstadoUsuario.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun registerUsuario(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }//Crea una cuenta nueva en Firebase con email y contraseña.


    fun cerrarCuenta() {
        auth.signOut()
    }
    fun uidCuentaActual(): String{
        return auth.currentUser?.uid ?: ""
    } //Devuelve el UID del usuario actual.

    fun existeCuenta(): Boolean {
        return auth.currentUser != null
    } //Verifica si hay un usuario autenticado.
}