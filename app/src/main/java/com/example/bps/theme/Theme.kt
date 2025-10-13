package com.example.bps.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Skema warna untuk Tema Gelap (Dark Theme)
private val DarkColorScheme = darkColorScheme(
    primary = Orange300,
    onPrimary = Orange900,
    primaryContainer = Orange800,
    onPrimaryContainer = Orange100,
    secondary = Emerald300,
    onSecondary = Emerald900,
    secondaryContainer = Emerald800,
    onSecondaryContainer = Emerald100,
    tertiary = Sky300,
    onTertiary = Sky900,
    tertiaryContainer = Sky800,
    onTertiaryContainer = Sky100,
    error = Red300,
    onError = Red900,
    errorContainer = Red800,
    onErrorContainer = Red100,
    background = Gray300,
    onBackground = Gray200,
    surface = Gray50,
    onSurface = Gray200,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,
    outline = Gray600
)

// Skema warna untuk Tema Terang (Light Theme)
private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = White,
    primaryContainer = Orange100,
    onPrimaryContainer = Orange900,
    secondary = Emerald600,
    onSecondary = White,
    secondaryContainer = Emerald100,
    onSecondaryContainer = Emerald900,
    tertiary = Sky500,
    onTertiary = White,
    tertiaryContainer = Sky100,
    onTertiaryContainer = Sky900,
    error = Red600,
    onError = White,
    errorContainer = Red100,
    onErrorContainer = Red900,
    background = Gray50,
    onBackground = Gray900,
    surface = Gray50,
    onSurface = Gray900,
    surfaceVariant = Gray200,
    onSurfaceVariant = Gray700,
    outline = Gray400
)

@Composable
fun BpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}