package com.example.app_compuservic.ui.vistas.administrador.producto

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.app_compuservic.modelos.Producto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri


/**
como guardar imagenes de storage y accedes a su collection de store??

flujo de logica:
1.- generar carpeta con id del producto
2.- generar imagenes dentro -> (esto se puede hacer mediantes un index [imagen_0, imagen_1, imagen_2....])
3.- guardar en storage
4.- la url que se general guardar en un campo de store, se puede llamar urlList

flujo simplificado:
carpeta[producto_id] -> agregar imagenes[1.png, 2.png] -> agregar en store [documento(producto_id)]

actualizar imagenes:
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AñadirProductoVista(navController: NavController) {
    val productoEditar =
        navController.previousBackStackEntry?.savedStateHandle?.get<Producto>("productoEditar")

    var nombre by remember { mutableStateOf(productoEditar?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(productoEditar?.descripcion ?: "") }
    var marca by remember { mutableStateOf(productoEditar?.marca ?: "") }
    var categorias by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var categoriaSeleccionada by remember { mutableStateOf(productoEditar?.categoriaId ?: "") }
    var expandedCategoria by remember { mutableStateOf(false) }
    var precio by remember { mutableStateOf(productoEditar?.precio?.toString() ?: "") }
    var descuentoActivo by remember { mutableStateOf(productoEditar?.descuento != null) }
    var ejemploDescuento by remember { mutableStateOf("") }
    var porcentaje by remember { mutableStateOf(productoEditar?.descuento?.toString() ?: "") }
    var precioConDescuento by remember {
        mutableStateOf(productoEditar?.precioFinal?.let { "S/. %.2f".format(it) } ?: "")
    }
    var stock by remember { mutableStateOf(productoEditar?.stock ?: 0) }
    var imagenPrincipal by remember { mutableStateOf(productoEditar?.url ?: "") }
    var imagenesRemotas by remember { mutableStateOf(productoEditar?.urlList ?: emptyList()) }
    var imagenesUri by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var indiceRemotaAReemplazar by remember { mutableStateOf<Int?>(null) }
    var reemplazarImagenPrincipal by remember { mutableStateOf(false) }
    var imagenesRemotasReemplazadas by remember { mutableStateOf<Map<Int, Uri>>(emptyMap()) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            if (uris != null) imagenesUri = imagenesUri + uris
        }

    val launcherReemplazo =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                if (reemplazarImagenPrincipal) {
                    imagenPrincipal = uri.toString()
                    reemplazarImagenPrincipal = false
                } else if (indiceRemotaAReemplazar != null) {
                    val nuevasRemotas = imagenesRemotas.toMutableList()
                    nuevasRemotas[indiceRemotaAReemplazar!!] = uri.toString()
                    imagenesRemotas = nuevasRemotas
                    imagenesRemotasReemplazadas =
                        imagenesRemotasReemplazadas + (indiceRemotaAReemplazar!! to uri)
                    indiceRemotaAReemplazar = null
                }
            }
        }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val snapshot = Firebase.firestore.collection("categorias").get().await()
        categorias = snapshot.documents.mapNotNull {
            val nombre = it.getString("nombre") ?: return@mapNotNull null
            val id = it.id
            id to nombre
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (productoEditar != null) "Editar producto" else "Agregar un producto",
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {
                        if (nombre.isBlank() || descripcion.isBlank() || categoriaSeleccionada.isBlank() || precio.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor completa todos los campos obligatorios")
                            }
                            return@IconButton
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val db = Firebase.firestore
                                val docRef = productoEditar?.id?.let {
                                    db.collection("productos").document(it)
                                } ?: db.collection("productos").document()
                                val productoId = docRef.id
                                val storage = Firebase.storage.reference
                                val imagenesLocalesUri =
                                    imagenesUri.filter { it.scheme == "content" || it.scheme == "file" }

                                val imagenPrincipalUrl =
                                    if (imagenPrincipal.startsWith("content://") ||
                                        imagenPrincipal.startsWith("file://")
                                    ) {
                                        try {
                                            val imageRef =
                                                storage.child("productos/$productoId/principal.jpg")
                                            imageRef.putFile(imagenPrincipal.toUri()).await()
                                            imageRef.downloadUrl.await().toString()
                                        } catch (e: Exception) {
                                            productoEditar?.url ?: ""
                                        }
                                    } else {
                                        imagenPrincipal
                                    }

                                val imagenesFinalesSecundarias = mutableListOf<String>()

                                for ((index, uri) in imagenesRemotasReemplazadas) {
                                    try {
                                        val nombreArchivo = "imagen_$index.jpg"
                                        val destinoStorage = "productos/$productoId/$nombreArchivo"
                                        val imageRef = storage.child(destinoStorage)
                                        imageRef.putFile(uri).await()
                                        val nuevaUrl = imageRef.downloadUrl.await().toString()
                                        imagenesFinalesSecundarias.add(index, nuevaUrl)
                                    } catch (e: Exception) {
                                        Log.i(
                                            "nueva imagen",
                                            "Error al subir nueva imagen secundaria: ${e.message}"
                                        )
                                    }
                                }

                                imagenesRemotas.forEachIndexed { index, url ->
                                    if (!imagenesRemotasReemplazadas.containsKey(index)) {
                                        imagenesFinalesSecundarias.add(index, url)
                                    }
                                }
                                var indexAux = imagenesRemotas.size
                                imagenesLocalesUri.forEach { uri ->
                                    try {
                                        val nombreArchivo = "imagen_$indexAux.jpg"
                                        val destinoStorage = "productos/$productoId/$nombreArchivo"
                                        val imageRef = storage.child(destinoStorage)
                                        imageRef.putFile(uri).await()
                                        val url = imageRef.downloadUrl.await().toString()
                                        imagenesFinalesSecundarias.add(url)
                                        indexAux++
                                    } catch (e: Exception) {
                                        Log.i(
                                            "nueva imagen",
                                            "Error al subir nueva imagen secundaria: ${e.message}"
                                        )
                                    }
                                }

                                val producto = hashMapOf(
                                    "nombre" to nombre,
                                    "descripcion" to descripcion,
                                    "marca" to marca,
                                    "categoriaId" to categoriaSeleccionada,
                                    "precio" to precio.toDoubleOrNull(),
                                    "descuento" to if (descuentoActivo) porcentaje.toDoubleOrNull() else null,
                                    "precioFinal" to if (descuentoActivo) precioConDescuento.replace(
                                        "S/. ",
                                        ""
                                    ).toDoubleOrNull() else precio.toDoubleOrNull(),
                                    "url" to imagenPrincipalUrl,
                                    "urlList" to imagenesFinalesSecundarias,
                                    "precioFinal" to if (descuentoActivo) precioConDescuento.replace(
                                        "S/. ",
                                        ""
                                    ).toDoubleOrNull() else precio.toDoubleOrNull(),
                                    "stock" to stock,
                                    "fechaRegistro" to com.google.firebase.Timestamp.now()
                                )

                                docRef.set(producto).await()
                                val refCategoria =
                                    db.collection("categorias").document(categoriaSeleccionada)
                                        .collection("productos").document(docRef.id)
                                refCategoria.set(producto).await()

                                scope.launch {
                                    snackbarHostState.showSnackbar("Producto guardado exitosamente")
                                }

                            } catch (e: Exception) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Error al guardar: ${e.message}")
                                }
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.CloudUpload,
                            contentDescription = "Publicar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0033CC))
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


            Row(
                Modifier.fillMaxWidth()
            ) {
                Column(Modifier.wrapContentHeight(),verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (imagenPrincipal.isNotEmpty()) {
                    Text("Imagen Principal", fontSize = 16.sp)
                        AsyncImage(
                            model = imagenPrincipal,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    reemplazarImagenPrincipal = true
                                    launcherReemplazo.launch("image/*")
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else if(productoEditar == null){
                        Text("Agregar Imagen Principal", fontSize = 16.sp)
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    reemplazarImagenPrincipal = true
                                    launcherReemplazo.launch("image/*")
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Subir imagen",
                                tint = Color.Gray
                            )
                        }
                    }
                }
                Spacer(Modifier.width(80.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (imagenesRemotas.isNotEmpty()) {
                    Text("Imagenes Secundarias", fontSize = 16.sp)
                        LazyRow {
                            itemsIndexed(imagenesRemotas) { index, url ->
                                AsyncImage(
                                    model = url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clickable {
                                            indiceRemotaAReemplazar = index
                                            launcherReemplazo.launch("image/*")
                                        }
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                        }
                    }else if(productoEditar == null){
                        Text("")
                    }else{
                        Text("No hay imagenes secundarias")
                    }
                }
            }
            Text("Agregar imagen secundaria: ", fontSize = 16.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                imagenesUri.forEach { uri ->
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CloudUpload, contentDescription = "Agregar imagen")
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
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }) {
                OutlinedTextField(
                    value = categorias.firstOrNull { it.first == categoriaSeleccionada }?.second
                        ?: "",
                    onValueChange = {},
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true
                )
                DropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }) {
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
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) precio = it },
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
                    onValueChange = {
                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) ejemploDescuento = it
                    },
                    label = { Text("Ej. 10% OFF") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = porcentaje,
                    onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) porcentaje = it },
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033CC))
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
            // Stock
            Text("Stock", fontWeight = FontWeight.Medium)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { if (stock > 0) stock-- }) {
                    Text("−")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("$stock")
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { stock++ }) {
                    Text("+")
                }
            }

        }
    }
}
