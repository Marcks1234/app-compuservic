package com.example.app_compuservic.ui.vistas.administrador.categoria
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CategoriaViewModel : ViewModel() {

    private val _nombreCategoria = MutableStateFlow("")
    val nombreCategoria: StateFlow<String> = _nombreCategoria

    private val _imagenUrl = MutableStateFlow("https://cdn-icons-png.flaticon.com/512/1828/1828884.png")
    val imagenUrl: StateFlow<String> = _imagenUrl

    private val _listaCategorias = MutableStateFlow<List<Categoria>>(emptyList())
    val listaCategorias: StateFlow<List<Categoria>> = _listaCategorias

    fun cambiarNombre(nuevo: String) {
        _nombreCategoria.value = nuevo
    }

    fun cambiarImagen(nuevaUrl: String) {
        _imagenUrl.value = nuevaUrl
    }

    fun agregarCategoria() {
        if (_nombreCategoria.value.isNotBlank()) {
            val nueva = Categoria(_nombreCategoria.value, null)
            _listaCategorias.value = _listaCategorias.value + nueva
            _nombreCategoria.value = ""
            _imagenUrl.value = "https://cdn-icons-png.flaticon.com/512/1828/1828884.png"
        }
    }

    fun eliminarCategoria(categoria: Categoria) {
        _listaCategorias.value = _listaCategorias.value - categoria
    }

    fun editarCategoria(categoria: Categoria) {
        _nombreCategoria.value = categoria.nombre
        _imagenUrl.value = categoria.imagenUri?.toString() ?: ""
    }
}