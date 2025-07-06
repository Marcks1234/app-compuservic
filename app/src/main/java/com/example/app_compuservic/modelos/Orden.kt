package com.example.app_compuservic.modelos

data class Orden(
    val id: String = "",
    val fecha: String = "",
    val estado: String = "Solicitud recibida",
    val direccion: String? = null,
    val total: Double = 0.0,
    val productos: List<ItemOrden> = emptyList()
)