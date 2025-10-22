package com.korddy.envgotravel.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.korddy.envgotravel.services.api.ApiService
import com.korddy.envgotravel.services.api.SendOtpRequest
import kotlinx.coroutines.launch

class SigninViewModel(
    private val api: ApiService
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    // =====================
    // Envia OTP + checa telefone no backend
    // =====================
    fun sendOtpAndCheckPhone(formattedPhone: String, onResult: (Boolean, String?) -> Unit) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val otpResponse = api.sendOtp(SendOtpRequest(formattedPhone))
                if (!otpResponse.isSuccessful) {
                    isLoading.value = false
                    onResult(false, "Erro sendOtp: ${otpResponse.message()}")
                    return@launch
                }

                val checkResponse = api.checkPhone(formattedPhone)
                if (!checkResponse.isSuccessful) {
                    isLoading.value = false
                    onResult(false, "Erro checkPhone: ${checkResponse.message()}")
                    return@launch
                }

                isLoading.value = false
                onResult(true, null)

            } catch (e: Exception) {
                isLoading.value = false
                onResult(false, e.localizedMessage)
            }
        }
    }
}
