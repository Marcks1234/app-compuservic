package com.example.app_compuservic.navegador.gestionNavegacion

sealed class Rutas(val route: String) {

    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object PrincipalUsuario : Rutas("principalUsuario")
    object Categorias : Rutas("categorias")
    object PrincipalAdministrador : Rutas("principalAdministrador")


}