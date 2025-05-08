package com.example.app_compuservic.navegador

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas.*
import com.example.app_compuservic.ui.vistas.login.LoginVista
import com.example.app_compuservic.ui.vistas.principal.PrincipalVista
import com.example.app_compuservic.ui.vistas.registro.Registro_Cliente

@Composable
fun Navegador(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Login.route) {
        composable(route = Login.route) {
            LoginVista(
                toRegister = navegarHastaPantalla(navController, Registro, Login, false),
                toLogin = navegarHastaPantalla(navController, Principal, Login, true)
            )
        }
        composable(route = Registro.route) {
            Registro_Cliente(
                toLogin = navegarHastaPantalla(navController, Login, Registro, true),
                toPrincipal = navegarHastaPantalla(navController, Principal, Registro, true)
            )
        }
        composable(route = Principal.route) {
            PrincipalVista(toLogin = navegarHastaPantalla(navController, Login, Principal, true))
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

fun navegar(
    navController: NavHostController,
    direccion: Rutas,
): () -> Unit {

    return { navController.navigate(direccion.route, { launchSingleTop = true }) }
}