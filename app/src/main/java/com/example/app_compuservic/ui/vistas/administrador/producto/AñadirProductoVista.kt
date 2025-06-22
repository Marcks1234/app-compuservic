package com.example.app_compuservic.ui.vistas.administrador.producto

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AñadirProductoVista(viewModel: ProductoViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar un producto") },
                actions = {
                    IconButton(onClick = {
                        val producto = Producto(
                            nombre = nombre,
                            categoriaId = categoriaId,
                            marca = marca,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            stock = stock.toIntOrNull() ?: 0,
                            isLike = false,
                            id = "", // se genera en el ViewModel
                        )
                        viewModel.agregarProducto(producto)
                        // Limpiar campos si deseas
                        nombre = ""
                        categoriaId = ""
                        marca = ""
                        precio = ""
                        stock = ""
                        url = ""
                    }) {
                        Icon(Icons.Default.Upload, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = categoriaId, onValueChange = { categoriaId = it }, label = { Text("ID Categoría") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL Imagen") }, modifier = Modifier.fillMaxWidth())
        }
    }
}
