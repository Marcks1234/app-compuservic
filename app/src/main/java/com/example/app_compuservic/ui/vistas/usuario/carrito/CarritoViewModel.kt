package com.example.app_compuservic.ui.vistas.usuario.carrito
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_compuservic.modelos.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.app_compuservic.modelos.ProductoCarrito
import com.example.app_compuservic.repositorios.CarritoRepositorio
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CarritoViewModel : ViewModel() {
    private val _carrito = MutableStateFlow<List<ProductoCarrito>>(emptyList())
    val carrito: StateFlow<List<ProductoCarrito>> = _carrito

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        val actual = _carrito.value.toMutableList()
        val index = actual.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val itemExistente = actual[index]
            actual[index] = itemExistente.copy(cantidad = itemExistente.cantidad + cantidad)
        } else {
            actual.add(ProductoCarrito(producto, cantidad))
        }
        _carrito.value = actual
        guardarProductoFirestore(producto, cantidad)

    }

    fun guardarProductoFirestore(producto: Producto, cantidad: Int) {
        val item = ProductoCarrito(producto, cantidad)
        CarritoRepositorio.guardarProductoEnCarrito(
            productoCarrito = item,
            onSuccess = { Log.d("CarritoViewModel", "Producto guardado en Firestore") },
            onError = { e -> Log.e("CarritoViewModel", "Error al guardar: ${e.message}") }
        )
    }
    fun cargarCarritoCuandoUsuarioDisponible() {
        Firebase.auth.addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                Firebase.firestore
                    .collection("usuarios")
                    .document(user.uid)
                    .collection("carrito")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val productos = snapshot.documents.mapNotNull {
                            it.toObject(ProductoCarrito::class.java)
                        }
                        _carrito.value = productos
                    }
            }
        }
    }


    fun obtenerCarritoDesdeFirestore() {
        val uid = Firebase.auth.currentUser?.uid
        if (uid.isNullOrEmpty()) return  // Evita crashes

        Firebase.firestore
            .collection("usuarios")
            .document(uid)
            .collection("carrito")
            .get()
            .addOnSuccessListener { snapshot ->
                val productos = snapshot.documents.mapNotNull {
                    it.toObject(ProductoCarrito::class.java)
                }
                _carrito.value = productos
            }
    }

    fun eliminarProducto(productoId: String) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == productoId }
        CarritoRepositorio.eliminarProductoDeFirestore(
            productoId = productoId,
            onSuccess = { Log.d("CarritoViewModel", "Producto eliminado de Firestore") },
            onError = { e -> Log.e("CarritoViewModel", "Error al eliminar: ${e.message}") }
        )
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }
}

