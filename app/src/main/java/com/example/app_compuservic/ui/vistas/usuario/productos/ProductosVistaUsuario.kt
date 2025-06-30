package com.example.app_compuservic.ui.vistas.usuario.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app_compuservic.ui.vistas.componentes.ColumnaProducto

@Composable
fun ProductosVistaUsuario(categoriaid: String,
                          navController: NavHostController,
                          viewModel: ProductosUsuarioViewModel = viewModel()
) {

    val listaProducto by viewModel.listaProducto.collectAsState()
    val listaFavorito by viewModel.listaFavorito.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerProductos(categoriaid)
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ColumnaProducto(
            listaProducto = listaProducto,
            listaFavorito = listaFavorito,
            favoritoBoton = { viewModel.cambiarFavorito(it)},
            infoBoton = { producto ->
                navController.navigate("detalle_producto/${producto.id}")
            }
        )
    }

}