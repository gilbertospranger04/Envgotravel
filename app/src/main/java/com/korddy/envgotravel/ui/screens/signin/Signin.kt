package com.korddy.envgotravel.ui.screens.signin  
  
import android.app.Activity  
import android.content.Intent  
import android.net.Uri  
import androidx.compose.foundation.Image  
import androidx.compose.foundation.clickable  
import androidx.compose.foundation.layout.*  
import androidx.compose.foundation.text.KeyboardOptions  
import androidx.compose.material3.*  
import androidx.compose.runtime.*  
import androidx.compose.ui.Alignment  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.platform.LocalContext  
import androidx.compose.ui.text.input.KeyboardType  
import androidx.compose.ui.unit.dp  
import androidx.lifecycle.viewmodel.compose.viewModel  
import androidx.navigation.NavController  
import coil.compose.rememberAsyncImagePainter  
import com.korddy.envgotravel.ui.components.ButtonLoading  
import com.korddy.envgotravel.ui.theme.EnvgotravelTheme  
import com.korddy.envgotravel.utils.PhoneNumberUtils  
import androidx.compose.material3.MaterialTheme  
import androidx.compose.foundation.background 
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight 
  
  
// =====================  
// Lista de países  
// =====================  
data class Country(  
    val name: String,  
    val code: String,  
    val flagUrl: String  
)  
  
val countries = listOf(  
    Country("Angola", "+244", "https://flagcdn.com/w20/ao.png"),  
    Country("Brasil", "+55", "https://flagcdn.com/w20/br.png"),  
    Country("Portugal", "+351", "https://flagcdn.com/w20/pt.png"),  
    Country("Moçambique", "+258", "https://flagcdn.com/w20/mz.png"),  
    Country("Cabo Verde", "+238", "https://flagcdn.com/w20/cv.png"),  
    Country("Guiné-Bissau", "+245", "https://flagcdn.com/w20/gw.png"),  
    Country("São Tomé e Príncipe", "+239", "https://flagcdn.com/w20/st.png"),  
    Country("Timor-Leste", "+670", "https://flagcdn.com/w20/tl.png"),  
    Country("Guiné Equatorial", "+240", "https://flagcdn.com/w20/gq.png"),  
    Country("Estados Unidos", "+1", "https://flagcdn.com/w20/us.png"),  
    Country("Reino Unido", "+44", "https://flagcdn.com/w20/gb.png"),  
    Country("França", "+33", "https://flagcdn.com/w20/fr.png"),  
    Country("Alemanha", "+49", "https://flagcdn.com/w20/de.png"),  
    Country("Espanha", "+34", "https://flagcdn.com/w20/es.png"),  
    Country("Itália", "+39", "https://flagcdn.com/w20/it.png"),  
    Country("Canadá", "+1", "https://flagcdn.com/w20/ca.png"),  
    Country("África do Sul", "+27", "https://flagcdn.com/w20/za.png"),  
    Country("Índia", "+91", "https://flagcdn.com/w20/in.png"),  
    Country("China", "+86", "https://flagcdn.com/w20/cn.png"),  
    Country("Japão", "+81", "https://flagcdn.com/w20/jp.png")  
)  
  
// =====================  
// Composable Signin  
// =====================  
@Composable  
fun Signin(  
    navController: NavController,  
    viewModel: SigninViewModel  
) {  
val context = LocalContext.current  
    var expanded by remember { mutableStateOf(false) }  
    var selectedCountry by remember { mutableStateOf(countries[0]) }  
    var phoneNumber by remember { mutableStateOf("") }  
  
    val isLoading by viewModel.isLoading  
    val errorMessage by viewModel.errorMessage  
  
    EnvgotravelTheme {  
        Column(  
            modifier = Modifier  
                .fillMaxSize()  
                .background(MaterialTheme.colorScheme.background)  
                .padding(24.dp),  
            horizontalAlignment = Alignment.CenterHorizontally,  
            verticalArrangement = Arrangement.Center  
        ) {  
            Text(
    text = "Entrar com telefone",
    style = MaterialTheme.typography.headlineSmall,
    color = MaterialTheme.colorScheme.onBackground,
    textAlign = TextAlign.Center,
    fontWeight = FontWeight.Bold
)
Spacer(modifier = Modifier.height(20.dp))
  
            Box {  
                OutlinedTextField(  
                    value = phoneNumber,  
                    onValueChange = { phoneNumber = it },  
                    label = { Text("Telefone") },  
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),  
                    singleLine = true,  
                    leadingIcon = {  
                        Row(  
                            verticalAlignment = Alignment.CenterVertically,  
                            modifier = Modifier  
                                .clickable { expanded = true }  
                                .padding(start = 8.dp)  
                        ) {  
                            Image(  
                                painter = rememberAsyncImagePainter(selectedCountry.flagUrl),  
                                contentDescription = selectedCountry.name,  
                                modifier = Modifier.size(20.dp)  
                            )  
                            Spacer(modifier = Modifier.width(6.dp))  
                            Text(selectedCountry.code)  
                        }  
                    },  
                    modifier = Modifier.fillMaxWidth()  
                )  
  
                DropdownMenu(  
                    expanded = expanded,  
                    onDismissRequest = { expanded = false }  
                ) {  
                    countries.forEach { country ->  
                        DropdownMenuItem(  
                            text = {  
                                Row(verticalAlignment = Alignment.CenterVertically) {  
                                    Image(  
                                        painter = rememberAsyncImagePainter(country.flagUrl),  
                                        contentDescription = country.name,  
                                        modifier = Modifier.size(20.dp)  
                                    )  
                                    Spacer(modifier = Modifier.width(8.dp))  
                                    Text("${country.name} (${country.code})")  
                                }  
                            },  
                            onClick = {  
                                selectedCountry = country  
                                expanded = false  
                            }  
                        )  
                    }  
                }  
            }  
  
            if (!errorMessage.isNullOrEmpty()) {  
                Spacer(modifier = Modifier.height(8.dp))  
                Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)  
            }  
  
            Spacer(modifier = Modifier.height(16.dp))  
  
            // 🔹 Apenas envia OTP e navega  
            ButtonLoading(  
                text = "Next",  
                loading = isLoading,  
                enabled = phoneNumber.length >= 9,  
                onClick = {  
                    val formattedPhone = PhoneNumberUtils.formatForPost(selectedCountry.code + phoneNumber)  
  
                    viewModel.sendOtpAndCheckPhone(formattedPhone) { success, error ->  
                        if (success) {  
                            navController.navigate("confirmation/$formattedPhone")  
                        } else {  
                            viewModel.errorMessage.value = error  
                        }  
                    }  
                },  
                modifier = Modifier.fillMaxWidth()  
            )  
  
            Spacer(modifier = Modifier.height(16.dp))  
  
Button(  
    onClick = {  
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://imlinkey.store"))  
        // Verifica se há app capaz de abrir a URL  
        if (intent.resolveActivity(context.packageManager) != null) {  
            context.startActivity(intent)  
        }  
    },  
    modifier = Modifier.fillMaxWidth()  
) {  
    Image(  
        painter = rememberAsyncImagePainter("https://imlinkey.store/favicon.png"),  
        contentDescription = "Imlinkey Logo",  
        modifier = Modifier.size(20.dp)  
    )  
    Spacer(modifier = Modifier.width(8.dp))  
    Text("Signin with Imlinkey")  
}  
  
            Spacer(modifier = Modifier.height(32.dp))  
  
            Column(horizontalAlignment = Alignment.CenterHorizontally) {  
                TextButton(onClick = { /* abrir termos */ }) { Text("Termos de Uso") }  
                TextButton(onClick = { /* abrir política */ }) { Text("Política de Privacidade") }  
            }  
        }  
    }  
}