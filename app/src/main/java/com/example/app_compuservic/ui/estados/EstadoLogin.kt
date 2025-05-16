package com.example.app_compuservic.ui.estados

import com.example.app_compuservic.repositorios.datoFireBase.TipoUsuario

sealed class EstadoLogin {
    object Cargando : EstadoLogin()
    data class Exito(val tipoUsuario: TipoUsuario) : EstadoLogin()
    data class Error(val mensaje: String) : EstadoLogin()
    object Vacio : EstadoLogin()
}