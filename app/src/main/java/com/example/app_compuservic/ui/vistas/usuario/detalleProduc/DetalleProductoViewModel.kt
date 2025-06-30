package com.example.app_compuservic.ui.vistas.usuario.detalleProduc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val repositorio: FireStoreRepositorio = FireStoreRepositorio()
) : ViewModel() {

    private val _producto = MutableStateFlow<Producto?>(null)
    val producto = _producto.asStateFlow()

    fun cargarProductoPorId(Id: String) {
        viewModelScope.launch {
            val result = repositorio.obtenerProductoPorId(Id)
            _producto.value = result
        }
    }
}