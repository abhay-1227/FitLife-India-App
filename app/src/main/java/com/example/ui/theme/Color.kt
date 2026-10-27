package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Indian Wellness Palette (Zero Purple Gradients)
val PrimarySaffron = Color(0xFFFF9F43)        // Deep Indian Saffron
val PrimarySaffronDark = Color(0xFFE07E20)
val SageGreen = Color(0xFF6D6E45)             // Ayurvedic Sage
val SageGreenLight = Color(0xFF8B8C5C)
val OceanBlue = Color(0xFF4A90E2)             // Technology & Water
val Terracotta = Color(0xFFE06D53)            // Indian Earth

// Apple Fitness Ring Colors
val RingStepsYellow = Color(0xFFFFD600)       // Steps (Yellow)
val RingCaloriesOrange = Color(0xFFFF9500)    // Calorie Intake (Orange)
val RingBurnRed = Color(0xFFFF3B30)           // Calorie Burning (Red)

// Semantic & Status
val SuccessGreen = Color(0xFF27AE60)
val WarningAmber = Color(0xFFF39C12)
val ErrorRed = Color(0xFFE74C3C)
val InfoCyan = Color(0xFF00C6FF)

// Background & Surfaces
val SurfaceCreamLight = Color(0xFFFDF8F3)     // Light Warm Cream
val SurfaceCreamCard = Color(0xFFFFFDFB)
val BackgroundDark = Color(0xFF141210)        // Deep Obsidian Graphite
val SurfaceDarkCard = Color(0xFF1E1A16)       // Dark Neomorphic Card

// Glassmorphism Accents
val GlassWhiteHigh = Color(0x33FFFFFF)
val GlassWhiteBorder = Color(0x4DFFFFFF)
val GlassDarkHigh = Color(0x332A241F)
val GlassDarkBorder = Color(0x4D453C33)

// Gradients (Authentic Saffron, Sunset, Earth)
val SaffronSunsetBrush = Brush.linearGradient(
    colors = listOf(Color(0xFFFF9F43), Color(0xFFFF5E3A))
)
val GoldSunBrush = Brush.linearGradient(
    colors = listOf(Color(0xFFFFD600), Color(0xFFFF9F43))
)
val OceanWaterBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
)
val EarthSageBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF6D6E45), Color(0xFF9EA068))
)
val FireBurnBrush = Brush.linearGradient(
    colors = listOf(Color(0xFFFF3B30), Color(0xFFFF9500))
)
