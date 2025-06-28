package com.example.app_compuservic.ui.vistas.administrador.categoria

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_compuservic.modelos.Categoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryDialog(
    categoria: Categoria,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val nuevoNombre = remember { mutableStateOf(categoria.nombre) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Botón "X" en la parte superior derecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Text(
                    text = "Actualizar nom categoria",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nuevoNombre.value,
                    onValueChange = { nuevoNombre.value = it },
                    label = { Text("Nombre de Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(nuevoNombre.value) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002984)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Actualizar", color = Color.White)
                }
            }
        }
    )
}
