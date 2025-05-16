package com.example.app_compuservic.ui.vistas.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun FondoDecorativo(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp) // Aumentamos la altura de la onda
    ) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(width * 0.4f, 0f)

            // Primera ondita marcada
            quadraticBezierTo(width * 0.55f, height * 0.3f, width * 0.65f, height * 0.2f)

            // Segunda ondita
            quadraticBezierTo(width * 0.75f, height * 0.1f, width * 0.8f, height * 0.25f)

            // Última onda más caída
            quadraticBezierTo(width * 0.9f, height * 0.45f, width, height * 0.4f)

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
}