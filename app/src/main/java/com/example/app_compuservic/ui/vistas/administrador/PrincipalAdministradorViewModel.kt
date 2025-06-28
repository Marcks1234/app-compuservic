package com.example.app_compuservic.ui.vistas.administrador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Categoria
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.Estados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class PrincipalViewModel(
    private val repositorio: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val repositorioStore: FireStoreRepositorio = FireStoreRepositorio()
) :
    ViewModel() {


    var categorias = MutableStateFlow<List<Categoria>>(emptyList())
        private set


    fun cerrarCuenta() {
        repositorio.cerrarCuenta()
    }

    fun existeCuenta(): Boolean {
        return repositorio.existeCuenta()
    }

    fun getListCategory() {
        viewModelScope.launch {
            repositorioStore.obtenerCategorias().collect {
                when (it) {
                    Estados.Cargando -> {}
                    is Estados.Error -> {}
                    is Estados.Exito -> {
                        categorias.value = it.datos
                    }

                    Estados.Vacio -> {}
                }
            }

        }
    }
}

