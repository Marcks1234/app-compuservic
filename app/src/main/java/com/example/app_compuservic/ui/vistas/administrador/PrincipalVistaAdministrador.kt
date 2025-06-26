package com.example.app_compuservic.ui.vistas.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalVistaAdministrador(
    navController: NavHostController,
    viewModel: PrincipalViewModel = viewModel(),
    toLogin: () -> Unit
) {
    val listCategory by viewModel.categorias.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (listCategory.isEmpty()) viewModel.getListCategory()
    }
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

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

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
                    Icon(
                        Icons.Default.Computer,
                        contentDescription = null,
                        tint = Color(0xFF0033CC),
                        modifier = Modifier.size(40.dp)
                    )
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
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
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
                                Icon(
                                    Icons.Default.Category,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    "Mis productos",
                                    color = Color.White,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                )
                            }
                        }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(Rutas.AñadirProducto.route) },
                        icon = {

                            Icon(Icons.Default.Add, tint = Color.White, contentDescription = "Agregar producto")
                        }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color.White)
                                Text(
                                    "Órdenes",
                                    color = Color.White,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {

                    items(listCategory) { category ->

                        Card(Modifier.padding(5.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AsyncImage(
                                    model = category.url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Text(
                                    category.nombre,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Button(border = BorderStroke(1.dp, Color(0xFF002984)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    onClick = {
                                        // Navegar a la pantalla de productos pasando el categoriaId
                                        navController.navigate("productos/${category.id}")

                                    }) {
                                    Text("Ver productos", color = Color.Black)
                                }
                                HorizontalDivider()
                                Row(
                                    Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    IconButton(onClick = {


                                    }) {
                                        Icon(Icons.Default.Edit, "editar", tint = Color(0xAE162C46))
                                    }
                                    IconButton(onClick = {



                                    }) {
                                        Icon(Icons.Default.Delete, "editar", tint = Color.Red)
                                    }
                                }
                            }
                        }


                    }
                }
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