package com.example.app_compuservic.ui.vistas.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.modelos.EstadoUsuario
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val repositorio: FireBaseAuthRepositorio = FireBaseAuthRepositorio()) :
    ViewModel() {


    var estadoUsuario = MutableStateFlow<EstadoUsuario>(EstadoUsuario.vacio)
        private set
    var email = MutableStateFlow<String>("")
        private set
    var password = MutableStateFlow<String>("")
        private set

    fun agregarEmail(nuevoEmail: String) { email.value = nuevoEmail}
    fun agregarPassword(nuevoPassword: String) { password.value = nuevoPassword}

    fun loginUsuario(email: String, password: String) {
        viewModelScope.launch {
            estadoUsuario.value = EstadoUsuario.cargando
            estadoUsuario.value = repositorio.loginUsuario(email, password)
        }
    }

}