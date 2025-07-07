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
import com.example.app_compuservic.ui.vistas.usuario.orden.DetalleOrdenVista
import com.example.app_compuservic.ui.vistas.usuario.perfil.MiPerfilVista
import com.example.app_compuservic.ui.vistas.usuario.perfil.SeleccionarUbicacionVista

@Composable

fun UsuarioNavegador(
    raizNavController: NavHostController,
    navController: NavHostController) {

    val carritoViewModel: CarritoViewModel = viewModel()

    NavHost(navController = navController, startDestination = Tienda.route) {

       //Al seleccionar una categoría, navega a la vista de productos
        // de esa categoría.
        composable(route = Tienda.route) {
            TiendaVistaUsuario(toProductos = { categoriaId ->
                navController.navigate(Productos.crearRutaConId(categoriaId))
            })
        }

        //Muestra los productos favoritos del usuario.
        composable(route = Favoritos.route) {
            FavoritosVistaUsuario(navController = navController)
        }

        //Usa un CarritoViewModel compartido para mantener los productos agregados.
        composable(route = Carrito.route) {
            CarritoVistaUsuario(viewModel = carritoViewModel,navController = navController)
        }

        composable("mi_perfil") {
            MiPerfilVista(navController = navController)
        }

        composable("seleccionar_ubicacion") {
            SeleccionarUbicacionVista(navController = navController)
        }

        //Muestra los detalles de una orden en específico.
        composable("detalle_orden/{ordenId}") { backStackEntry ->
            val ordenId = backStackEntry.arguments?.getString("ordenId") ?: ""
            DetalleOrdenVista(ordenId = ordenId, navController = navController)
        }

        //Al hacer clic en un producto, se navega a su vista de detalle
        composable(
            route = "detalle_producto/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            val viewModel: DetalleProductoViewModel = viewModel() //Carga el producto con su ViewModel
            val productoState = viewModel.producto.collectAsState()
            val producto = productoState.value


            LaunchedEffect(productoId) {
                viewModel.cargarProductoPorId(productoId)
            }

            producto?.let {
                DetalleProductoVista( //Permite agregarlo al carrito
                    producto = it,
                    navController = navController,
                    onAgregarAlCarrito = { producto, cantidad ->
                        carritoViewModel.agregarAlCarrito(producto,cantidad)

                    }
                )
            }
        }

        //Muestra los productos de una categoría específica
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
