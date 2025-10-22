package com.korddy.envgotravel.ui.screens.signup

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.korddy.envgotravel.services.api.RetrofitClient
import com.korddy.envgotravel.services.api.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class SignupViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun uploadAndRegister(
        phoneNumber: String,
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        birthdate: String,
        age: Int?,
        password: String,
        confirmPassword: String,
        isDriver: Boolean,
        profilePictureUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (password != confirmPassword) {
            errorMessage.value = "Senhas não coincidem"
            onError("Senhas não coincidem")
            return
        }

        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                var profilePictureUrl: String? = null
                profilePictureUri?.let { uri ->
                    val ref = Firebase.storage.reference.child("profile_picture/${UUID.randomUUID()}")
                    ref.putFile(uri).await()
                    profilePictureUrl = ref.downloadUrl.await().toString()
                }

                val request = SignupRequest(
                    username = username,
                    password = password,
                    confirmPassword = confirmPassword,
                    email = email,
                    phoneNumber = phoneNumber,
                    firstName = firstName,
                    lastName = lastName,
                    age = age,
                    birthdate = birthdate,
                    isDriver = isDriver,
                    profilePicture = profilePictureUrl
                )

                val response = RetrofitClient.api.signup(request)
                isLoading.value = false

                if (response.isSuccessful) onSuccess()
                else {
                    val msg = "Erro: ${response.code()}"
                    errorMessage.value = msg
                    onError(msg)
                }

            } catch (e: Exception) {
                isLoading.value = false
                val msg = e.message ?: "Erro desconhecido"
                errorMessage.value = msg
                onError(msg)
            }
        }
    }
}
