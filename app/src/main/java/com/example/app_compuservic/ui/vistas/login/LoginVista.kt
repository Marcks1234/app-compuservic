package com.example.app_compuservic.ui.vistas.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.repositorios.datoFireBase.TipoUsuario
import com.example.app_compuservic.ui.estados.EstadoLogin
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.ui.vistas.componentes.CampoEntrada
import com.example.app_compuservic.ui.vistas.componentes.FondoDecorativo

@Composable
fun LoginVista(
    viewModel: LoginViewModel = viewModel(),
    toRegister: () -> Unit,
    toHomeAdmin: () -> Unit,
    toHomeUser: () -> Unit,
) {
    val estadoLogin by viewModel.estadoLogin.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize()) {

        FondoDecorativo(modifier = Modifier.align(Alignment.TopEnd))

        // Contenido principal alineado a la izquierda
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Login",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "Please sign in to continue", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            CampoEntrada(label = "Email", icon = Icons.Default.Email, value = email) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            CampoEntrada(
                isPassword = true,
                label = "Password",
                icon = Icons.Default.Lock,
                value = password
            ) { password = it }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón login (sin flecha)
            Button(
                onClick = { viewModel.loginUsuario(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033FF)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text("LOGIN", color = Color.White) }

            Spacer(modifier = Modifier.height(24.dp))

            // Texto de registro alineado a la izquierda
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { toRegister() }) {
                    Text("sing up", color = Color(0xFF0033FF), fontWeight = FontWeight.Bold)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (estadoLogin) {
                    EstadoLogin.Cargando -> CircularProgressIndicator()
                    is EstadoLogin.Error -> Text((estadoLogin as EstadoLogin.Error).mensaje)
                    is EstadoLogin.Exito -> {
                        val tipo = (estadoLogin as EstadoLogin.Exito).tipoUsuario
                        when (tipo) {
                            is TipoUsuario.administrador -> toHomeAdmin()
                            is TipoUsuario.usuario ->toHomeUser()
                            TipoUsuario.nuevo_usuario -> {}
                        }
                    }
                    EstadoLogin.Vacio -> {}
                }
            }
        }
    }
}
