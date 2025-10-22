package com.korddy.envgotravel.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.services.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    val user = mutableStateOf<User?>(null)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun fetchUser() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getCurrentUser()
                if (response.isSuccessful) {
                    user.value = response.body()
                } else {
                    errorMessage.value = "Erro ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erro inesperado"
            } finally {
                isLoading.value = false
            }
        }
    }
}
