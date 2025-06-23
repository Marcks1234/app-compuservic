package com.example.app_compuservic.navegador.gestionNavegacion

sealed class Rutas(val route: String) {

    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object PrincipalUsuario : Rutas("principalUsuario")
    object Categorias : Rutas("categorias")
    object Productos : Rutas("productos")
    object MiTienda : Rutas("mitienda")
    object PrincipalAdministrador : Rutas("principalAdministrador")
    object AñadirProducto : Rutas("añadir_producto")
    object ListaProductos : Rutas("lista_productos")

}