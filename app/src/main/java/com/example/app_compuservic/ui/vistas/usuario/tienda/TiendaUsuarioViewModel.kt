package com.example.app_compuservic.ui.vistas.usuario.tienda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.Categoria
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.Estados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TiendaUsuarioViewModel(
    val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    val db: FireStoreRepositorio = FireStoreRepositorio()

) :
    ViewModel() {
    var estados = MutableStateFlow<Estados<List<Categoria>>>(Estados.Vacio)
        private set


    var listaCategoria = MutableStateFlow<List<Categoria>>(emptyList())
        private set

    var nombreActual = MutableStateFlow<String>("")

    fun obtenerCategorias() {
        viewModelScope.launch {
            estados.value = Estados.Cargando
            db.obtenerCategorias().collect {
                estados.value = it
                if (it is Estados.Exito) {
                    listaCategoria.value = it.datos
                }
            }
        }
    }

    fun obtenerNombre(){
        viewModelScope.launch {
         nombreActual.value = db.obtenerDatosUsuarioActual(auth.uidCuentaActual())
        }
    }
    fun cerrarCuenta() {
        auth.cerrarCuenta()
    }

    fun existeCuenta(): Boolean {
        return auth.existeCuenta()
    }


}

