package com.example.app_compuservic.ui.vistas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_compuservic.navegador.gestionNavegacion.Rutas
import com.example.app_compuservic.repositorios.datoFireBase.TipoUsuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.example.app_compuservic.ui.vistas.administrador.categoria.DrawerBoton

@Composable

fun NavegadorLateral(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    cerrarSesion: () -> Unit,
    categoria: () -> Unit = {},
    tipoUsuario: TipoUsuario
) {
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

        when (tipoUsuario) {
            TipoUsuario.administrador -> {
                DrawerBoton("Inicio", Icons.Default.Home) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Rutas.PrincipalAdministrador.route)
                    }
                }
                DrawerBoton("Categoría", Icons.Default.Category) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Rutas.Categorias.route)
                    }
                }
                DrawerBoton("Productos", Icons.Default.GridView) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Rutas.Productos.route)
                    }
                }
                DrawerBoton("Mi Tienda", Icons.Default.Store) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Rutas.MiTienda.route)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                DrawerBoton("Cerrar Sesión", Icons.Default.ExitToApp) {
                    cerrarSesion()
                }
            }

            TipoUsuario.usuario -> {
                DrawerBoton("Inicio", Icons.Default.Home) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Mi Perfil", Icons.Default.Person) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Cerrar Sesión", Icons.Default.ExitToApp) {
                    cerrarSesion()
                }
            }

            TipoUsuario.nuevo_usuario -> { /* No muestra nada */ }
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
