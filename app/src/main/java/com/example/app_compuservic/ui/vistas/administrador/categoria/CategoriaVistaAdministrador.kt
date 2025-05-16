package com.example.app_compuservic.ui.vistas.administrador.categoria

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

data class Categoria(
    val nombre: String,
    val imagenUri: Uri?
)

val listaCategorias = mutableStateListOf<Categoria>()

@Composable
fun CategoriasVista() {
    var nombreCategoria by remember { mutableStateOf("") }
    var imagenSeleccionada by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenSeleccionada = uri
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Título
        Text("Tienda Virtual", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para seleccionar imagen
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E0E0))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imagenSeleccionada == null) {
                Icon(Icons.Filled.Photo, contentDescription = "Seleccionar imagen", modifier = Modifier.size(40.dp))
            } else {
                AsyncImage(
                    model = imagenSeleccionada,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // TextField para nombre
        OutlinedTextField(
            value = nombreCategoria,
            onValueChange = { nombreCategoria = it },
            label = { Text("Ingrese categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botón agregar
        Button(
            onClick = {
                if (nombreCategoria.isNotBlank()) {
                    listaCategorias.add(Categoria(nombreCategoria, imagenSeleccionada))
                    nombreCategoria = ""
                    imagenSeleccionada = null
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033CC)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Categoría", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Lista de categorías", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar lista
        listaCategorias.forEach { categoria ->
            CategoriaItem(
                categoria = categoria,
                onEliminar = { listaCategorias.remove(categoria) },
                onEditar = {
                    nombreCategoria = categoria.nombre
                    imagenSeleccionada = categoria.imagenUri
                }
            )
        }
    }
}

@Composable
fun CategoriaItem(categoria: Categoria, onEliminar: () -> Unit, onEditar: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            if (categoria.imagenUri != null) {
                AsyncImage(
                    model = categoria.imagenUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(categoria.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { /* ver productos */ },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Ver Productos")
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
                IconButton(onClick = onEditar) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Blue)
                }
            }
        }
    }
}