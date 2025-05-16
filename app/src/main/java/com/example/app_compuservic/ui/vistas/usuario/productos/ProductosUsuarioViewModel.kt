package com.example.app_compuservic.ui.vistas.usuario.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.Estados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductosUsuarioViewModel(
    private val db: FireStoreRepositorio = FireStoreRepositorio(),
    private val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio()
) :
    ViewModel() {
    private var uid: String = auth.uidCuentaActual()

    var estadoProductos = MutableStateFlow<Estados<List<Producto>>>(Estados.Vacio)
        private set
    var listaProducto = MutableStateFlow<List<Producto>>(emptyList())
        private set
    var listaFavorito = MutableStateFlow<Set<String>>(emptySet())
        private set

    fun obtenerProductos(categoriaId: String) {
        viewModelScope.launch {
            estadoProductos.value = Estados.Cargando
            db.obtenerProductos(categoriaId).collect {
                estadoProductos.value = it
                if (it is Estados.Exito) {
                    listaProducto.value = it.datos
                    obtenerFavoritos()
                }
            }
        }
    }

    fun cambiarFavorito(producto: Producto) {
        viewModelScope.launch {
            val esFavorito = listaFavorito.value.contains(producto.id)
            db.cambiarFavorito(uid, producto, esFavorito)
            obtenerFavoritos()
        }
    }

    fun obtenerFavoritos() {
        viewModelScope.launch {
            listaFavorito.value = db.obtenerIdsFavoritos(uid)
        }
    }
}