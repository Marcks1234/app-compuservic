package com.example.app_compuservic.ui.vistas.usuario

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_compuservic.R
import com.example.app_compuservic.navegador.UsuarioNavegador
import com.example.app_compuservic.navegador.gestionNavegacion.UsuarioRutas
import com.example.app_compuservic.navegador.gestionNavegacion.UsuarioRutas.*
import com.example.app_compuservic.repositorios.datoFireBase.TipoUsuario
import com.example.app_compuservic.ui.vistas.administrador.PrincipalViewModel
import com.example.app_compuservic.ui.vistas.componentes.NavegadorLateral

val listaRutasCategoria = listOf<UsuarioRutas>(
    Tienda,
    Favoritos,
    Carrito,
    Ordenes
)

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
fun BottomNavigationBar(navController: NavHostController, rutaActual: String) {
    NavigationBar(
        containerColor = Color(0xFF003FA1),
        contentColor = Color.White
    ) {
        listaRutasCategoria.forEach { ruta ->
            NavigationBarItem(
                selected = rutaActual == ruta.route,
                onClick = {
                    if (rutaActual != ruta.route) {
                        navController.navigate(ruta.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(id = ruta.icon),
                            contentDescription = ruta.route,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = { Text(ruta.label, color = Color.White) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalVistaUsuario(
    raizNavController: NavHostController,
    toLogin: () -> Unit,
    viewModel: PrincipalViewModel = PrincipalViewModel()
) {

    val navController = rememberNavController()
    val pilaTraseraActual by navController.currentBackStackEntryAsState()
    val rutaActual = pilaTraseraActual?.destination?.route ?: "tienda"
    val drawState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    Log.i("ruta actual", rutaActual)
    ModalNavigationDrawer(
        drawerState = drawState,
        drawerContent = {
            NavegadorLateral(
                tipoUsuario = TipoUsuario.usuario,
                drawerState = drawState,
                scope = scope,
                cerrarSesion = {
                    viewModel.cerrarCuenta()
                    toLogin()
                })
        }
    ) {
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
            bottomBar = { BottomNavigationBar(navController, rutaActual) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                UsuarioNavegador(
                    raizNavController = raizNavController,
                    navController = navController
                )
            }
        }
    }
}




