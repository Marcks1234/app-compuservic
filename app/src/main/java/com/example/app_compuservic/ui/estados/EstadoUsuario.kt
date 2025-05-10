package com.example.app_compuservic.ui.estados

sealed class EstadoUsuario {
    object vacio : EstadoUsuario()
    object cargando : EstadoUsuario()
    object Exito : EstadoUsuario()
    data class Error(val mensaje: String) : EstadoUsuario()
}


