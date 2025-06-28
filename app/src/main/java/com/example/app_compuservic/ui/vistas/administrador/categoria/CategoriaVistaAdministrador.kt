package com.example.app_compuservic.ui.vistas.administrador.categoria

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasVista() {
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    var nombreCategoria by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var categorias by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var editandoId by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri
        imagenUrl = uri.toString()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar categorías al inicio
    LaunchedEffect(Unit) {
        val snapshot = db.collection("categorias").get().await()
        categorias = snapshot.documents.map { doc ->
            mapOf(
                "id" to doc.id,
                "nombre" to (doc.getString("nombre") ?: ""),
                "url" to (doc.getString("url") ?: "")
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tienda Virtual", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0033CC))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Cuadro para imagen
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(24.dp))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri != null) {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (imagenUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Filled.Image, contentDescription = "Seleccionar imagen")
                }
            }

            // Formulario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nombreCategoria,
                    onValueChange = { nombreCategoria = it },
                    label = { Text("Ingrese Categoría") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (nombreCategoria.isNotBlank()) {
                        scope.launch {
                            try {
                                if (editandoId == null) {
                                    db.collection("categorias").add(
                                        mapOf("nombre" to nombreCategoria, "url" to imagenUrl)
                                    ).await()
                                } else {
                                    db.collection("categorias").document(editandoId!!)
                                        .update("nombre", nombreCategoria, "url", imagenUrl)
                                        .await()
                                    editandoId = null
                                }
                                nombreCategoria = ""
                                imagenUrl = ""
                                imagenUri = null
                                val snapshot = db.collection("categorias").get().await()
                                categorias = snapshot.documents.map { doc ->
                                    mapOf(
                                        "id" to doc.id,
                                        "nombre" to (doc.getString("nombre") ?: ""),
                                        "url" to (doc.getString("url") ?: "")
                                    )
                                }
                                snackbarHostState.showSnackbar("Categoría guardada correctamente")
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error: ${e.message}")
                            }
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033CC))) {
                    Text(if (editandoId == null) "Agregar Categoría" else "Actualizar")
                }
                if (editandoId != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = {
                        editandoId = null
                        nombreCategoria = ""
                        imagenUrl = ""
                        imagenUri = null
                    }) {
                        Text("Cancelar")
                    }
                }
            }

            Text("Lista de categorías", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            categorias.forEach { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = categoria["url"],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(categoria["nombre"] ?: "", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Button(
                                onClick = { /* navegar o mostrar productos */ },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Ver Productos")
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = {
                                editandoId = categoria["id"]
                                nombreCategoria = categoria["nombre"] ?: ""
                                imagenUrl = categoria["url"] ?: ""
                                imagenUri = null
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    db.collection("categorias").document(categoria["id"]!!).delete().await()
                                    categorias = categorias.filter { it["id"] != categoria["id"] }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
