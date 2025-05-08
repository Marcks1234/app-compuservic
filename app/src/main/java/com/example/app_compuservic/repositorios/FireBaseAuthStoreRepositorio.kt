package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.EstadoUsuario
import com.example.app_compuservic.modelos.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireBaseAuthStoreRepositorio(
    private val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val store: FireStoreRepositorio = FireStoreRepositorio()
) {

    // AGREGAR USUARIO EN FIRESTORE
    suspend fun crearUsuario(
        email: String,
        password: String,
        nuevoUsuario: Usuario
    ): EstadoUsuario {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.registerUsuario(email, password)
                val uid = result.user?.uid
                if (uid != null) {
                    store.agregarDatosUsuario(uid, nuevoUsuario)
                } else {
                    EstadoUsuario.Error("UID no disponible")
                }
            } catch (e: Exception) {
                EstadoUsuario.Error(e.message ?: "error desconocido")
            }
        }
    }

}