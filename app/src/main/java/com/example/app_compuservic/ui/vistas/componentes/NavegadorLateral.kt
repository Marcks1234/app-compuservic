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
import com.example.app_compuservic.ui.vistas.administrador.DrawerBoton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavegadorLateral(
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
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Categorías", Icons.Default.Category) {
                    scope.launch {
                        drawerState.close()
                        categoria()
                    }
                }
                DrawerBoton("Productos", Icons.Default.GridView) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Mi tienda", Icons.Default.Store) {
                    scope.launch { drawerState.close() }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                DrawerBoton("Cerrar Sesión", Icons.Default.ExitToApp) {
                    cerrarSesion()
                }

            }

            TipoUsuario.nuevo_usuario -> {}

            TipoUsuario.usuario -> {
                DrawerBoton("Tienda", Icons.Default.Store) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Favorito", Icons.Default.Favorite) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Carrito", Icons.Default.AddShoppingCart) {
                    scope.launch { drawerState.close() }
                }
                DrawerBoton("Cerrar Sesión", Icons.Default.ExitToApp) {
                    cerrarSesion()
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp))


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