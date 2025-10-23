package com.korddy.envgotravel.navigations.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.korddy.envgotravel.domain.user.User
import com.korddy.envgotravel.services.session.SessionCache
import com.korddy.envgotravel.ui.components.DrawerItem
import com.google.gson.Gson

@Composable
fun Drawer(
    navController: NavController,
    onClose: () -> Unit
) {
    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        SessionCache.getCurrentUser()?.let { userJson ->
            currentUser = Gson().fromJson(userJson, User::class.java)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp)
    ) {
        // Cabeçalho
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Olá, ${currentUser?.firstName ?: "Visitante"}!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val fullName = listOfNotNull(currentUser?.firstName, currentUser?.lastName)
                .joinToString(" ")
            if (fullName.isNotBlank()) {
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }

            if (!currentUser?.email.isNullOrBlank()) {
                Text(
                    text = currentUser?.email ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Itens principais
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            DrawerItem(Icons.Default.Map, "Mapa") {
                navController.navigate("map"); onClose()
            }
            DrawerItem(Icons.Default.CreditCard, "Métodos de pagamento") {
                navController.navigate("paymentmethods"); onClose()
            }
            DrawerItem(Icons.Default.Percent, "Descontos e ofertas") {
                navController.navigate("discounts"); onClose()
            }
            DrawerItem(Icons.Default.History, "Histórico") {
                navController.navigate("history"); onClose()
            }
            DrawerItem(Icons.Default.LocationOn, "As minhas moradas") {
                navController.navigate("addresses"); onClose()
            }
            DrawerItem(Icons.Default.SupportAgent, "Suporte") {
                navController.navigate("support"); onClose()
            }
            DrawerItem(Icons.Default.Security, "Segurança") {
                navController.navigate("security"); onClose()
            }

            // 2FA
            DrawerItem(Icons.Default.Lock, "Configurar 2FA") {
                navController.navigate("setup2fa"); onClose()
            }
            DrawerItem(Icons.Default.VerifiedUser, "Verificar 2FA") {
                navController.navigate("verify2fa"); onClose()
            }

            DrawerItem(Icons.Default.DriveEta, "Ganhar como motorista") {
                navController.navigate("driverearn"); onClose()
            }
            DrawerItem(Icons.Default.Work, "Conta empresarial") {
                navController.navigate("businessaccount"); onClose()
            }
            DrawerItem(Icons.Default.Settings, "Definições") {
                navController.navigate("settings"); onClose()
            }
            DrawerItem(Icons.Default.Info, "Informações") {
                navController.navigate("information"); onClose()
            }
        }

        // Logout
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            DrawerItem(
                icon = Icons.Default.Logout,
                label = "Sair",
                iconTint = MaterialTheme.colorScheme.error
            ) {
                SessionCache.clearAuthToken()
                SessionCache.clearCurrentUser()
                navController.navigate("signin") { popUpTo(0) }
                onClose()
            }
        }
    }
}