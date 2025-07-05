package com.example.app_compuservic.ui.vistas.usuario.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.app_compuservic.modelos.ProductoCarrito
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun CarritoVistaUsuario(
    viewModel: CarritoViewModel = viewModel()
) {

    val carrito by viewModel.carrito.collectAsState()
    //agregamos
    val usuarioActual = Firebase.auth.currentUser

    LaunchedEffect(Unit) {
        //agregamos
        if (usuarioActual != null) {//agregamos
            viewModel.cargarCarritoCuandoUsuarioDisponible()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Carrito de Compras", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (carrito.isEmpty()) {
            Text("Tu carrito está vacío.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(carrito) { item: ProductoCarrito ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp) // Cambia esto
                        ) {
                            // Imagen
                            AsyncImage(
                                model = item.producto.url,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )

                            // Info + eliminar en una sola columna con espacio entre ellos
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    item.producto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Cantidad: ${item.cantidad}")
                                Text("Precio: S/. ${item.producto.precioFinal ?: item.producto.precio}")
                            }

                            // Ícono de eliminar
                            IconButton(
                                onClick = {
                                    viewModel.eliminarProducto(item.producto.id)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}