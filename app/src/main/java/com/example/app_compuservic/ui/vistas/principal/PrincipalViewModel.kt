package com.example.app_compuservic.ui.vistas.principal

import androidx.lifecycle.ViewModel
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import kotlinx.coroutines.flow.MutableStateFlow

class PrincipalViewModel(
    val repositorio: FireBaseAuthRepositorio = FireBaseAuthRepositorio()
) :
    ViewModel() {

    fun cerrarCuenta() {
        repositorio.cerrarCuenta()
    }
    fun existeCuenta() : Boolean{
        return repositorio.existeCuenta()
    }
}