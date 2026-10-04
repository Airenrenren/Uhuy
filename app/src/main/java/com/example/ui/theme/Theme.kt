package com.example.ui.theme

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

// Aesthetic Calm Light Theme: Coklat Susu, Soft Blue, Warm Cream (tenang, tidak mencolok, nyaman di mata)
private val LightColorScheme = lightColorScheme(
    primary = CoklatSusu,
    onPrimary = Color.White,
    primaryContainer = CoklatSusu.copy(alpha = 0.2f),
    onPrimaryContainer = CoklatSusuDark,
    secondary = SoftBlueDeep,
    onSecondary = Color.White,
    secondaryContainer = SoftBlueIce,
    onSecondaryContainer = Color(0xFF1E3A52),
    tertiary = EarthSage,
    background = CoklatSusuCream,
    onBackground = EarthCharcoal,
    surface = Color(0xFFF5EFE6),
    onSurface = EarthCharcoal,
    surfaceVariant = Color(0xFFEBDED1),
    onSurfaceVariant = EarthMuted,
    outline = Color(0xFFD8C7B5)
)

// Aesthetic Dark Mode: Deep velvety charcoal with warm gold & lavender tones
private val DarkColorScheme = darkColorScheme(
    primary = DarkAccentGold,
    onPrimary = DarkCanvasBg,
    primaryContainer = DarkElevatedSurface,
    onPrimaryContainer = DarkAccentGold,
    secondary = DarkAccentLavender,
    onSecondary = DarkCanvasBg,
    secondaryContainer = DarkElevatedSurface,
    onSecondaryContainer = DarkAccentLavender,
    tertiary = SoftBlue,
    background = DarkCanvasBg,
    onBackground = DarkTextPrimary,
    surface = DarkPaperSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkElevatedSurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder
)

@Composable
fun RuangJurnalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
