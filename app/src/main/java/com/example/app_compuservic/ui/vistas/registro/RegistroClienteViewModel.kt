package com.example.app_compuservic.ui.vistas.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.modelos.Usuario
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireBaseAuthStoreRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegistroClienteViewModel(
    val repositorio: FireBaseAuthStoreRepositorio = FireBaseAuthStoreRepositorio()
) : ViewModel() {
    var estadoUsario = MutableStateFlow<EstadoUsuario>(EstadoUsuario.vacio)
        private set
    var nombre = MutableStateFlow<String>("")
        private set
    var email = MutableStateFlow<String>("")
        private set
    var password = MutableStateFlow<String>("")
        private set
    var confirmarPassword = MutableStateFlow<String>("")
        private set
    var mostrarError = MutableStateFlow<Boolean>(false)
        private set


    fun cambiarMostrarError(valor: Boolean) {
        mostrarError.value = valor
    }
    fun cambiarNombre(nuevoNombre: String) {
        nombre.value = nuevoNombre
    }

    fun cambiarEmail(nuevoEmail: String) {
        email.value = nuevoEmail
    }

    fun cambiarPassword(nuevoPassword: String) {
        password.value = nuevoPassword
    }

    fun cambiarConfirmarPassword(nuevoConfirmarPassword: String) {
        confirmarPassword.value = nuevoConfirmarPassword
    }

    fun verificarPassword() : Boolean{
        return password.value.equals(confirmarPassword.value) && password.value.isNotEmpty()
    }

    fun crearUsuario(email: String, password: String, nuevoUsuario: Usuario) {

        viewModelScope.launch {
            estadoUsario.value = EstadoUsuario.cargando
            estadoUsario.value = repositorio.crearUsuario(email, password, nuevoUsuario)
        }

    }


}


