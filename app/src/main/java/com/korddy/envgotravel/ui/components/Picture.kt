package com.korddy.envgotravel.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.io.File

/**
 * Componente Picture universal para visualizar ou atualizar avatar
 * @param value pode ser String (URL) ou File local
 * @param size tamanho do círculo
 * @param onUpload callback para upload do avatar (CRUD)
 * @param onDelete callback para deletar avatar
 */
@Composable
fun Picture(
    value: Any?, // File ou URL (String)
    size: Dp = 120.dp,
    onUpload: ((File) -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    var showCrudDialog by remember { mutableStateOf(false) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var localFile by remember { mutableStateOf<File?>(null) }

    // Launcher para galeria
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = File(it.path ?: "")
            localFile = file
            onUpload?.invoke(file)
        }
        showSourceDialog = false
    }

    // Launcher para câmera
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            // Aqui podes converter bitmap para File se necessário
            val file = File("/tmp/avatar.jpg") // Exemplo de placeholder
            localFile = file
            onUpload?.invoke(file)
        }
        showSourceDialog = false
    }

    val imagePainter = rememberAsyncImagePainter(
        model = localFile?.path ?: value?.toString() ?: "/default-avatar.png"
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
            .clickable(enabled = onUpload != null || onDelete != null) { showCrudDialog = true },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // CRUD Dialog
        if (showCrudDialog) {
            AlertDialog(
                onDismissRequest = { showCrudDialog = false },
                title = { Text("Avatar") },
                text = { Text("Escolha uma ação") },
                confirmButton = {
                    if (onUpload != null) {
                        TextButton(onClick = {
                            showCrudDialog = false
                            showSourceDialog = true
                        }) { Text("Atualizar") }
                    }
                },
                dismissButton = {
                    Row {
                        if (onDelete != null) {
                            TextButton(onClick = {
                                onDelete.invoke()
                                showCrudDialog = false
                            }) { Text("Deletar") }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { showCrudDialog = false }) { Text("Cancelar") }
                    }
                }
            )
        }

        // Fonte: câmera ou galeria
        if (showSourceDialog) {
            AlertDialog(
                onDismissRequest = { showSourceDialog = false },
                title = { Text("Escolher fonte") },
                text = { Text("Escolha a origem da imagem") },
                confirmButton = {
                    TextButton(onClick = {
                        galleryLauncher.launch("image/*")
                        showSourceDialog = false
                    }) { Text("Galeria") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        cameraLauncher.launch(null)
                        showSourceDialog = false
                    }) { Text("Câmera") }
                }
            )
        }
    }
}