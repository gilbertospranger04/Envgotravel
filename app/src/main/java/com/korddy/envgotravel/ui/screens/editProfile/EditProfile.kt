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
import com.korddy.envgotravel.ui.components.*
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    navController: NavController,
    viewModel: EditProfileViewModel = EditProfileViewModel()
) {
    val darkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    val user by viewModel.user
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val scrollState = rememberScrollState()

    // Estados dos campos
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var walletBalance by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.fetchUser() }

    // Atualiza campos quando o user muda
    LaunchedEffect(user) {
        user?.let {
            username = it.username
            firstName = it.firstName.orEmpty()
            lastName = it.lastName.orEmpty()
            email = it.email.orEmpty()
            age = it.age?.toString().orEmpty()
            birthdate = it.birthdate.orEmpty()
            walletBalance = it.walletBalance?.toString().orEmpty()
        }
    }

    EnvgotravelTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar Perfil", color = colors.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = colors.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background
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
                when {
                    isLoading -> CircularProgressIndicator(color = colors.primary)
                    else -> {
                        if (!errorMessage.isNullOrEmpty())
                            Text(errorMessage!!, color = colors.error)

                        Picture(
                            value = user?.profilePicture,
                            size = 120.dp,
                            centered = true,
                            onUpload = { file: File ->
                                viewModel.uploadAvatar(
                                    file,
                                    onSuccess = { updated -> viewModel.user.value = updated },
                                    onFailure = { Log.e("EditProfile", it) }
                                )
                            },
                            onDelete = {
                                viewModel.deleteAvatar(
                                    onSuccess = {
                                        viewModel.user.value =
                                            viewModel.user.value?.copy(profilePicture = null)
                                    },
                                    onFailure = { Log.e("EditProfile", it) }
                                )
                            }
                        )

                        Input(username, { username = it }, "Username")
                        Input(firstName, { firstName = it }, "Nome")
                        Input(lastName, { lastName = it }, "Sobrenome")
                        Input(email, { email = it }, "Email")
                        Input(age, { if (it.all(Char::isDigit)) age = it }, "Idade")
                        Input(birthdate, { birthdate = it }, "Nascimento (YYYY-MM-DD)")
                        Input(
                            walletBalance,
                            { if (it.matches(Regex("^\\d*\\.?\\d*$"))) walletBalance = it },
                            "Saldo da Carteira"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ButtonLoading(
                            text = "Salvar Alterações",
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
                                    onSuccess = { navController.popBackStack() },
                                    onFailure = { Log.e("EditProfile", it) }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}