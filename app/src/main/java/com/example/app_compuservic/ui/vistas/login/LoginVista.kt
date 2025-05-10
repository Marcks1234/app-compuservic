package com.example.app_compuservic.ui.vistas.login

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.ui.estados.EstadoUsuario

@Composable
fun LoginVista(
    viewModel: LoginViewModel = viewModel(),
    toRegister: () -> Unit,
    toLogin: () -> Unit
) {
    val estadoUsuario by viewModel.estadoUsuario.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    LaunchedEffect(estadoUsuario) {
        if (estadoUsuario == EstadoUsuario.Exito) {
            toLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Onda decorativa azul fuerte
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Aumentamos la altura de la onda
                .align(Alignment.TopEnd)
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(width * 0.4f, 0f)

                // Primera ondita marcada
                quadraticBezierTo(
                    width * 0.55f, height * 0.3f, width * 0.65f, height * 0.2f
                )

                // Segunda ondita
                quadraticBezierTo(
                    width * 0.75f, height * 0.1f, width * 0.8f, height * 0.25f
                )

                // Última onda más caída
                quadraticBezierTo(
                    width * 0.9f, height * 0.45f, width, height * 0.4f
                )


                // Cierre de la figura
                lineTo(width, 0f)
                close()
            }

            drawPath(
                path = path, brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0033CC), Color(0xFF0066FF))
                )
            )
        }

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

            Text(
                text = "Please sign in to continue", fontSize = 14.sp, color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.agregarEmail(it) },
                label = { Text("Email") },
                placeholder = { Text("email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.agregarPassword(it) },
                label = { Text("Password") },
                placeholder = { Text("password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón login (sin flecha)
            Button(
                onClick = { viewModel.loginUsuario(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033FF)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("LOGIN", color = Color.White)
            }

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
            when (estadoUsuario) {
                is EstadoUsuario.Error -> Text(estadoUsuario.toString())
                EstadoUsuario.Exito -> Text("INGRESASTE")
                EstadoUsuario.cargando -> CircularProgressIndicator()
                EstadoUsuario.vacio -> {}
            }
        }
    }
}
