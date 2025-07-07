package com.example.app_compuservic.navegador

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas.*
import com.example.app_compuservic.ui.vistas.administrador.PrincipalVistaAdministrador
import com.example.app_compuservic.ui.vistas.administrador.categoria.CategoriasVista
import com.example.app_compuservic.ui.vistas.login.LoginVista
import com.example.app_compuservic.ui.vistas.administrador.producto.AñadirProductoVista
import com.example.app_compuservic.ui.vistas.administrador.producto.ListaProductosVista
import com.example.app_compuservic.ui.vistas.administrador.producto.ProductosVista
import com.example.app_compuservic.ui.vistas.usuario.PrincipalVistaUsuario
import com.example.app_compuservic.ui.vistas.registro.Registro_Cliente
import com.example.app_compuservic.ui.vistas.usuario.tienda.TiendaVistaUsuario


@Composable
fun Navegador(navController: NavHostController) {

    //Define el contenedor de navegación. --> la pantalla incial sera la del login .

    NavHost(navController = navController, startDestination = Login.route) {
        composable(route = Login.route) {
            LoginVista(
                toRegister = navegarHastaPantalla(navController, Registro, Login, false),
                toHomeAdmin = navegarHastaPantalla(
                    navController,
                    PrincipalAdministrador,
                    Login,
                    true
                ),
                toHomeUser = navegarHastaPantalla(navController, PrincipalUsuario, Login, true)
            )
        }
        composable(route = Registro.route) {
            Registro_Cliente(
                toLogin = navegarHastaPantalla(navController, Login, Registro, true),
                toPrincipal = navegarHastaPantalla(
                    navController,
                    PrincipalAdministrador,
                    Registro,
                    true
                )
            )
        }
        composable(route = PrincipalAdministrador.route) {
            PrincipalVistaAdministrador(
                navController, toLogin = navegarHastaPantalla(
                    navController, Login,
                    PrincipalAdministrador, true
                )
            )
        }
        composable(route = PrincipalUsuario.route) {
            PrincipalVistaUsuario(
                navController,
                toLogin = navegarHastaPantalla(navController, Login, PrincipalUsuario, true)
            )
        }
        composable(route = Rutas.Productos.route) {
            Text("Vista de productos del administrador (por construir)")
        }

        // Ruta con parámetro dinámico : Esta pantalla recibe un parámetro (categoriaId) en la URL
        composable(route = "productos/{categoriaId}") { backStackEntry ->
            val categoriaId = backStackEntry.arguments?.getString("categoriaId") ?: ""
            ProductosVista(categoriaId = categoriaId, navController = navController)
        }
        composable(route = Rutas.MiTienda.route) {
            TiendaVistaUsuario(toProductos = { categoriaId ->
                navController.navigate("productos/$categoriaId")
            })
        }

        composable(route = Categorias.route) {
            CategoriasVista()
        }
        composable(route = Rutas.AñadirProducto.route) {
            AñadirProductoVista(navController = navController)
        }
        composable(route = Rutas.ListaProductos.route) {
            ListaProductosVista(navController = navController)
        }


    }

}

//Sirve para navegar a otra pantalla y
// opcionalmente eliminar del historial las anteriores.
fun navegarHastaPantalla(
    navController: NavHostController,
    direccion: Rutas,
    eliminarHasta: Rutas,
    incluirUltima: Boolean
): () -> Unit {
    return {
        navController.navigate(direccion.route, {
            popUpTo(eliminarHasta.route) { inclusive = incluirUltima }
            launchSingleTop = true
        })
    }
}

