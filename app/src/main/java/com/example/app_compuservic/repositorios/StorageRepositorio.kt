package com.example.app_compuservic.repositorios

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object StorageRepositorio {

    //Esto obtiene la raíz del almacenamiento en Firebase
    // (gs://...), donde se guardan los archivos.
    private val storageRef = Firebase.storage.reference

    fun subirImagen(
        uri: Uri, //La URI de la imagen a subir.
        nombreArchivo: String,//El nombre con el que se guardará en Firebase
        onSuccess: (String) -> Unit, //Una función en caso de éxito.
        onError: (Exception) -> Unit) //Una función  en caso de error.
    {

        val imagenRef = storageRef.child("imagenes_productos/$nombreArchivo")
        //Este comando sube el archivo a Firebase usando su Uri.
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
