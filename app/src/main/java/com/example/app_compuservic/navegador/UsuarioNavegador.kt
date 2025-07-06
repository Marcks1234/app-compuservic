package com.example.app_compuservic.navegador

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_compuservic.navegador.gestionNavegacion.UsuarioRutas.*
import com.example.app_compuservic.ui.vistas.usuario.favoritos.FavoritosVistaUsuario
import com.example.app_compuservic.ui.vistas.usuario.detalleProduc.DetalleProductoVista
import com.example.app_compuservic.ui.vistas.usuario.productos.ProductosVistaUsuario
import com.example.app_compuservic.ui.vistas.usuario.tienda.TiendaVistaUsuario
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.app_compuservic.ui.vistas.usuario.carrito.CarritoViewModel
import com.example.app_compuservic.ui.vistas.usuario.carrito.CarritoVistaUsuario
import com.example.app_compuservic.ui.vistas.usuario.detalleProduc.DetalleProductoViewModel
import com.example.app_compuservic.ui.vistas.usuario.perfil.MiPerfilVista

@Composable
fun UsuarioNavegador(
    raizNavController: NavHostController,
    navController: NavHostController) {

    //agregado

    val carritoViewModel: CarritoViewModel = viewModel()

    NavHost(navController = navController, startDestination = Tienda.route) {

        composable(route = Tienda.route) {
            TiendaVistaUsuario(toProductos = { categoriaId ->
                navController.navigate(Productos.crearRutaConId(categoriaId))
            })
        }

        composable(route = Favoritos.route) {
            FavoritosVistaUsuario(navController = navController)
        }
        composable(route = Carrito.route) {
            CarritoVistaUsuario(viewModel = carritoViewModel,navController = navController)

        }
        composable("mi_perfil") {
            MiPerfilVista(navController = navController)
        }

        composable(
            route = "detalle_producto/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            val viewModel: DetalleProductoViewModel = viewModel()
            val productoState = viewModel.producto.collectAsState()
            val producto = productoState.value


            LaunchedEffect(productoId) {
                viewModel.cargarProductoPorId(productoId)
            }

            producto?.let {
                DetalleProductoVista(
                    producto = it,
                    navController = navController, // ✅ se pasa aquí
                    onAgregarAlCarrito = { producto, cantidad ->
                        carritoViewModel.agregarAlCarrito(producto,cantidad)

                    }
                )
            }
        }


        composable(
            route = Productos.route,
            arguments = listOf(navArgument("categoriaId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->

            val categoriaId = backStackEntry.arguments?.getString("categoriaId") ?: ""
            Log.i("busqueda categoria", categoriaId)

            ProductosVistaUsuario(
                categoriaid = categoriaId,
                navController = navController
            )
        }


    }
}
