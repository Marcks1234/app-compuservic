package com.example.app_compuservic.ui.vistas.administrador.producto

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AñadirProductoVista() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var expandedCategoria by remember { mutableStateOf(false) }
    var precio by remember { mutableStateOf("") }
    var descuentoActivo by remember { mutableStateOf(false) }
    var ejemploDescuento by remember { mutableStateOf("") }
    var porcentaje by remember { mutableStateOf("") }
    var precioConDescuento by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri
    }

    LaunchedEffect(Unit) {
        val snapshot = Firebase.firestore.collection("categorias").get().await()
        categorias = snapshot.documents.mapNotNull {
            val nombre = it.getString("nombre") ?: return@mapNotNull null
            val id = it.id
            id to nombre
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar un producto", color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        if (nombre.isBlank() || descripcion.isBlank() || categoriaSeleccionada.isBlank() || precio.isBlank()) {
                            println("Por favor completa todos los campos obligatorios")
                            return@IconButton
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val ref = Firebase.firestore.collection("productos").document()
                                val producto = hashMapOf(
                                    "nombre" to nombre,
                                    "descripcion" to descripcion,
                                    "categoriaId" to categoriaSeleccionada,
                                    "precio" to precio.toDoubleOrNull(),
                                    "descuento" to if (descuentoActivo) porcentaje.toDoubleOrNull() else null,
                                    "precioFinal" to if (descuentoActivo) precioConDescuento.replace("S/. ", "").toDoubleOrNull() else precio.toDoubleOrNull(),
                                    "url" to (imagenUri?.toString() ?: ""),
                                    "fechaRegistro" to com.google.firebase.Timestamp.now()
                                )
                                ref.set(producto).await()

                                val refCategoria = Firebase.firestore
                                    .collection("categorias")
                                    .document(categoriaSeleccionada)
                                    .collection("productos")
                                    .document(ref.id)
                                refCategoria.set(producto).await()

                                println("Producto publicado correctamente")
                            } catch (e: Exception) {
                                println("Error al subir producto: ${e.message}")
                            }
                        }
                    }) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Publicar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF002FA7))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                } else {
                    Icon(Icons.Filled.CloudUpload, contentDescription = "Seleccionar imagen")
                }
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria },
            ) {
                OutlinedTextField(
                    value = categorias.firstOrNull { it.first == categoriaSeleccionada }?.second ?: "",
                    onValueChange = {},
                    label = { Text("Categoría") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true
                )
                DropdownMenu(expanded = expandedCategoria, onDismissRequest = { expandedCategoria = false }) {
                    categorias.forEach { (id, nombreCat) ->
                        DropdownMenuItem(text = { Text(nombreCat) }, onClick = {
                            categoriaSeleccionada = id
                            expandedCategoria = false
                        })
                    }
                }
            }

            OutlinedTextField(
                value = precio,
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Aplicar descuento?", fontWeight = FontWeight.SemiBold)
                Switch(checked = descuentoActivo, onCheckedChange = { descuentoActivo = it })
            }

            if (descuentoActivo) {
                OutlinedTextField(
                    value = ejemploDescuento,
                    onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) ejemploDescuento = it },
                    label = { Text("Ej. 10% OFF") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = porcentaje,
                    onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) porcentaje = it },
                    label = { Text("Porcentaje (ej: 20, 50)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val precioNum = precio.toDoubleOrNull() ?: 0.0
                        val porcentajeNum = porcentaje.toDoubleOrNull() ?: 0.0
                        val descuento = precioNum * (porcentajeNum / 100)
                        val total = precioNum - descuento
                        precioConDescuento = "S/. %.2f".format(total)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002FA7))
                ) {
                    Text("Calcular", color = Color.White)
                }

                Text("Precio con descuento aplic.", fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = precioConDescuento,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("S/.") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
