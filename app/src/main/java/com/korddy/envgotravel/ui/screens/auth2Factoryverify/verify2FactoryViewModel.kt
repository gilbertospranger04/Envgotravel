package com.korddy.envgotravel.ui.screens.auth2Factoryverify

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korddy.envgotravel.services.api.ApiService
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.Verify2FASetupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Verify2factoryViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _verifyState = MutableStateFlow<Verify2FAState>(Verify2FAState.Idle)
    val verifyState: StateFlow<Verify2FAState> = _verifyState.asStateFlow()

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun verify2FASetup(otpCode: String) {
        viewModelScope.launch {
            _verifyState.value = Verify2FAState.Loading
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val response = api.verify2FASetup(Verify2FASetupRequest(code = otpCode))
                if (response.isSuccessful && response.body()?.success == true) {
                    _verifyState.value = Verify2FAState.Success(response.body()!!)
                } else {
                    val error = response.body()?.message ?: response.errorBody()?.string() ?: "Falha na verificação do código"
                    _verifyState.value = Verify2FAState.Error(error)
                    errorMessage.value = error
                }
            } catch (e: Exception) {
                val error = e.localizedMessage ?: "Erro de conexão"
                _verifyState.value = Verify2FAState.Error(error)
                errorMessage.value = error
            } finally {
                isLoading.value = false
            }
        }
    }
}

sealed class Verify2FAState {
    object Idle : Verify2FAState()
    object Loading : Verify2FAState()
    data class Success(val data: com.korddy.envgotravel.services.api.Verify2FASetupResponse) : Verify2FAState()
    data class Error(val message: String) : Verify2FAState()
}