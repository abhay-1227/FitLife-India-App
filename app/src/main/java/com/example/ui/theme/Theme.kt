package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

val LocalThemeController = compositionLocalOf<(AppThemeMode) -> Unit> { {} }
val LocalCurrentThemeMode = compositionLocalOf { AppThemeMode.SYSTEM }

private val DarkColorScheme = darkColorScheme(
    primary = PrimarySaffron,
    onPrimary = Color.Black,
    secondary = SageGreenLight,
    onSecondary = Color.Black,
    tertiary = OceanBlue,
    onTertiary = Color.White,
    background = BackgroundDark,
    onBackground = Color(0xFFF0EAE1),
    surface = SurfaceDarkCard,
    onSurface = Color(0xFFF0EAE1),
    surfaceVariant = Color(0xFF28231D),
    onSurfaceVariant = Color(0xFFC7BDB1),
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimarySaffron,
    onPrimary = Color.White,
    secondary = SageGreen,
    onSecondary = Color.White,
    tertiary = OceanBlue,
    onTertiary = Color.White,
    background = SurfaceCreamLight,
    onBackground = Color(0xFF1E1A16),
    surface = SurfaceCreamCard,
    onSurface = Color(0xFF1E1A16),
    surfaceVariant = Color(0xFFF2ECE4),
    onSurfaceVariant = Color(0xFF524A40),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun FitLifeIndiaTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    onThemeModeChange: (AppThemeMode) -> Unit = {},
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalCurrentThemeMode provides themeMode,
        LocalThemeController provides onThemeModeChange
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
