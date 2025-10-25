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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: EditProfileViewModel = viewModel(), // safe instantiation
    onBack: () -> Unit = { navController.navigateUp() }, // backward compatible
    onSave: () -> Unit = {}
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
    var phoneNumber by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var walletBalance by remember { mutableStateOf("") }
    var twoFactorEnabled by remember { mutableStateOf(false) }
    var isDriver by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.fetchUser() }

    LaunchedEffect(user) {
        user?.let {
            username = it.username
            firstName = it.firstName.orEmpty()
            lastName = it.lastName.orEmpty()
            email = it.email.orEmpty()
            phoneNumber = it.phoneNumber.orEmpty()
            nickname = it.nickname.orEmpty()
            age = it.age?.toString().orEmpty()
            birthdate = it.birthdate.orEmpty()
            walletBalance = it.walletBalance?.toString().orEmpty()
            twoFactorEnabled = it.twoFactorEnabled
            isDriver = it.isDriver
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
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colors.primary)
                        }
                    }
                    else -> {
                        if (!errorMessage.isNullOrEmpty())
                            Text(errorMessage!!, color = colors.error)

                        // Foto (apenas visual na profile, mas aqui permitimos upload/delete)
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Picture(
                                value = user?.profilePicture,
                                size = 120.dp,
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
                        }

                        // Inputs
                        Input(username, { username = it }, "Username")
                        Input(firstName, { firstName = it }, "Nome")
                        Input(lastName, { lastName = it }, "Sobrenome")
                        Input(email, { email = it }, "Email")
                        Input(phoneNumber, { phoneNumber = it }, "Telefone")
                        Input(nickname, { nickname = it }, "Nickname")
                        Input(age, { if (it.all(Char::isDigit)) age = it }, "Idade")
                        Input(birthdate, { birthdate = it }, "Data de Nascimento (YYYY-MM-DD)")
                        Input(
                            walletBalance,
                            { if (it.matches(Regex("^\\d*\\.?\\d*$"))) walletBalance = it },
                            "Saldo da Carteira"
                        )

                        // toggles simples
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Motorista", color = colors.onBackground, modifier = Modifier.alignByBaseline())
                            Switch(checked = isDriver, onCheckedChange = { isDriver = it })
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("2FA Ativado", color = colors.onBackground, modifier = Modifier.alignByBaseline())
                            Switch(checked = twoFactorEnabled, onCheckedChange = { twoFactorEnabled = it })
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        ButtonLoading(
                            text = "Salvar Alterações",
                            loading = isLoading,
                            enabled = true,
                            onClick = {
                                val updated = UserUpdateRequest(
                                    username = username.ifBlank { null },
                                    firstName = firstName.ifBlank { null },
                                    lastName = lastName.ifBlank { null },
                                    email = email.ifBlank { null },
                                    phoneNumber = phoneNumber.ifBlank { null },
                                    age = age.toIntOrNull(),
                                    birthdate = birthdate.ifBlank { null },
                                    isDriver = isDriver,
                                    profilePicture = user?.profilePicture,
                                    walletBalance = walletBalance.toDoubleOrNull()
                                )

                                // Passa userId e phoneNumber opcionalmente (compatibilidade)
                                viewModel.updateUser(
                                    request = updated,
                                    userId = user?.id,
                                    phoneNumber = user?.phoneNumber,
                                    onSuccess = {
                                        onSave()
                                        navController.popBackStack()
                                    },
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