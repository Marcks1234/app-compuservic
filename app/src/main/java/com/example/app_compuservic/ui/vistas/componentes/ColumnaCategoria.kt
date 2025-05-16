package com.example.app_compuservic.ui.vistas.componentes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_compuservic.modelos.Categoria

@Composable
fun ColumnaCategoria(listaCategoria: List<Categoria>, toProduct: (String) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(800.dp)
    ) {

        items(listaCategoria) { categoria ->
            val context = LocalContext.current

            @Suppress("DiscouragedApi")
            val resId = remember(categoria.imagenRes) {
                context.resources.getIdentifier(
                    categoria.imagenRes,
                    "drawable",
                    context.packageName
                )
            }
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    .clickable {  /* */ }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = categoria.nombre,
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(categoria.nombre.toString(), fontWeight = FontWeight.Bold)
                    Button(
                        onClick = {
                            if (!categoria.id.isNullOrEmpty()) {
                                Log.i("identificar Categoria de ColumnaCategoria", categoria.id)
                                toProduct(categoria.id)
                            }
                        },
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text("Ver Productos", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}