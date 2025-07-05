package com.example.app_compuservic.modelos

data class Producto(
    val id: String = "",
    val categoriaId: String = "sin categoria",
    val descripcion: String = "",
    val marca: String = "sin marca",
    val nombre: String = "sin nombre",
    val precio: Double = 0.00,
    val stock: Int = 0,
    val isLike: Boolean = false,
    val descuento: Double? = null,
    val precioFinal: Double? = null,
    val url: String = "",
    val urlList: List<String> = emptyList<String>()
) : java.io.Serializable
