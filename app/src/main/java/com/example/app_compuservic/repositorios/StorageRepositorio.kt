package com.example.app_compuservic.repositorios

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object StorageRepositorio {

    private val storageRef = Firebase.storage.reference

    fun subirImagen(uri: Uri, nombreArchivo: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val imagenRef = storageRef.child("imagenes_productos/$nombreArchivo")

        imagenRef.putFile(uri)
            .addOnSuccessListener {
                imagenRef.downloadUrl.addOnSuccessListener { uriDescarga ->
                    onSuccess(uriDescarga.toString())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
