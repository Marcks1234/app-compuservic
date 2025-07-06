package com.example.app_compuservic.ui.vistas.usuario.orden

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.app_compuservic.modelos.Orden
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class OrdenViewModel : ViewModel() {
    private val _orden = MutableStateFlow<Orden?>(null)
    val orden: StateFlow<Orden?> get() = _orden


    fun cargarOrdenPorId(ordenId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(uid)
            .collection("ordenes")
            .document(ordenId)
            .get()
            .addOnSuccessListener { doc ->
                val orden = doc.toObject(Orden::class.java)?.copy(id = ordenId)
                _orden.value = orden
            }
    }
}

