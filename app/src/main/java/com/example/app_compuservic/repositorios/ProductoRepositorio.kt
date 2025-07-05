package com.example.app_compuservic.repositorios

import com.example.app_compuservic.modelos.Producto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProductoRepositorio {
    fun guardarProductoFirestore(
        producto: Producto,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        Firebase.firestore.collection("productos")
            .add(producto)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}