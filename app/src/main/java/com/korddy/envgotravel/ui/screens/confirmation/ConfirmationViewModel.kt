package com.korddy.envgotravel.ui.screens.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.korddy.envgotravel.services.api.ApiService
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.VerifyOtpRequest
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    /**
     * Função principal para confirmar OTP
     */
    fun confirmOtp(
        formattedPhone: String,
        otp: String,
        onExists: () -> Unit,
        onNotExists: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                // 🔹 Verifica OTP
                val verifyResponse = api.verifyOtp(
                    VerifyOtpRequest(
                        phoneNumber = formattedPhone,
                        otp = otp
                    )
                )

                if (verifyResponse.isSuccessful) {
                    // 🔹 Checa se o telefone existe no DB
                    val checkResponse = api.checkPhone(formattedPhone)
                    isLoading.value = false

                    if (checkResponse.isSuccessful) {
                        val exists = checkResponse.body()?.exists ?: false
                        if (exists) onExists() else onNotExists()
                    } else {
                        onError("Erro checkPhone: ${checkResponse.message()}")
                    }

                } else {
                    isLoading.value = false
                    val errorMsg = verifyResponse.errorBody()?.string() ?: "OTP inválido ou expirado"
                    onError(errorMsg)
                }

            } catch (e: Exception) {
                isLoading.value = false
                onError(e.localizedMessage ?: "Erro desconhecido")
            }
        }
    }
}