package com.korddy.envgotravel.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    val user by profileViewModel.user
    val isLoading by profileViewModel.isLoading
    val errorMessage by profileViewModel.errorMessage

    LaunchedEffect(Unit) { profileViewModel.fetchUser() }

    EnvgotravelTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Perfil", color = colors.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = colors.onBackground)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("editprofile") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Perfil", tint = colors.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background,
                        titleContentColor = colors.onBackground
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
                    !errorMessage.isNullOrEmpty() -> Text(
                        errorMessage ?: "Erro desconhecido",
                        color = colors.error
                    )
                    user != null -> ProfileContent(user!!, colors)
                    else -> Text(
                        "Usuário não encontrado",
                        color = colors.onBackground
                    )
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
        // Foto de perfil
        Picture(
            value = user.profilePicture,
            size = 120.dp,
        )

        // Nome + username
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

        // Informações
        ProfileInfoRow("Email", user.email ?: "-", colors)
        ProfileInfoRow("Telefone", user.phoneNumber ?: "-", colors)
        ProfileInfoRow("Idade", user.age?.toString() ?: "-", colors)
        ProfileInfoRow("Data de Nascimento", user.birthdate ?: "-", colors)
        ProfileInfoRow("Motorista", if (user.isDriver) "Sim" else "Não", colors)
        ProfileInfoRow("Saldo na Carteira", "Kz ${user.walletBalance ?: 0.0}", colors)
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String, colors: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )
    }
}