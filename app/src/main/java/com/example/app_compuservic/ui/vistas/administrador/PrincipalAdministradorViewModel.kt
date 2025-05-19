package com.example.app_compuservic.ui.vistas.administrador

import androidx.lifecycle.ViewModel
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio


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