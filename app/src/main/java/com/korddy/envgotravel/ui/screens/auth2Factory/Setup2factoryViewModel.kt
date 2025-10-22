package com.korddy.envgotravel.ui.screens.auth2Factory

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korddy.envgotravel.services.api.ApiService
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.Setup2FAResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Setup2factoryViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _setupState = MutableStateFlow<Setup2FAState>(Setup2FAState.Idle)
    val setupState: StateFlow<Setup2FAState> = _setupState.asStateFlow()

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun setup2FA() {
        viewModelScope.launch {
            _setupState.value = Setup2FAState.Loading
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val response = api.setup2FA()
                if (response.isSuccessful && response.body()?.success == true) {
                    _setupState.value = Setup2FAState.Success(response.body()!!)
                } else {
                    val error = response.body()?.message ?: response.errorBody()?.string() ?: "Erro ao configurar 2FA"
                    _setupState.value = Setup2FAState.Error(error)
                    errorMessage.value = error
                }
            } catch (e: Exception) {
                val error = e.localizedMessage ?: "Erro de conexão"
                _setupState.value = Setup2FAState.Error(error)
                errorMessage.value = error
            } finally {
                isLoading.value = false
            }
        }
    }
}

sealed class Setup2FAState {
    object Idle : Setup2FAState()
    object Loading : Setup2FAState()
    data class Success(val data: Setup2FAResponse) : Setup2FAState()
    data class Error(val message: String) : Setup2FAState()
}