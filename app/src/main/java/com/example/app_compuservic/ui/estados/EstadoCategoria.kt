package com.example.app_compuservic.ui.estados

import com.example.app_compuservic.modelos.Categoria

sealed class EstadoCategoria(){
    object Vacio: EstadoCategoria()
    object Cargando: EstadoCategoria()
    class Exito(val listaCategoria: List<Categoria>): EstadoCategoria()
    class Error(val mensaje: String): EstadoCategoria()


}
