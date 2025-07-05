package com.example.app_compuservic.ui.vistas.administrador.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.app_compuservic.modelos.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosVista(
    categoriaId: String,
    viewModel: ProductoViewModel = viewModel(),
    navController: NavController
) {
    val productos = viewModel.productos.collectAsState().value
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    LaunchedEffect(categoriaId) {
        viewModel.obtenerProductos(categoriaId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Productos - Administrador", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("principalAdministrador") }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0033CC))
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(productos) { producto ->
                ProductoItem(
                    producto = producto,
                    navController = navController,
                    onEliminarClick = {
                        productoAEliminar = it
                        mostrarDialogo = true
                    }
                )
            }
        }

        if (mostrarDialogo && productoAEliminar != null) {
            DeleteProductDialog(
                producto = productoAEliminar!!,
                onDismiss = { mostrarDialogo = false },
                onConfirm = {
                    scope.launch {
                        db.collection("productos").document(productoAEliminar!!.id).delete().await()
                        db.collection("categorias").document(productoAEliminar!!.categoriaId)
                            .collection("productos").document(productoAEliminar!!.id).delete().await()

                        mostrarDialogo = false
                        viewModel.obtenerProductos(productoAEliminar!!.categoriaId) // refresca la lista
                    }
                }
            )
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    navController: NavController,
    onEliminarClick: (Producto) -> Unit
) {
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
            // ✅ Imagen del producto
            AsyncImage(
                model = producto.url,
                contentDescription = "Imagen del producto",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 15.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Marca: ${producto.marca}", fontSize = 14.sp, color = Color.Gray)
                Text("Precio: S/. ${"%.2f".format(producto.precio)}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                producto.descuento?.let {
                    Text("${it.toInt()} % descuento", color = Color(0xFF0033CC), fontSize = 14.sp)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("productoEditar", producto)
                    navController.navigate("añadir_producto")
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar producto", tint = Color(0xFF0033CC))
                }
                IconButton(onClick = {
                    onEliminarClick(producto)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto", tint = Color.Red)
                }
            }
        }
    }
}
