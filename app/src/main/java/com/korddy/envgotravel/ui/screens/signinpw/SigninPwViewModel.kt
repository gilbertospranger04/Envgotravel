package com.korddy.envgotravel.ui.screens.signinpw

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.SigninRequest
import com.korddy.envgotravel.services.session.SessionCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninPwViewModel : ViewModel() {

    val password = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun signin(
        phoneNumber: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // 🔹 Tipos explícitos ajudam o compilador Kotlin
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true

                val request = SigninRequest(
                    phoneNumber = phoneNumber,
                    password = password.value
                )

                val response = RetrofitClient.api.signin(request)

                withContext(Dispatchers.Main) {
                    isLoading.value = false

                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body?.token.isNullOrBlank()) {
                            onFailure.invoke("Resposta inválida do servidor")
                            return@withContext
                        }

                        // 🔹 Guarda o token com expiração de 2 dias
                        SessionCache.setAuthToken(body!!.token)

                        // 🔹 Guarda o usuário, se vier no body
                        body.user?.let { user ->
                            val userJson = Gson().toJson(user)
                            SessionCache.saveCurrentUser(userJson)
                        }

                        onSuccess.invoke()
                    } else {
                        onFailure.invoke("Erro ${response.code()}")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    onFailure.invoke(e.message ?: "Erro desconhecido")
                }
            }
        }
    }
}