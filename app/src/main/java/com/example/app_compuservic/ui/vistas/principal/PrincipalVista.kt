package com.example.app_compuservic.ui.vistas.principal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun PrincipalVista(viewModel: PrincipalViewModel = viewModel(), toLogin: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("entraste en el Home", fontSize = 40.sp)
        Button(onClick = {
            viewModel.cerrarCuenta()
            toLogin()
        }) {
            Text("Cerrar cuenta")
        }
    }

}