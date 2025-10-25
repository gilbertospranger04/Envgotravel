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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.ui.components.Picture
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(), // instantiate safely via viewModel()
    onBack: () -> Unit = { navController.navigateUp() }, // backward compat if NavHost passes it
    onSave: () -> Unit = {} // accept onSave if NavHost supplies it (no-op by default)
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
                        IconButton(onClick = onBack) {
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // centraliza a imagem via Box caso necessário
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Picture(
                value = user.profilePicture,
                size = 120.dp,
                // em profile a área de foto é apenas visual: não passamos onUpload/onDelete
            )
        }

        Text(
            text = (listOfNotNull(user.firstName, user.lastName).joinToString(" ").ifEmpty { user.username }),
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
        ProfileInfo("Nome de perfil (nickname)", user.nickname ?: "-", colors)
        ProfileInfo("Idade", user.age?.toString() ?: "-", colors)
        ProfileInfo("Data de Nascimento", user.birthdate ?: "-", colors)
        ProfileInfo("Motorista", if (user.isDriver) "Sim" else "Não", colors)
        ProfileInfo("Disponível", if (user.isAvailable) "Sim" else "Não", colors)
        ProfileInfo("Saldo na Carteira", String.format("Kz %.2f", user.walletBalance ?: 0.0), colors)
        ProfileInfo("2FA Ativado", if (user.twoFactorEnabled) "Sim" else "Não", colors)
        ProfileInfo("Criado em", user.createdAt ?: "-", colors)
        ProfileInfo("Atualizado em", user.updatedAt ?: "-", colors)
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