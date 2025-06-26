package com.example.app_compuservic.ui.vistas.administrador.producto

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.Producto
import com.example.app_compuservic.ui.estados.Estados

@Composable
fun ProductosVista(categoriaId: String, viewModel: ProductoViewModel = viewModel()) {
    // Cargar productos por categoriaId
    viewModel.obtenerProductos(categoriaId)

    // Obtener el estado de la lista de productos
    val productos = viewModel.productos.collectAsState().value

    LaunchedEffect(categoriaId) {
        Log.d("ProductosVista", "Cargando productos para la categoría: $categoriaId")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Productos en la categoría: $categoriaId",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mostrar productos con LazyColumn
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(productos) { producto ->
                ProductoItem(producto = producto)
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
            Text(text = "Precio: S/. ${producto.precio}", fontWeight = FontWeight.Medium)
            Text(text = "Descripción: ${producto.descripcion}")
            // Añadir más detalles de producto según sea necesario
        }
    }
}
