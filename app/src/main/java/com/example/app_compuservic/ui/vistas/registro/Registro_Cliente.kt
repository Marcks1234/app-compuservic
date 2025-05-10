package com.example.app_compuservic.ui.vistas.registro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.modelos.Usuario

@Composable
fun Registro_Cliente(
    viewModel: RegistroClienteViewModel = viewModel(),
    toLogin: () -> Unit,
    toPrincipal: () -> Unit
) {
    val estadoUsuario by viewModel.estadoUsario.collectAsState()
    val mostrarError by viewModel.mostrarError.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmarPassword by viewModel.confirmarPassword.collectAsState()

    LaunchedEffect(estadoUsuario) {
        if (estadoUsuario == EstadoUsuario.Exito) {
            toPrincipal()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Onda superior derecha
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopEnd)
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(width * 0.4f, 0f)
                quadraticBezierTo(width * 0.55f, height * 0.3f, width * 0.65f, height * 0.2f)
                quadraticBezierTo(width * 0.75f, height * 0.1f, width * 0.8f, height * 0.25f)
                quadraticBezierTo(width * 0.9f, height * 0.45f, width, height * 0.4f)
                lineTo(width, 0f)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0033CC), Color(0xFF0066FF))
                )
            )
        }

        // Contenido centrado del formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CampoInput(
                    value = nombre,
                    OnValueChange = { viewModel.cambiarNombre(it) },
                    icon = Icons.Default.Person,
                    label = "Full Name",
                    placeholder = "full name"
                )
                CampoInput(
                    value = email,
                    OnValueChange = { viewModel.cambiarEmail(it) },
                    icon = Icons.Default.Email, label = "Email", placeholder = "email",
                )
                CampoInput(
                    value = password,
                    OnValueChange = {
                        viewModel.cambiarPassword(it)
                        viewModel.cambiarMostrarError(false)
                    },
                    icon = Icons.Default.Lock,
                    label = "Password",
                    placeholder = "Password",
                    esPassword = true
                )
                CampoInput(
                    value = confirmarPassword,
                    OnValueChange = {
                        viewModel.cambiarConfirmarPassword(it)
                        viewModel.cambiarMostrarError(false)
                    },
                    icon = Icons.Default.Lock,
                    label = "Confirm Password",
                    placeholder = "Confirm Password",
                    esPassword = true
                )

                Spacer(modifier = Modifier.height(24.dp))
                if (mostrarError) {
                    Text("Las contraseñas no coinciden", color = Color.Red)
                }
                Button(
                    onClick = {
                        if (viewModel.verificarPassword()) {
                            viewModel.crearUsuario(
                                email,
                                password,
                                Usuario(nombre = nombre, email = email)
                            )
                        }else{
                            viewModel.cambiarMostrarError(true)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0033CC)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "SIGN UP →", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Text(
                        text = "Sign in",
                        modifier = Modifier.clickable { toLogin() },
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
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
}

@Composable
fun CampoInput(
    value: String,
    OnValueChange: (String) -> Unit,
    icon: ImageVector,
    label: String,
    placeholder: String,
    esPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = OnValueChange,
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(icon, contentDescription = null) },
            visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        )
    }
}


