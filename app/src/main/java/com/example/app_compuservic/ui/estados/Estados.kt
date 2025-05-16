package com.example.app_compuservic.ui.estados

import com.example.app_compuservic.modelos.Categoria

sealed class Estados<out T>(){
    object Vacio: Estados<Nothing>()
    object Cargando: Estados<Nothing>()
    class Exito<out T>(val datos: T): Estados<T>()
    class Error(val mensaje: String): Estados<Nothing>()
}
