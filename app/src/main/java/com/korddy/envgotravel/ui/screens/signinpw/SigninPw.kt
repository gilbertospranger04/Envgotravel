package com.korddy.envgotravel.ui.screens.signinpw

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.korddy.envgotravel.ui.components.ButtonLoading
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme

@Composable
fun SigninPw(
    navController: NavController,
    formattedPhone: String,
    viewModel: SigninPwViewModel = viewModel()
) {
    val context = LocalContext.current
    val password by viewModel.password
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val scrollState = rememberScrollState()

    EnvgotravelTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Entrar com senha",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Telefone: $formattedPhone",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Senha"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ButtonLoading(
                text = "Entrar",
                loading = isLoading,
                enabled = password.isNotBlank(),
                onClick = {
                    viewModel.signin(
                        phoneNumber = formattedPhone,
                        onSuccess = {
                            Toast.makeText(context, "Login realizado!", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") {
                                popUpTo("signinpw") { inclusive = true }
                            }
                        },
                        onFailure = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}