package com.example.app_compuservic.ui.vistas.administrador.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.Estados
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.flow.MutableStateFlow


class ProductoViewModel : ViewModel() {
    // Lista de productos
    val productos = MutableStateFlow<List<Producto>>(emptyList())

    // Obtener productos por categoriaId
    fun obtenerProductos(categoriaId: String) {
        viewModelScope.launch {
            repositorio.obtenerProductos(categoriaId).collect { estado ->
                when (estado) {
                    is Estados.Exito -> {
                        // Si la consulta fue exitosa, actualizamos la lista de productos
                        productos.value = estado.datos
                    }
                    else -> {
                        // Aquí puedes manejar otros casos, como error o vacío
                        productos.value = emptyList()
                    }
                }
            }
        }
    }

    private val repositorio = FireStoreRepositorio()
    // Lista de productos
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
