package com.example.app_compuservic.ui.vistas.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Categoria
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.EstadoCategoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PrincipalViewModel(
    val repositorioAuth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    val repositorioStore: FireStoreRepositorio = FireStoreRepositorio()

) :
    ViewModel() {
    var estadoCategoria = MutableStateFlow<EstadoCategoria>(EstadoCategoria.Vacio)
        private set


    var listaCategoria = MutableStateFlow<List<Categoria>>(emptyList())
        private set

    init {
        obtenerCategorias()
    }

    fun obtenerCategorias() {
        viewModelScope.launch {
            estadoCategoria.value = EstadoCategoria.Cargando
            val resultado = repositorioStore.obtenerCategorias()
            estadoCategoria.value = resultado

            if (resultado is EstadoCategoria.Exito) {
                listaCategoria.value = resultado.listaCategoria
            }
        }
    }


    fun cerrarCuenta() {
        repositorioAuth.cerrarCuenta()
    }

    fun existeCuenta(): Boolean {
        return repositorioAuth.existeCuenta()
    }


}

