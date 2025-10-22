package com.korddy.envgotravel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AlertType {
    SUCCESS, ERROR, WARNING, INFO, DANGER, PRIMARY, SECONDARY
}

@Composable
fun AlertBox(
    message: String,
    type: AlertType,
    onDismiss: () -> Unit
) {
    val backgroundColor: Color
    val contentColor: Color
    val icon = when(type) {
        AlertType.SUCCESS -> {
            backgroundColor = Color(0xFFDFF6DD) // Verde claro
            contentColor = Color(0xFF2E7D32)   // Verde escuro
            Icons.Default.CheckCircle
        }
        AlertType.ERROR -> {
            backgroundColor = Color(0xFFFFE5E5) // Vermelho claro
            contentColor = Color(0xFFD32F2F)    // Vermelho escuro
            Icons.Default.Close
        }
        AlertType.WARNING -> {
            backgroundColor = Color(0xFFFFF4E5) // Amarelo claro
            contentColor = Color(0xFFF57C00)    // Laranja escuro
            Icons.Default.Warning
        }
        AlertType.INFO -> {
            backgroundColor = Color(0xFFE5F0FF) // Azul claro
            contentColor = Color(0xFF1565C0)    // Azul escuro
            Icons.Default.Info
        }
        AlertType.DANGER -> {
            backgroundColor = Color(0xFFFFE5E5) // Vermelho suave
            contentColor = Color(0xFFB71C1C)    // Vermelho escuro
            Icons.Default.Error
        }
        AlertType.PRIMARY -> {
            backgroundColor = Color(0xFFEDE7F6) // Roxo claro
            contentColor = Color(0xFF512DA8)    // Roxo escuro
            Icons.Default.Info
        }
        AlertType.SECONDARY -> {
            backgroundColor = Color(0xFFE0E0E0) // Cinza claro
            contentColor = Color(0xFF000000)    // Preto
            Icons.Default.Info
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Sombra sobre a tela
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            modifier = Modifier
                .fillMaxWidth(0.85f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = message,
                    color = contentColor,
                    fontSize = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentColor,
                        contentColor = backgroundColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}