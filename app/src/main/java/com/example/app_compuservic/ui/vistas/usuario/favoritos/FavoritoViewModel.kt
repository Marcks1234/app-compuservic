package com.example.app_compuservic.ui.vistas.usuario.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoritoViewModel(
    private val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val db: FireStoreRepositorio = FireStoreRepositorio()
) :  ViewModel() {

    var listaFavorito = MutableStateFlow<List<Producto>>(emptyList())
        private set

    fun obtenerFavoritosTiempoReal() {
        viewModelScope.launch {
            db.escucharFavoritos(auth.uidCuentaActual()).collect {
            listaFavorito.value = it
            }
        }
    }
    fun cambiarFavorito(producto: Producto) {
        viewModelScope.launch {
            val uid = auth.uidCuentaActual()
            val esFavorito = listaFavorito.value.any { it.id == producto.id }
            db.cambiarFavorito(uid, producto, esFavorito)
        }
    }

}