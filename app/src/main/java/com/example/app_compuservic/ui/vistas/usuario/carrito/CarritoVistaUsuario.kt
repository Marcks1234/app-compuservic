package com.example.app_compuservic.ui.vistas.usuario.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.ProductoCarrito

@Composable
fun CarritoVistaUsuario(
    viewModel: CarritoViewModel = viewModel()
) {
    val carrito by viewModel.carrito.collectAsState()

    Column(modifier = Modifier
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
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Cantidad: ${item.cantidad}")
                            Text("Precio: S/. ${item.producto.precioFinal ?: item.producto.precio}")
                        }
                    }
                }
            }
        }
    }
}
