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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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
        val escucha = db.collection("productos")
            .whereEqualTo("categoriaId", categoriaId)  // Filtramos por categoriaId en lugar de categoriaNombre
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Estados.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val lista = snapshot.documents.mapNotNull { document ->
                        val producto = document.toObject(Producto::class.java)
                        producto?.copy(id = document.id)  // Añadimos el ID al producto
                    }
                    trySend(Estados.Exito(lista))  // Si encontramos productos, los enviamos en el estado Exito
                } else {
                    trySend(Estados.Vacio)  // Si no encontramos productos, enviamos el estado vacío
                }
            }
        awaitClose { escucha.remove() }  // Cerramos la escucha cuando el flujo se cierre
    }

    //otenemos productos por id
    suspend fun obtenerProductoPorId(id: String): Producto? {
        return try {
            val doc = db.collection("productos").document(id).get().await()
            doc.toObject(Producto::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }


    //agregamos esto:
    suspend fun agregarProducto(producto: Producto) {
        db.collection("productos").add(producto).await()
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


    fun escucharFavoritos(uid: String): Flow<List<Producto>> = callbackFlow {
        val coleccion = db.collection("usuarios").document(uid).collection("favoritos")

        val listener = coleccion.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val ids = snapshot?.documents?.map { it.id } ?: emptyList()

            if (ids.isEmpty()) {
                trySend(emptyList())
                return@addSnapshotListener
            }

            val tareas = ids.map { id ->
                db.collection("productos").document(id).get()
            }

            val productos = mutableListOf<Producto>()
            GlobalScope.launch {
                val resultados = tareas.map { it.await() }
                resultados.forEach { doc ->
                    doc.toObject(Producto::class.java)?.let {
                        productos.add(it.copy(id = doc.id))
                    }
                }
                trySend(productos)
            }
        }
        awaitClose { listener.remove() }
    }

}