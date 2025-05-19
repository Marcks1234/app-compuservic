package com.example.app_compuservic.ui.vistas.usuario.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.ui.vistas.componentes.ColumnaProducto

@Composable
fun FavoritosVistaUsuario(viewModel: FavoritoViewModel = viewModel()) {
    val listaFavoritos by viewModel.listaFavorito.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.obtenerFavoritosTiempoReal()
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ColumnaProducto(
            colorFavoritoRojo = true,
            listaProducto = listaFavoritos,
            favoritoBoton = { producto ->
                viewModel.cambiarFavorito(producto)
            }
        )
    }
}