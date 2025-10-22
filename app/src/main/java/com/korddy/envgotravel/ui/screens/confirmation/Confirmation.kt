package com.korddy.envgotravel.ui.screens.confirmation

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.korddy.envgotravel.ui.components.AlertBox
import com.korddy.envgotravel.ui.components.AlertType
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import kotlinx.coroutines.launch

@Composable
fun Confirmation(
    navController: NavController,
    formattedPhone: String,
    viewModel: ConfirmationViewModel = viewModel()
) {
    val context = LocalContext.current as Activity
    var otpCode by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val scope = rememberCoroutineScope()

    // Estado do alerta
    var alertState by remember { mutableStateOf<Pair<AlertType, String>?>(null) }

    // Abre teclado automaticamente
    LaunchedEffect(Unit) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    EnvgotravelTheme {
        val colorScheme = MaterialTheme.colorScheme

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Texto explicativo
                Text(
                    text = "Digite o código de 6 dígitos enviado para $formattedPhone",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo OTP
                TextField(
                    value = otpCode,
                    onValueChange = { newValue ->
                        if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                            otpCode = newValue
                            if (!errorMessage.isNullOrEmpty()) viewModel.errorMessage.value = null
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
                        disabledContainerColor = colorScheme.surface,
                        cursorColor = colorScheme.primary,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(colorScheme.surface, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão de confirmação OTP
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.confirmOtp(
                                formattedPhone = formattedPhone,
                                otp = otpCode,
                                onExists = {
                                    alertState = AlertType.SUCCESS to "OTP verificado com sucesso!"
                                    navController.navigate("signinpw/$formattedPhone")
                                },
                                onNotExists = {
                                    alertState = AlertType.SUCCESS to "OTP verificado com sucesso!"
                                    navController.navigate("signup/$formattedPhone")
                                },
                                onError = { msg ->
                                    alertState = AlertType.ERROR to "Erro: $msg"
                                }
                            )
                        }
                    },
                    enabled = otpCode.length == 6 && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Confirmar", style = MaterialTheme.typography.bodyLarge)
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