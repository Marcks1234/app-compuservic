package com.example.app_compuservic.ui.vistas.usuario.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MiPerfilVista(navController: NavHostController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        db.collection("usuarios").document(uid).get().addOnSuccessListener { doc ->
            nombre = doc.getString("nombre") ?: ""
            email = doc.getString("email") ?: ""
            telefono = doc.getString("telefono") ?: ""
            dni = doc.getString("dni") ?: ""
            ubicacion = doc.getString("ubicacion") ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "MI PERFIL",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Icono circular
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF6A1B9A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campos de texto
        listOf(
            Triple(nombre, "Nombres Completos") { it: String -> nombre = it },
            Triple(email, "Email") { it: String -> email = it },
            Triple(telefono, "Teléfono") { it: String -> telefono = it },
            Triple(dni, "DNI") { it: String -> dni = it },
            Triple(ubicacion, "Ubicación") { it: String -> ubicacion = it }
        ).forEach { (valor, label, onChange) ->
            OutlinedTextField(
                value = valor,
                onValueChange = onChange,
                label = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val datos = mapOf(
                    "nombre" to nombre,
                    "email" to email,
                    "telefono" to telefono,
                    "dni" to dni,
                    "ubicacion" to ubicacion
                )

                db.collection("usuarios").document(uid).update(datos)
                    .addOnSuccessListener {
                        mensaje = "Datos actualizados correctamente."
                    }
                    .addOnFailureListener {
                        mensaje = "Error al actualizar: ${it.message}"
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
            shape = RoundedCornerShape(30)
        ) {
            Icon(Icons.Default.Save, contentDescription = "Guardar", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("GUARDAR", color = Color.White)
        }

        if (mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}
