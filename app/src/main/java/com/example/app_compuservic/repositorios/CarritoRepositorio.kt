package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.ProductoCarrito
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CarritoRepositorio {
    private val db = Firebase.firestore
    private val uid get() = Firebase.auth.currentUser?.uid.orEmpty()

    fun guardarProductoEnCarrito(
        productoCarrito: ProductoCarrito,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = Firebase.auth.currentUser?.uid ?: return onError(Exception("Usuario no autenticado"))

        Firebase.firestore
            .collection("usuarios")
            .document(uid)
            .collection("carrito")
            .document(productoCarrito.producto.id)
            .set(productoCarrito)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun obtenerCarritoDelUsuario(
        onSuccess: (List<ProductoCarrito>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("usuarios")
            .document(uid)
            .collection("carrito")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { it.toObject(ProductoCarrito::class.java) }
                onSuccess(lista)
            }
            .addOnFailureListener { onError(it) }
    }
    fun eliminarProductoDeFirestore(
        productoId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = Firebase.auth.currentUser?.uid ?: return onError(Exception("Usuario no autenticado"))

        Firebase.firestore
            .collection("usuarios")
            .document(uid)
            .collection("carrito")
            .document(productoId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

}
