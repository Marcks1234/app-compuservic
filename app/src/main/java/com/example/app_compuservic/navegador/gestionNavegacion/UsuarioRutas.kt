package com.example.app_compuservic.navegador.gestionNavegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.example.app_compuservic.R

sealed class UsuarioRutas(val route: String = "", val label: String = "", val icon: Int = 0) {

    object Tienda : UsuarioRutas("tienda", "Tienda", R.drawable.ic_tienda)
    object Favoritos : UsuarioRutas("favoritos", "Favoritos", R.drawable.ic_favoritos)
    object Carrito : UsuarioRutas("carrito", "Carrito", R.drawable.ic_carrito)
    object Ordenes : UsuarioRutas("ordenes", "Órdenes", R.drawable.ic_ordenes)
    object Productos : UsuarioRutas("productos/{categoriaId}") {
        fun crearRutaConId(categoriaId: String): String {
            return "productos/$categoriaId"
        }

    }

}