package com.korddy.envgotravel.ui.screens.auth2Factory

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
fun Setup2factory(
    navController: NavController,
    viewModel: Setup2factoryViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val setupState by viewModel.setupState.collectAsState()
    val errorMessage by viewModel.errorMessage
    val scope = rememberCoroutineScope()
    var qrBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var alertState by remember { mutableStateOf<Pair<AlertType, String>?>(null) }

    // Processamento do QR code
    LaunchedEffect(setupState) {
        when (setupState) {
            is Setup2FAState.Success -> {
                val data = (setupState as Setup2FAState.Success).data
                try {
                    val base64Data = data.qrCodeBase64.substringAfter("base64,")
                    val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                    qrBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    alertState = AlertType.SUCCESS to "QR Code gerado com sucesso!"
                } catch (e: Exception) {
                    viewModel.errorMessage.value = "Erro ao carregar QR code: ${e.message}"
                }
            }
            is Setup2FAState.Error -> {
                alertState = AlertType.ERROR to (setupState as Setup2FAState.Error).message
            }
            else -> {}
        }
    }

    // Inicializa setup se estiver idle
    LaunchedEffect(Unit) {
        if (setupState is Setup2FAState.Idle) viewModel.setup2FA()
    }

    EnvgotravelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "Ativar autenticação de dois fatores",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
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
                    when (setupState) {
                        is Setup2FAState.Loading -> {
                            CircularProgressIndicator(color = colorScheme.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Gerando QR Code...", color = colorScheme.onBackground)
                        }

                        is Setup2FAState.Success -> {
                            val data = (setupState as Setup2FAState.Success).data

                            Text(
                                "Escaneie o QR Code com o Google Authenticator",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            qrBitmap?.let { bitmap ->
                                Card(
                                    modifier = Modifier.size(240.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "QR Code para Google Authenticator",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Chave secreta:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = data.secretKey,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colorScheme.primary,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Botão para navegação à verificação 2FA
                            Button(
                                onClick = {
                                    navController.navigate("verify2fa")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorScheme.primary,
                                    contentColor = colorScheme.onPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text("Continuar para verificação", style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        else -> {}
                    }

                    errorMessage?.let { message ->
                        if (setupState !is Setup2FAState.Error) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = message,
                                color = colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

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