package com.example.app_compuservic.modelos

data class ProductoCarrito (
    val producto: Producto = Producto(),
    val cantidad: Int = 1
)