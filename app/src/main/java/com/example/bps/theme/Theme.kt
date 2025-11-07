package com.example.bps.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import com.example.bps.theme.Black // <-- Impor ditambahkan

// Skema warna untuk Tema Gelap (Dark Theme)
private val DarkColorScheme = darkColorScheme(
    primary = Orange300,
    onPrimary = Black, // Diubah
    primaryContainer = Orange800,
    onPrimaryContainer = Black, // Diubah
    secondary = Emerald300,
    onSecondary = Black, // Diubah
    secondaryContainer = Emerald800,
    onSecondaryContainer = Black, // Diubah
    tertiary = Sky300,
    onTertiary = Black, // Diubah
    tertiaryContainer = Sky800,
    onTertiaryContainer = Black, // Diubah
    error = Red300,
    onError = Black, // Diubah
    errorContainer = Red800,
    onErrorContainer = Black, // Diubah
    background = Gray100,
    onBackground = Black, // Diubah
    surface = Gray900,
    onSurface = Black, // Diubah
    surfaceVariant = Gray700,
    onSurfaceVariant = Black, // Diubah
    outline = Gray600
)

// Skema warna untuk Tema Terang (Light Theme)
private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = Black, // Sudah Benar
    primaryContainer = Orange100,
    onPrimaryContainer = Black, // Diubah
    secondary = Emerald600,
    onSecondary = Black, // Sudah Benar
    secondaryContainer = Emerald100,
    onSecondaryContainer = Black, // Diubah
    tertiary = Sky500,
    onTertiary = Black, // Sudah Benar
    tertiaryContainer = Sky100,
    onTertiaryContainer = Black, // Diubah
    error = Red600,
    onError = Black, // Sudah Benar
    errorContainer = Red100,
    onErrorContainer = Black, // Diubah
    background = Gray50,
    onBackground = Black, // Diubah
    surface = White, // Sebaiknya ini warna terang (misal White)
    onSurface = Black, // Diubah
    surfaceVariant = Orange300, // Sebaiknya ini warna terang
    onSurfaceVariant = Gray900, // Diubah
    outline = Gray900
)

@Composable
fun BpsTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}