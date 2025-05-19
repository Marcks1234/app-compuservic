package com.example.app_compuservic.navegador

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_compuservic.navegador.gestionNavegacion.UsuarioRutas.*
import com.example.app_compuservic.ui.vistas.usuario.favoritos.FavoritosVistaUsuario
import com.example.app_compuservic.ui.vistas.usuario.productos.ProductosVistaUsuario
import com.example.app_compuservic.ui.vistas.usuario.tienda.TiendaVistaUsuario

@Composable
fun UsuarioNavegador(raizNavController: NavHostController, navController: NavHostController) {

    NavHost(navController = navController, startDestination = Tienda.route) {

        composable(route = Tienda.route) {
            TiendaVistaUsuario(toProductos = { categoriaId ->
                navController.navigate(Productos.crearRutaConId(categoriaId))
            })
        }

        composable(route = Favoritos.route) {
            FavoritosVistaUsuario()
        }

        composable(
            route = Productos.route, arguments = listOf(navArgument("categoriaId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoriaId = backStackEntry.arguments!!.getString("categoriaId")
            if (categoriaId != null) {
                Log.i("busqueda categoria", categoriaId)
                ProductosVistaUsuario(categoriaId)
            }
        }
    }
}
