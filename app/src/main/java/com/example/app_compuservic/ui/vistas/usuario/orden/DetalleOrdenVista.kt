package com.example.app_compuservic.ui.vistas.usuario.orden

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app_compuservic.ui.vistas.usuario.orden.OrdenViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.Orden


@Composable
fun DetalleOrdenVista(
    ordenId: String,
    navController: NavHostController,
    viewModel: OrdenViewModel = viewModel()
) {
    val ordenState: Orden? by viewModel.orden.collectAsState(initial = null)

    LaunchedEffect(ordenId) {
        viewModel.cargarOrdenPorId(ordenId)
    }

    ordenState?.let { orden ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Detalle de la orden", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Id de la orden: ${orden.id}")
            Text("Fecha: ${orden.fecha}")
            Text("Estado: ${orden.estado}", color = Color(0xFF0288D1))
            Text("Cantidad de productos: ${orden.productos.size}")
            Text("Costo:\nTotal: ${"%.2f".format(orden.total)} USD")
            Text("Dirección: ${orden.direccion ?: "Sin dirección"}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("CONTINUAR", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Productos ordenados",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(orden.productos) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.nombre, fontWeight = FontWeight.Bold)
                            Text("Precio unidad: ${"%.2f".format(item.precio)} USD")
                            Text("Cantidad: ${item.cantidad}")
                            Text("Precio suma total: ${"%.2f".format(item.precio * item.cantidad)} USD")
                        }
                    }
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
