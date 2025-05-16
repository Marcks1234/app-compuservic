package com.example.app_compuservic.navegador

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas.*
import com.example.app_compuservic.ui.vistas.administrador.PrincipalVistaAdministrador
import com.example.app_compuservic.ui.vistas.administrador.categoria.CategoriasVista
import com.example.app_compuservic.ui.vistas.login.LoginVista
import com.example.app_compuservic.ui.vistas.usuario.PrincipalVistaUsuario
import com.example.app_compuservic.ui.vistas.registro.Registro_Cliente

@Composable
fun Navegador(navController: NavHostController) {

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
            PrincipalVistaUsuario(navController)
        }
        composable(route = Categorias.route) {
            CategoriasVista()
        }
    }

}

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

