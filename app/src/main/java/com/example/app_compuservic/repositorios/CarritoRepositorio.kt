package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.ProductoCarrito
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CarritoRepositorio {
    //referencia al servicio Firestore
    private val db = Firebase.firestore
    //obtiene el ID del usuario actualmente autenticado.
    private val uid get() = Firebase.auth.currentUser?.uid.orEmpty()

    //Guarda un producto en el carrito del usuario actual.
    fun guardarProductoEnCarrito(
        productoCarrito: ProductoCarrito,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = Firebase.auth.currentUser?.uid ?: return onError(Exception("Usuario no autenticado"))
        //los guarda en esta ruta ->usuarios/{uid}/carrito/{id del producto}
        Firebase.firestore
            .collection("usuarios")
            .document(uid)
            .collection("carrito")
            .document(productoCarrito.producto.id)
            .set(productoCarrito)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Obtiene todos los productos que el
    // usuario tiene actualmente en su carrito.
    fun obtenerCarritoDelUsuario(
        onSuccess: (List<ProductoCarrito>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        //Lee todos los documentos de la colección
        db.collection("usuarios")
            .document(uid)
            .collection("carrito")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { it.toObject(ProductoCarrito::class.java) }
                onSuccess(lista) //Devuelve la lista de productos
            }
            .addOnFailureListener { onError(it) }
    }
    //Eliminacion de productos por ID
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
