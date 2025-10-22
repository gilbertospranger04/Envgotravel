package com.korddy.envgotravel.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.korddy.envgotravel.ui.components.AlertBox
import com.korddy.envgotravel.ui.components.AlertType
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun Signup(
    navController: NavController,
    formattedPhone: String,
    viewModel: SignupViewModel = SignupViewModel()
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isDriver by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    var alertState by remember { mutableStateOf<Pair<AlertType, String>?>(null) }

    fun calculateAge(birthdate: String): Int? = try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = format.parse(birthdate)
        val diff = Date().time - (date?.time ?: 0)
        (diff / (1000L * 60 * 60 * 24 * 365)).toInt()
    } catch (e: Exception) { null }

    fun formatBirthdateInput(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length <= 2 -> digits
            digits.length <= 4 -> digits.substring(0, 2) + "/" + digits.substring(2)
            digits.length <= 8 -> digits.substring(0, 2) + "/" + digits.substring(2, 4) + "/" + digits.substring(4)
            else -> digits.substring(0, 2) + "/" + digits.substring(2, 4) + "/" + digits.substring(4, 8)
        }
    }

    val age = remember(birthdate) { calculateAge(birthdate) }

    EnvgotravelTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campos de texto
                val textFields = listOf(
                    Triple("Username", username) { value: String -> username = value },
                    Triple("Nome", firstName) { value: String -> firstName = value },
                    Triple("Sobrenome", lastName) { value: String -> lastName = value },
                    Triple("Email", email) { value: String -> email = value },
                    Triple("Data Nascimento", birthdate) { value: String -> birthdate = formatBirthdateInput(value) }
                )

                textFields.forEach { (label, value, onChange) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { onChange(it) },
                        label = { Text(label) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Idade (somente leitura)
                OutlinedTextField(
                    value = age?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Idade") },
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                val passwordFields = listOf(
                    Triple("Senha", password) { value: String -> password = value },
                    Triple("Confirmar Senha", confirmPassword) { value: String -> confirmPassword = value }
                )

                passwordFields.forEach { (label, value, onChange) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { onChange(it) },
                        label = { Text(label) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Toggle motorista
                Button(
                    onClick = { isDriver = !isDriver },
                    enabled = (age ?: 0) >= 21,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isDriver) "Motorista (Ativo)" else "Motorista (Inativo)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão cadastrar
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.uploadAndRegister(
                                phoneNumber = formattedPhone,
                                username = username,
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                birthdate = birthdate,
                                age = age,
                                password = password,
                                confirmPassword = confirmPassword,
                                isDriver = isDriver,
                                profilePictureUri = null,
                                onSuccess = {
                                    alertState = AlertType.SUCCESS to "Cadastro realizado com sucesso!"
                                    navController.navigate("signin")
                                },
                                onError = { msg -> alertState = AlertType.ERROR to msg }
                            )
                        }
                    },
                    enabled = username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) CircularProgressIndicator() else Text("Cadastrar")
                }
            }

            // AlertBox sobre a tela
            alertState?.let { (type, message) ->
                AlertBox(message = message, type = type, onDismiss = { alertState = null })
            }
        }
    }
}