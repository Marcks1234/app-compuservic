package com.example.app_compuservic.navegador.gestionNavegacion

sealed class Rutas(val route: String) {

    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Principal : Rutas("home")

}