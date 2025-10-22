package com.korddy.envgotravel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ==== Esquemas de cores personalizados ====
private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = BlueDark,
    secondary = White,
    onSecondary = BlueDark,
    tertiary = White,
    background = BlueDark,   // #00003A
    surface = BlueDark,      // #00003A
    onBackground = White,
    onSurface = White,
    error = Red,
    outline = Gray
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = Black,
    onSecondary = White,
    tertiary = Black,
    background = White,     // #FFFFFF
    surface = White,        // #FFFFFF
    onBackground = Black,
    onSurface = Black,
    error = Red,
    outline = Gray
)

// ==== Tema principal fixo (sem dynamic color) ====
@Composable
fun EnvgotravelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}