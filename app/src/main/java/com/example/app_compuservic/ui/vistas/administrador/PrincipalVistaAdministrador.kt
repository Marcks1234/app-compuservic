package com.example.app_compuservic.ui.vistas.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalVistaAdministrador(
    navController: NavHostController,
    viewModel: PrincipalViewModel = viewModel(),
    toLogin: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF002984))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Bienvenido(a)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                DrawerBoton("Inicio", Icons.Default.Home) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Categorías", Icons.Default.Category) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Rutas.Categorias.route)
                    }
                }
                DrawerBoton("Productos", Icons.Default.GridView) {
                    scope.launch { drawerState.close() }
                    navController.navigate("lista_productos")
                }
                DrawerBoton("Mi tienda", Icons.Default.Store) {
                    scope.launch { drawerState.close() }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                DrawerBoton("Cerrar Sesión", Icons.Default.ExitToApp) {
                    viewModel.cerrarCuenta()
                    toLogin()
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Computer, contentDescription = null, tint = Color(0xFF0033CC), modifier = Modifier.size(40.dp))
                    Text("Compuservic", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tienda Virtual", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF002984))
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF002984)) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Category, contentDescription = null, tint = Color.White)
                                Text("Mis productos", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                        }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {navController.navigate(Rutas.AñadirProducto.route) },
                        icon = {
                            Icon(Icons.Default.Add, contentDescription = "Agregar producto")
                        }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null)
                                Text("Órdenes", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                        }
                    )

                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Text("Bienvenido a tu tienda", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun DrawerBoton(texto: String, icono: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = texto, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(12.dp))
        Text(texto, fontSize = 16.sp, color = Color.DarkGray)
    }
}