package com.example.app_compuservic.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.app_compuservic.navegador.Navegador

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
             val navController = rememberNavController()
            Navegador(navController)
            //ProductosVista("ve81uVYEybDxidalYh3U")
        }
    }
}
@Composable
fun SolicitarPermisoGaleria(onPermisoConcedido: () -> Unit) {
    val contexto = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) onPermisoConcedido()
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}
