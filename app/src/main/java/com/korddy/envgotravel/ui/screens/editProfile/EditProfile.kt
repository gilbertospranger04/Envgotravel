package com.korddy.envgotravel.ui.screens.editProfile

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.korddy.envgotravel.services.api.UserUpdateRequest
import com.korddy.envgotravel.ui.components.ButtonLoading
import com.korddy.envgotravel.ui.components.Input
import com.korddy.envgotravel.ui.components.Picture
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    navController: NavController,
    viewModel: EditProfileViewModel = EditProfileViewModel(),
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    val user by viewModel.user
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val scrollState = rememberScrollState()

    // Campos editáveis
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var walletBalance by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.fetchUser() }

    LaunchedEffect(user) {
        user?.let {
            username = it.username
            firstName = it.firstName ?: ""
            lastName = it.lastName ?: ""
            email = it.email ?: ""
            age = it.age?.toString() ?: ""
            birthdate = it.birthdate ?: ""
            walletBalance = it.walletBalance?.toString() ?: ""
        }
    }

    EnvgotravelTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar Perfil", color = colors.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = colors.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background,
                        titleContentColor = colors.onBackground
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = colors.primary)
                } else {
                    if (!errorMessage.isNullOrEmpty()) {
                        Text(errorMessage ?: "", color = colors.error)
                    }

                    // Foto de perfil centralizada
                    Picture(
                        value = user?.profilePicture,
                        size = 120.dp,
                        centered = true,
                        onUpload = { file: File ->
                            viewModel.uploadAvatar(
                                file,
                                onSuccess = { updatedUser ->
                                    viewModel.user.value = updatedUser
                                },
                                onFailure = { error ->
                                    Log.e("EditProfile", "Erro ao enviar avatar: $error")
                                }
                            )
                        },
                        onDelete = {
                            viewModel.deleteAvatar(
                                onSuccess = {
                                    viewModel.user.value =
                                        viewModel.user.value?.copy(profilePicture = null)
                                },
                                onFailure = { error ->
                                    Log.e("EditProfile", "Erro ao deletar avatar: $error")
                                }
                            )
                        }
                    )

                    // Inputs
                    Input(username, { username = it }, "Username")
                    Input(firstName, { firstName = it }, "Nome")
                    Input(lastName, { lastName = it }, "Sobrenome")
                    Input(email, { email = it }, "Email")
                    Input(age, { if (it.all { c -> c.isDigit() }) age = it }, "Idade")
                    Input(birthdate, { birthdate = it }, "Data de Nascimento (YYYY-MM-DD)")
                    Input(
                        walletBalance,
                        { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) walletBalance = it },
                        "Saldo da Carteira"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Um único botão de salvar
                    ButtonLoading(
                        text = "Salvar",
                        loading = isLoading,
                        enabled = true,
                        onClick = {
                            val updated = UserUpdateRequest(
                                username = username,
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                age = age.toIntOrNull(),
                                walletBalance = walletBalance.toDoubleOrNull(),
                                birthdate = birthdate
                            )
                            viewModel.updateUser(
                                updated,
                                onSuccess = { onSave() },
                                onFailure = { Log.e("EditProfile", "Erro ao atualizar: $it") }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}