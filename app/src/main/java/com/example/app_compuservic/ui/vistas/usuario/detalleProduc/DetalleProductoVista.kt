package com.example.app_compuservic.ui.vistas.usuario.detalleProduc

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.ui.vistas.usuario.carrito.CarritoViewModel

@Composable
fun DetalleProductoVista(
    producto: Producto,
    navController: NavHostController,
    onAgregarAlCarrito: (Producto,Int) -> Unit

) {
    var expandirImagen by remember { mutableStateOf(false) }
    var mostrarMas by remember { mutableStateOf(false) }
    var cantidad by remember { mutableStateOf(1) }


    val carritoViewModel: CarritoViewModel = viewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Imagen principal
        AsyncImage(
            model = producto.url,
            contentDescription = "Imagen del producto",
            modifier = Modifier
                .fillMaxWidth()
                .height(if (expandirImagen) 400.dp else 200.dp)
                .clickable { expandirImagen = !expandirImagen },
            contentScale = if (expandirImagen) ContentScale.Fit else ContentScale.Crop
        )

        Spacer(Modifier.height(12.dp))

        // Información principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(producto.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(producto.marca, color = Color.Gray)

                producto.descuento?.takeIf { it > 0 }?.let {
                    Text(
                        "S/ ${producto.precio}",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        "S/ ${producto.precioFinal}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "${it.toInt()}% de descuento",
                        color = Color(0xFF2E7D32)
                    )
                } ?: Text(
                    "S/ ${producto.precio}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))
                // Sección de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cantidad:", fontWeight = FontWeight.Medium)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (cantidad > 1) cantidad-- },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("-")
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(cantidad.toString())
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { cantidad++ },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("+")
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))


                Button(
                    onClick = {
                        if (cantidad <= producto.stock) {
                            onAgregarAlCarrito(producto, cantidad)
                            Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No hay suficiente stock", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = producto.stock > 0
                ) {
                    Text("Agregar al Carrito")
                }

            }
        }

        Spacer(Modifier.height(16.dp))

        // Especificaciones
        Text("Especificaciones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))

        if (mostrarMas) {
            Text("Descripción:", fontWeight = FontWeight.SemiBold)
            Text(producto.descripcion)

            Spacer(Modifier.height(8.dp))
            Text("Marca:", fontWeight = FontWeight.SemiBold)
            Text(producto.marca)

            Spacer(Modifier.height(8.dp))
            Text("Stock disponible:", fontWeight = FontWeight.SemiBold)
            Text("${producto.stock} unidades")
        } else {
            Text(producto.descripcion.take(70) + "...")
        }

        Text(
            text = if (mostrarMas) "Mostrar menos ▲" else "Mostrar más ▼",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { mostrarMas = !mostrarMas },
            color = Color(0xFF1565C0),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}
