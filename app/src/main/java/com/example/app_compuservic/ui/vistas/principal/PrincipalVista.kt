package com.example.app_compuservic.ui.vistas.principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.R
import com.example.app_compuservic.ui.estados.EstadoCategoria
import com.example.app_compuservic.ui.vistas.componentes.GirdTwoColumns


@Composable
fun DestacadoProductoCard() {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = R.drawable.laptop_demo),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "IdeaPad Slim 3i 14' 10ma Gen - Luna Grey",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text("Laptops", color = Color.Blue, fontSize = 12.sp)
                Text("S/. 2,749.01", color = Color.Red, fontWeight = FontWeight.Bold)
                Text("19% de descuento", fontSize = 15.sp, color = Color.Black)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .align(Alignment.Bottom)
                    .clickable { /* acción */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Ir",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color(0xFF003FA1),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_tienda),
                        contentDescription = "Tienda",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("Tienda", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favoritos),
                        contentDescription = "Favoritos",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("Favoritos", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_carrito),
                        contentDescription = "Carrito",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("Carrito", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_ordenes),
                        contentDescription = "Órdenes",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { Text("Órdenes", color = Color.White) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalVista(viewModel: PrincipalViewModel = viewModel(), toLogin: () -> Unit) {
    val estadoCategoria by viewModel.estadoCategoria.collectAsState()
    val listaCategoria by viewModel.listaCategoria.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda Virtual", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* abrir menú */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF003FA1))
            )
        },
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Text("Bienvenido(a) : Patricia Avila ", fontSize = 14.sp)

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
            GirdTwoColumns(listaCategoria = listaCategoria)

        }
    }
}




