package com.example.app_compuservic.ui.vistas.usuario.perfil

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarUbicacionVista(navController: NavController) {
    var posicionSeleccionada by remember {
        mutableStateOf(LatLng(-13.433889, -76.1375)) // Chincha Alta
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionSeleccionada, 16f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecciona una ubicación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Lat: %.4f, Lng: %.4f".format(posicionSeleccionada.latitude, posicionSeleccionada.longitude))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Guardar en el back stack para MiPerfilVista
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("ubicacionSeleccionada", posicionSeleccionada)

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8))
                ) {
                    Text("CONFIRMAR", color = Color.White)
                }
            }
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng -> posicionSeleccionada = latLng }
        ) {
            Marker(state = MarkerState(position = posicionSeleccionada))
        }
    }
}
