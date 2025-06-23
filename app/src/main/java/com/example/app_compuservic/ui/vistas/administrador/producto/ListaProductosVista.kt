package com.example.app_compuservic.ui.vistas.administrador.producto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.app_compuservic.modelos.Producto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProductosVista(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    var productos by remember { mutableStateOf(listOf<Producto>()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val snapshot = db.collection("productos").get().await()
        productos = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Producto::class.java)?.copy(id = doc.id)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Encabezado de búsqueda
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF002FA7))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) Text("Buscar producto", color = Color.Gray)
                        innerTextField()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())) {
            productos
                .filter { it.nombre.contains(searchQuery, ignoreCase = true) }
                .forEach { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = producto.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${producto.categoriaId} - ${producto.marca}", color = Color.Gray, fontSize = 13.sp)
                                Text("S/. ${"%.2f".format(producto.precio)}", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                                producto.descuento?.let {
                                    Text("${it.toInt()} % descuento", color = Color(0xFF002FA7), fontSize = 13.sp)
                                }
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("productoEditar", producto)
                                    navController.navigate("añadir_producto")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF002FA7))
                                }
                                IconButton(onClick = {
                                    scope.launch {
                                        db.collection("productos").document(producto.id).delete().await()
                                        db.collection("categorias").document(producto.categoriaId)
                                            .collection("productos").document(producto.id).delete().await()
                                        productos = productos.filter { it.id != producto.id }
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
        }
    }
}
