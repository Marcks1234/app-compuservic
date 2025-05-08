package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.EstadoUsuario
import com.example.app_compuservic.repositorios.datoEnum.FireStoreColeccion.*
import com.example.app_compuservic.modelos.Usuario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireStoreRepositorio {
    private val store = Firebase.firestore


    suspend fun agregarDatosUsuario(uid: String, nuevoUsuario: Usuario): EstadoUsuario {
        return withContext(Dispatchers.IO) {
            try {
                store.collection(USUARIOS.valor).document(uid).set(nuevoUsuario).await()
                EstadoUsuario.Exito
            } catch (e: Exception) {
                EstadoUsuario.Error(e.message ?: "Error desconocido")
            }
        }
    }





}