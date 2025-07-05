package com.example.app_compuservic.ui.vistas.administrador

import android.net.Uri
import android.os.Build
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.app_compuservic.repositorios.StorageRepositorio

@Composable
fun SubirImagenUI() {
    var uriImagen by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uriImagen = it
            StorageRepositorio.subirImagen(
                uri = it,
                nombreArchivo = "imagen_${System.currentTimeMillis()}",
                onSuccess = { url ->
                    Toast.makeText(context, "Imagen subida: $url", Toast.LENGTH_LONG).show()
                },
                onError = {
                    Toast.makeText(context, "Error al subir: ${it.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    Column(Modifier.padding(16.dp)) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(Modifier.height(16.dp))

        uriImagen?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen seleccionada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}
