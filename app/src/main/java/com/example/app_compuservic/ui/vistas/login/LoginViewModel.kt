package com.example.app_compuservic.ui.vistas.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_compuservic.ui.estados.EstadoUsuario
import com.example.app_compuservic.repositorios.FireBaseAuthRepositorio
import com.example.app_compuservic.repositorios.FireStoreRepositorio
import com.example.app_compuservic.ui.estados.EstadoLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val auth: FireBaseAuthRepositorio = FireBaseAuthRepositorio(),
    private val db: FireStoreRepositorio = FireStoreRepositorio()
) :
    ViewModel() {

    var estadoLogin = MutableStateFlow<EstadoLogin>(EstadoLogin.Vacio)
        private set


    fun loginUsuario(email: String, password: String) {
        viewModelScope.launch {
            estadoLogin.value = EstadoLogin.Cargando
            try {
                val resultado: EstadoUsuario = auth.loginUsuario(email, password)
                if (resultado == EstadoUsuario.Exito) {
                    val uid = auth.uidCuentaActual()
                    if (uid != "") {
                        val tipoUsuario = db.obtenerTipoUsuario(uid)
                        estadoLogin.value = EstadoLogin.Exito(tipoUsuario)
                    } else {
                        estadoLogin.value = EstadoLogin.Error("No se pudo obtener UID.")
                    }
                } else if (resultado is EstadoUsuario.Error) {
                    estadoLogin.value = EstadoLogin.Error(resultado.mensaje)
                } else {
                    estadoLogin.value =
                        EstadoLogin.Error("Credenciales incorrectas o error inesperado.")
                }
            } catch (e: Exception) {
                estadoLogin.value = EstadoLogin.Error(e.message ?: "Error desconocido")
            }
        }
    }

}