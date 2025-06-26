package com.example.app_compuservic.ui.vistas.administrador.categoria
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.app_compuservic.modelos.Categoria
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CategoriaViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()


    private val _nombreCategoria = MutableStateFlow("")
    val nombreCategoria: StateFlow<String> = _nombreCategoria

    private val _imagenUrl = MutableStateFlow("https://cdn-icons-png.flaticon.com/512/1828/1828884.png")
    val imagenUrl: StateFlow<String> = _imagenUrl

    private val _listaCategorias = MutableStateFlow<List<Categoria>>(emptyList())
    val listaCategorias: StateFlow<List<Categoria>> = _listaCategorias
    init {
        obtenerCategoriasDesdeFirestore()
    }

    // 🔹 Esto agrega una categoría a Firestore (agregamos id)
    fun agregarCategoria() {
        if (_nombreCategoria.value.isNotBlank()) {
            viewModelScope.launch {
                try {
                    // Agregar la categoría sin necesidad de asignar un ID manualmente
                    val categoriaRef = db.collection("categorias").add(
                        hashMapOf(
                            "nombre" to _nombreCategoria.value,
                            "descripcion" to "Descripción local",
                            "imagenRes" to _imagenUrl.value
                        )
                    ).await() // Espera hasta que se haya agregado el documento

                    // Puedes obtener el ID de la categoría generada automáticamente
                    val idGenerado = categoriaRef.id

                    // Ahora actualizamos el documento agregando el campo 'id' con el valor del ID generado
                    categoriaRef.update("id", idGenerado)
                        .await() // Se agrega el campo "id" al documento

                    Log.d("CategoriaViewModel", "Categoría agregada con ID: $idGenerado")

                    // Aquí puedes hacer algo con el ID si lo necesitas, como actualizar alguna variable
                    // o mostrar algún mensaje en la UI
                } catch (e: Exception) {
                    Log.e("CategoriaViewModel", "Error al agregar categoría: ${e.message}")
                }
            }
        }
    }


    // 🔹 Esto es para mostrar desde Firestore al cargar
    private fun obtenerCategoriasDesdeFirestore() {
        db.collection("categorias")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("Firestore", "Error al leer categorías", error)
                    return@addSnapshotListener
                }

                val lista = snapshots?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Categoria(
                        id = data["id"] as? String ?: doc.id,
                        nombre = data["nombre"] as? String ?: "",
                        descripcion = data["descripcion"] as? String ?: "",
                        imagenRes = data["imagenRes"] as? String ?: ""
                    )
                } ?: emptyList()

                _listaCategorias.value = lista
            }
    }

    fun cambiarNombre(nuevo: String) {
        _nombreCategoria.value = nuevo
    }

    fun cambiarImagen(nuevaUrl: String) {
        _imagenUrl.value = nuevaUrl
    }

    fun eliminarCategoria(categoria: Categoria) {
        _listaCategorias.value = _listaCategorias.value - categoria
    }

    fun editarCategoria(categoria: Categoria) {
        _nombreCategoria.value = categoria.nombre
        _imagenUrl.value = categoria.imagenRes?.toString() ?: ""
    }
}