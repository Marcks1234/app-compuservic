package com.example.app_compuservic.ui.vistas.usuario.carrito

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.app_compuservic.modelos.ProductoCarrito
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.navigation.NavHostController

@Composable
fun CarritoVistaUsuario(
    viewModel: CarritoViewModel = viewModel(),
    navController: NavHostController
) {
    val carrito by viewModel.carrito.collectAsState()
    val context = LocalContext.current
    val usuarioActual = Firebase.auth.currentUser

    // Calcular el total
    val total = carrito.sumOf {
        val precio = it.producto.precioFinal ?: it.producto.precio
        precio * it.cantidad
    }

    LaunchedEffect(Unit) {
        if (usuarioActual != null) {
            viewModel.cargarCarritoCuandoUsuarioDisponible()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(bottom = 72.dp) // Espacio para botón fijo
        ) {
            Text(
                text = "Total: S/. %.2f".format(total),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    color = Color(0xFF0011A8) // Color púrpura elegante
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (carrito.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(carrito) { item: ProductoCarrito ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.producto.url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            if (item.cantidad > 1)
                                                viewModel.actualizarCantidad(item.producto.id, item.cantidad - 1)
                                        }) {
                                            Text("➖")
                                        }
                                        Text("${item.cantidad}")
                                        IconButton(onClick = {
                                            viewModel.actualizarCantidad(item.producto.id, item.cantidad + 1)
                                        }) {
                                            Text("➕")
                                        }
                                    }

                                    val precioUnitario = item.producto.precioFinal ?: item.producto.precio
                                    Text("Subtotal: S/. %.2f".format(precioUnitario * item.cantidad))
                                }

                                IconButton(onClick = {
                                    viewModel.eliminarProducto(item.producto.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }

        // ✅ Botón fijo inferior
        // ✅ Botón fijo inferior funcional
        val viewModelCarrito = viewModel<CarritoViewModel>()
        val uid = Firebase.auth.currentUser?.uid

        Button(
            onClick = {
                if (uid != null) {
                    val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    val nuevaOrdenId = firestore
                        .collection("usuarios").document(uid)
                        .collection("ordenes").document().id

                    // Cargar la dirección del usuario desde Firestore
                    firestore.collection("usuarios").document(uid).get()
                        .addOnSuccessListener { doc ->
                            val direccionGuardada = doc.getString("ubicacion") ?: "Sin dirección"

                            val orden = com.example.app_compuservic.modelos.Orden(
                                id = nuevaOrdenId,
                                fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                                estado = "Solicitud recibida",
                                direccion = direccionGuardada,
                                total = carrito.sumOf {
                                    val precio = it.producto.precioFinal ?: it.producto.precio
                                    precio * it.cantidad.toDouble()
                                },
                                productos = carrito.map {
                                    com.example.app_compuservic.modelos.ItemOrden(
                                        nombre = it.producto.nombre,
                                        precio = it.producto.precioFinal ?: it.producto.precio,
                                        cantidad = it.cantidad
                                    )
                                }
                            )

                            // Guardar la orden
                            firestore.collection("usuarios").document(uid)
                                .collection("ordenes").document(nuevaOrdenId)
                                .set(orden)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Orden confirmada", Toast.LENGTH_SHORT).show()
                                    viewModel.limpiarCarrito() // Asegúrate de tener esta función en tu ViewModel
                                    navController.navigate("detalle_orden/$nuevaOrdenId")

                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al confirmar orden", Toast.LENGTH_SHORT).show()
                                }

                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "No se pudo obtener dirección del usuario", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
            enabled = carrito.isNotEmpty()
        ) {
            Text("CONFIRMAR ORDEN", color = Color.White)
        }

    }
}
