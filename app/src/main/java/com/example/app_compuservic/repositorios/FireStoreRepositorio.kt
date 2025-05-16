package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.Categoria
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.repositorios.datoFireBase.FireStoreColeccion.*
import com.example.app_compuservic.modelos.Usuario
import com.example.app_compuservic.repositorios.datoFireBase.TipoUsuario
import com.example.app_compuservic.ui.estados.Estados
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireStoreRepositorio {
    private val db = Firebase.firestore


    suspend fun agregarDatosUsuario(uid: String, nuevoUsuario: Usuario): EstadoUsuario {
        return try {
            db.collection(C_USUARIOS.valor).document(uid).set(nuevoUsuario).await()
            EstadoUsuario.Exito
        } catch (e: Exception) {
            EstadoUsuario.Error(e.message ?: "Error desconocido")
        }
    }


    fun obtenerCategorias(): Flow<Estados<List<Categoria>>> = callbackFlow {
        val escucha = db.collection(C_CATEGORIAS.valor).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Estados.Error(error.message ?: "error desconocido"))
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val lista = snapshot.mapNotNull { it.toObject(Categoria::class.java) }
                trySend(Estados.Exito(lista))
            }
        }
        awaitClose { escucha.remove() }
    }


    fun obtenerProductos(categoriaId: String): Flow<Estados<List<Producto>>> = callbackFlow {
        val escucha = db.collection("productos").whereEqualTo("categoriaId", categoriaId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Estados.Error(error.message ?: "error desconocido"))
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val lista = snapshot.mapNotNull {
                        val producto = it.toObject(Producto::class.java)
                        producto.copy(id = it.id)
                    }
                    trySend(Estados.Exito(lista))
                } else {
                    trySend(Estados.Vacio)
                }
            }
        awaitClose { escucha.remove() }
    }


    suspend fun obtenerTipoUsuario(uid: String): TipoUsuario {
        val esAdministrador =
            db.collection("usuarios").document(uid).get().await().getBoolean("administrador")
        return when (esAdministrador) {
            true -> TipoUsuario.administrador
            false -> TipoUsuario.usuario
            null -> TipoUsuario.nuevo_usuario
        }
    }


    suspend fun obtenerDatosUsuarioActual(uidActual: String): String = withContext(Dispatchers.IO) {
        val consulta = db.collection("usuarios").document(uidActual).get().await()
        consulta.toObject(Usuario::class.java)?.nombre ?: "usuario sin nombre"
    }


    suspend fun obtenerIdsFavoritos(uid: String): Set<String> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("usuarios")
            .document(uid).collection("favoritos").get().await()
        snapshot.documents.map { it.id }.toSet()
    }

    suspend fun cambiarFavorito(uid: String, producto: Producto, esFavorito: Boolean) =
        withContext(Dispatchers.IO) {
            val documentoReferenciaFavorito =
                db.collection("usuarios").document(uid).collection("favoritos")
                    .document(producto.id)
            if (esFavorito) {
                documentoReferenciaFavorito.delete()
            } else {
                documentoReferenciaFavorito.set(mapOf("tiempo de favorito" to System.currentTimeMillis()))
                    .await()
            }
        }
}