package com.example.app_compuservic.ui.vistas.administrador.producto

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosVista(categoriaId: String, viewModel: ProductoViewModel = viewModel(), navController: NavController) {

    // Obtener el estado de la lista de productos
    val productos = viewModel.productos.collectAsState().value

    LaunchedEffect(categoriaId) {
        viewModel.obtenerProductos(categoriaId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior con fondo azul y flecha para regresar
        TopAppBar(
            title = {
                Text("Productos - Administrador", color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("principalAdministrador") }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0033CC))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar productos con LazyColumn
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(productos) { producto ->
                ProductoItem(producto = producto, navController = navController)
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aquí debes usar AsyncImage para cargar la imagen si la URL está disponible
            // AsyncImage
            Spacer(modifier = Modifier.width(16.dp))

            // Información del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Marca: ${producto.marca}", fontSize = 14.sp, color = Color.Gray)
                Text("Precio: S/. ${"%.2f".format(producto.precio)}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                producto.descuento?.let {
                    Text("${it.toInt()} % descuento", color = Color(0xFF0033CC), fontSize = 14.sp)
                }
            }

            // Botones para editar y eliminar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    // Lógica para editar el producto
                    navController.navigate("editar_producto/${producto.id}")
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar producto", tint = Color(0xFF0033CC))
                }
                IconButton(onClick = {
                    // Lógica para eliminar el producto
                    // Eliminar producto de Firestore
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto", tint = Color.Red)
                }
            }
        }
    }
}
