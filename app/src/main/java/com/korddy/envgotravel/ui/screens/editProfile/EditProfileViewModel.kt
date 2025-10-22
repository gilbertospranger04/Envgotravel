package com.korddy.envgotravel.ui.screens.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.UserUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditProfileViewModel : ViewModel() {

    val user = androidx.compose.runtime.mutableStateOf<User?>(null)
    val isLoading = androidx.compose.runtime.mutableStateOf(false)
    val errorMessage = androidx.compose.runtime.mutableStateOf<String?>(null)

    fun fetchUser() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<User> = RetrofitClient.api.getCurrentUser()
                if (response.isSuccessful) user.value = response.body() else errorMessage.value = "Erro ${response.code()}"
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erro desconhecido"
            } finally { isLoading.value = false }
        }
    }

    fun updateUser(updatedUser: UserUpdateRequest, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<User> = RetrofitClient.api.updateMyProfile(updatedUser)
                if (response.isSuccessful) { user.value = response.body(); onSuccess() } else onFailure("Erro ${response.code()}")
            } catch (e: Exception) { onFailure(e.message ?: "Erro desconhecido") } finally { isLoading.value = false }
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
                if (response.isSuccessful) { user.value = response.body(); onSuccess(response.body()!!) } else onFailure("Erro ${response.code()}")
            } catch (e: Exception) { onFailure(e.message ?: "Erro desconhecido") } finally { isLoading.value = false }
        }
    }

    fun updateAvatar(userId: String, file: File, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val body = MultipartBody.Part.createFormData(
            "avatar",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.updateAvatar(userId, body)
                if (response.isSuccessful) { user.value = response.body(); onSuccess(response.body()!!) } else onFailure("Erro ${response.code()}")
            } catch (e: Exception) { onFailure(e.message ?: "Erro desconhecido") } finally { isLoading.value = false }
        }
    }

    fun deleteAvatar(userId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.deleteAvatar(userId)
                if (response.isSuccessful) onSuccess() else onFailure("Erro ${response.code()}")
            } catch (e: Exception) { onFailure(e.message ?: "Erro desconhecido") } finally { isLoading.value = false }
        }
    }
}