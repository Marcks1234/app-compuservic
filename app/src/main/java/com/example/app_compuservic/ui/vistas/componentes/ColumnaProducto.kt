package com.example.app_compuservic.ui.vistas.componentes

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_compuservic.modelos.Producto

@Composable
fun ColumnaProducto(
    listaProducto: List<Producto>,
    listaFavorito: Set<String> = emptySet(),
    favoritoBoton: (Producto) -> Unit = {},
    infoBoton: () -> Unit = {},
    colorFavoritoRojo: Boolean = false,
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
            val producto: Producto = it
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
                            IconButton(onClick = { favoritoBoton(producto) }) {
                                Icon(
                                    if (colorFavoritoRojo) Icons.Default.Favorite
                                    else if (esFavorito) Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder,

                                    contentDescription = "like",
                                    tint = if (colorFavoritoRojo) Color.Red
                                    else if (esFavorito) Color.Red
                                    else Color.Gray
                                )
                            }
                            IconButton(onClick = infoBoton) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                            }
                        }
                    }
                }
            }
        }

    }
}