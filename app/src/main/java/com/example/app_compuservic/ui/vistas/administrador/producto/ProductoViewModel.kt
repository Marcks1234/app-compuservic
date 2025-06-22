package com.example.app_compuservic.ui.vistas.administrador.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore



class ProductoViewModel : ViewModel() {

    private val repositorio = FireStoreRepositorio()

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                val referencia = Firebase.firestore
                    .collection("productos")
                    .document()
                val nuevo = producto.copy(id = referencia.id)
                referencia.set(nuevo)
            } catch (e: Exception) {
                println("Error al agregar producto: ${e.message}")
            }
        }
    }
}
