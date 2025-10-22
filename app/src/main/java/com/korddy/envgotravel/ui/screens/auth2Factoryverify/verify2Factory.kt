package com.korddy.envgotravel.ui.screens.auth2Factoryverify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.korddy.envgotravel.ui.components.AlertBox
import com.korddy.envgotravel.ui.components.AlertType
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify2factory(
    navController: NavController,
    viewModel: Verify2factoryViewModel = viewModel()
) {
    var otpCode by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val verifyState by viewModel.verifyState.collectAsState()
    val errorMessage by viewModel.errorMessage

    // Estado do alerta
    var alertState by remember { mutableStateOf<Pair<AlertType, String>?>(null) }

    // Navegação e alertas
    LaunchedEffect(verifyState) {
        when (verifyState) {
            is Verify2FAState.Success -> {
                alertState = AlertType.SUCCESS to "2FA ativado com sucesso!"
                // Navegação para home após sucesso
                navController.navigate("home") {
                    popUpTo("setup2fa") { inclusive = true }
                }
            }
            is Verify2FAState.Error -> {
                alertState = AlertType.ERROR to (verifyState as Verify2FAState.Error).message
            }
            else -> {}
        }
    }

    EnvgotravelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Verificar Código 2FA", style = MaterialTheme.typography.titleMedium) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = colorScheme.background,
                            titleContentColor = colorScheme.onBackground
                        )
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.background)
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Digite o código de 6 dígitos do Google Authenticator",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    TextField(
                        value = otpCode,
                        onValueChange = { newValue ->
                            if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                                otpCode = newValue
                                if (errorMessage != null) viewModel.errorMessage.value = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "000000",
                                color = colorScheme.outline,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            textAlign = TextAlign.Center,
                            color = colorScheme.onBackground
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface,
                            cursorColor = colorScheme.primary,
                            focusedIndicatorColor = colorScheme.primary,
                            unfocusedIndicatorColor = colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botão de verificação 2FA
                    Button(
                        onClick = { scope.launch { viewModel.verify2FASetup(otpCode) } },
                        enabled = otpCode.length == 6,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        if (verifyState is Verify2FAState.Loading) {
                            CircularProgressIndicator(
                                color = colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Verificar e Ativar", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão para voltar para configuração
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Voltar para configuração")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // NOVO: Botão para navegar direto para home
                    Button(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("setup2fa") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary,
                            contentColor = colorScheme.onSecondary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Ir para Home", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // AlertBox sobre a tela
            alertState?.let { (type, message) ->
                AlertBox(
                    message = message,
                    type = type,
                    onDismiss = { alertState = null }
                )
            }
        }
    }
}