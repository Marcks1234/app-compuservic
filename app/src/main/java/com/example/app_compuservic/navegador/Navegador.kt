package com.example.app_compuservic.navegador

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas.*

@Composable
fun Navegador(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Login.route ) {
        composable (Login.route){

        }
    }

}