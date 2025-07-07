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
    //repositorio: maneja login, logout y datos del usuario con Firebase Auth.
    //repositorioStore: accede a Firestore (por ejemplo, para obtener categorías).
    private val repositorio: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val repositorioStore: FireStoreRepositorio = FireStoreRepositorio()
) :
    ViewModel() {

    //Es un StateFlow que contiene la lista de categorías.
    //Se usa para mostrar las categorías en Compose.
    //private set = Solo se puede modificar desde dentro del ViewModel.
    var categorias = MutableStateFlow<List<Categoria>>(emptyList())
        private set


    fun cerrarCuenta() {
        repositorio.cerrarCuenta()
    }
    //Devuelve true si hay un usuario logueado.
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

