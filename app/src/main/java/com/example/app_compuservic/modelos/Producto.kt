package com.example.app_compuservic.modelos

data class Producto(
    val id: String = "",
    val categoriaId: String = "sin categoria",
    val marca: String = "sin marca",
    val nombre: String = "sin nombre" ,
    val precio: Double = 0.00,
    val stock: Int = 0,
    val isLike: Boolean = false
)
