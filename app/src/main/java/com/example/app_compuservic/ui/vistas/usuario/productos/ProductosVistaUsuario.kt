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

@Composable
fun ProductosVistaUsuario(categoriaid: String, viewModel: ProductosUsuarioViewModel = viewModel()) {
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
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            items(listaProducto) {
                val esFavorito = listaFavorito.contains(it.id)
                Card(shape = RoundedCornerShape(18.dp)) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("IMAGEN DEL PRODUCTO")
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(7f)
                                    .padding(horizontal = 8.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(it.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(it.marca, color = Color.Red)
                                Text(it.id)
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "S/.${it.precio}",
                                        fontSize = 15.sp,
                                        color = Color.LightGray
                                    )
                                    Text("Unid: ${it.stock}", fontSize = 15.sp)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(onClick = {
                                    viewModel.cambiarFavorito(it)
                                }) {
                                    Icon(
                                        if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "like"
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                                }
                            }
                        }
                    }
                }
            }

        }

    }


}