package com.korddy.envgotravel.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp


@Composable
fun ButtonLoading(
    loading: Boolean = false,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        } else {
            Text(text)
        }
    }
}
