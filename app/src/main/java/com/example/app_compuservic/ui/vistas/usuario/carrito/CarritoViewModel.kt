package com.example.app_compuservic.ui.vistas.usuario.carrito
import androidx.lifecycle.ViewModel
import com.example.app_compuservic.modelos.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.app_compuservic.modelos.ProductoCarrito

class CarritoViewModel : ViewModel() {
    private val _carrito = MutableStateFlow<List<ProductoCarrito>>(emptyList())
    val carrito: StateFlow<List<ProductoCarrito>> = _carrito

    fun agregarAlCarrito(producto: Producto) {
        val actual = _carrito.value.toMutableList()
        val index = actual.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val itemExistente = actual[index]
            actual[index] = itemExistente.copy(cantidad = itemExistente.cantidad + 1)
        } else {
            actual.add(ProductoCarrito(producto))
        }

        _carrito.value = actual
    }

    fun eliminarProducto(productoId: String) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == productoId }

        fun limpiarCarrito() {
            _carrito.value = emptyList()
        }
    }
}
