package com.example.app_compuservic.repositorios.datoFireBase

enum class FireStoreColeccion(val valor: String) {

    C_USUARIOS("usuarios"),
    C_CATEGORIAS("categorias"),
    C_PEDIDOS("pedidos"),
    C_PRODUCTOS("productos")
}


sealed class TipoUsuario() {
    object usuario : TipoUsuario()
    object administrador : TipoUsuario()
    object nuevo_usuario : TipoUsuario()
}