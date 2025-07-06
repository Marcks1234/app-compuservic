package com.example.app_compuservic.modelos

data class Usuario(
    val nombre: String? = null,
    val email: String? = null,
    val administrador: Boolean = false,
    val telefono: String = "",
    val dni: String = "",
    val ubicacion: String = "",
    val fechaRegistro: String = ""
)
