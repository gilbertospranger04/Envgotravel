package com.korddy.envgotravel.ui.screens.editProfile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.UserUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditProfileViewModel : ViewModel() {

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
                errorMessage.value = e.message ?: "Erro desconhecido"
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Atualiza usuário.
     * userId e phoneNumber são opcionais para compatibilidade com chamadas antigas.
     */
    fun updateUser(
        request: UserUpdateRequest,
        userId: String? = null,
        phoneNumber: String? = null,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.updateMyProfile(request)
                if (response.isSuccessful) {
                    user.value = response.body()
                    onSuccess()
                } else {
                    onFailure("Erro ${response.code()}")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Erro desconhecido")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun uploadAvatar(file: File, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val body = MultipartBody.Part.createFormData(
            "avatar",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.uploadAvatar(body)
                if (response.isSuccessful) {
                    response.body()?.let {
                        user.value = it
                        onSuccess(it)
                    } ?: onFailure("Resposta vazia")
                } else {
                    onFailure("Erro ${response.code()}")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Erro desconhecido")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun deleteAvatar(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val uid = user.value?.id ?: run {
            onFailure("User id inexistente")
            return
        }
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.deleteAvatar(uid)
                if (response.isSuccessful) {
                    // apenas remove a url localmente; o backend já removeu
                    user.value = user.value?.copy(profilePicture = null)
                    onSuccess()
                } else {
                    onFailure("Erro ${response.code()}")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Erro desconhecido")
            } finally {
                isLoading.value = false
            }
        }
    }
}