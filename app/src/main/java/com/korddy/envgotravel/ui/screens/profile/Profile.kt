package com.korddy.envgotravel.ui.screens.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.ui.components.Picture
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val darkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    val user by viewModel.user
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    LaunchedEffect(Unit) { viewModel.fetchUser() }

    EnvgotravelTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Perfil", color = colors.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = colors.onBackground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("editprofile") }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Editar Perfil",
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> CircularProgressIndicator(color = colors.primary)
                    !errorMessage.isNullOrEmpty() -> Text(errorMessage!!, color = colors.error)
                    user != null -> ProfileContent(user!!, colors)
                    else -> Text("Usuário não encontrado", color = colors.onBackground)
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(user: User, colors: ColorScheme) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Picture(
            value = user.profilePicture,
            size = 120.dp,
            onUpload = null, // Removido parâmetro centered
            onDelete = null
        )

        Text(
            text = "${user.firstName ?: ""} ${user.lastName ?: ""}".trim().ifEmpty { user.username },
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.onBackground
        )
        Text(
            text = "@${user.username}",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfo("Email", user.email ?: "-", colors)
        ProfileInfo("Telefone", user.phoneNumber ?: "-", colors)
        ProfileInfo("Idade", user.age?.toString() ?: "-", colors)
        ProfileInfo("Nascimento", user.birthdate ?: "-", colors)
        ProfileInfo("Motorista", if (user.isDriver) "Sim" else "Não", colors)
        ProfileInfo("Saldo", "Kz ${user.walletBalance ?: 0.0}", colors)
    }
}

@Composable
private fun ProfileInfo(label: String, value: String, colors: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = colors.onBackground)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = colors.onBackground)
    }
}