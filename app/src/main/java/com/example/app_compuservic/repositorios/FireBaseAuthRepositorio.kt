package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.EstadoUsuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireBaseAuthRepositorio {
    private val auth = Firebase.auth

    suspend fun loginUsuario(email: String, password: String): EstadoUsuario {
        return withContext(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                EstadoUsuario.Exito
            } catch (e: Exception) {
                EstadoUsuario.Error(e.message.toString())
            }
        }
    }


    suspend fun registerUsuario(email: String, password: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                auth.createUserWithEmailAndPassword(email, password).await().user?.uid
            } catch (e: Exception) {
                null
            }
        }
    }
}