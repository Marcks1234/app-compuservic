package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.modelos.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireBaseAuthStoreRepositorio(
    private val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val db: FireStoreRepositorio = FireStoreRepositorio()
) {
    private var idActual = auth.uidCuentaActual()


    fun cerrarCuenta() {
        auth.cerrarCuenta()
    }

    // AGREGAR USUARIO EN FIRESTORE
    suspend fun crearUsuario(
        email: String,
        password: String,
        nuevoUsuario: Usuario
    ): EstadoUsuario = withContext(Dispatchers.IO) {
        try {
            val result = auth.registerUsuario(email, password)
            val uid = result.user?.uid
            if (uid != null) {
                db.agregarDatosUsuario(uid, nuevoUsuario)
            } else {
                EstadoUsuario.Error("UID no disponible")
            }
        } catch (e: Exception) {
            EstadoUsuario.Error(e.message ?: "error desconocido")
        }
    }



}
