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
import androidx.compose.ui.Alignment
import com.example.app_compuservic.modelos.Orden
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import android.widget.Toast
import com.example.app_compuservic.repositorios.MercadoPagoService
import com.google.firebase.auth.FirebaseAuth



@Composable
fun DetalleOrdenVista(
    ordenId: String,
    navController: NavHostController,
    viewModel: OrdenViewModel = viewModel(),

) {
    val ordenState: Orden? by viewModel.orden.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user = FirebaseAuth.getInstance().currentUser



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
            // Text("Costo:\nTotal: ${"%.2f".format(orden.total)} USD")
            Text("Total: S/. %.2f".format(orden.total))
            Text("Dirección: ${orden.direccion ?: "Sin dirección"}")

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val url = MercadoPagoService.crearPreferenciaPago(
                                total = orden.total,
                                ordenId = orden.id,
                                email = FirebaseAuth.getInstance().currentUser?.email ?: "correo@default.com",
                                uid = FirebaseAuth.getInstance().currentUser?.uid ?: "sin_uid"
                            )
                            withContext(Dispatchers.Main) {
                                if (url != null) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Error al generar el link de pago", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
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
                            Text("Precio unidad: S/. %.2f".format(item.precio))
                            Text("Cantidad: ${item.cantidad}")
                            Text("Precio suma total: S/. %.2f".format(item.precio * item.cantidad))
                        }
                    }
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}