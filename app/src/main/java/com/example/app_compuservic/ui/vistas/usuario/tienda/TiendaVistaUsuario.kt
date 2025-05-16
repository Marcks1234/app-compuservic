package com.example.app_compuservic.ui.vistas.usuario.tienda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.ui.vistas.componentes.ColumnaCategoria
import com.example.app_compuservic.ui.vistas.usuario.DestacadoProductoCard

@Composable
fun TiendaVistaUsuario(toProductos: (String) -> Unit, viewModel: TiendaUsuarioViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.obtenerNombre()
        viewModel.obtenerCategorias()
    }
    val nombre by viewModel.nombreActual.collectAsState()
    val listaCategoria by viewModel.listaCategoria.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Text("Bienvenido(a) : $nombre", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(8.dp))
        DestacadoProductoCard()
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Nuestras Categorías",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColumnaCategoria(listaCategoria = listaCategoria) { toProductos(it) }
    }
}