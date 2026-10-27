package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassDarkBorder
import com.example.ui.theme.GlassWhiteBorder
import com.example.ui.theme.PrimarySaffron

/**
 * Reusable Spatial Glassmorphism Card with Neomorphic Soft Dual Shadows
 * Gives tactile depth and frosted glass elegance without any AI slop or purple gradients.
 */
@Composable
fun SpatialGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    elevation: Dp = 6.dp,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val defaultBorder = borderStroke ?: BorderStroke(
        width = 1.dp,
        color = if (isDark) GlassDarkBorder else GlassWhiteBorder
    )
    val defaultBg = backgroundColor ?: if (isDark) {
        Color(0xCC1E1A16)
    } else {
        Color(0xE6FFFDFB)
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color(0x26000000),
                spotColor = if (isDark) PrimarySaffron.copy(alpha = 0.15f) else Color(0x33FF9F43)
            )
            .clip(shape)
            .background(defaultBg)
            .border(defaultBorder, shape),
        content = content
    )
}

/**
 * Neomorphic Pill Chip / Button with Embossed High-Contrast Accent
 */
@Composable
fun NeomorphicPill(
    text: String,
    isSelected: Boolean = false,
    icon: ImageVector? = null,
    accentColor: Color = PrimarySaffron,
    onClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val elevation by animateDpAsState(targetValue = if (isSelected) 1.dp else 4.dp, label = "elevation")
    val bgColor = if (isSelected) {
        accentColor.copy(alpha = if (isDark) 0.28f else 0.18f)
    } else {
        if (isDark) Color(0xFF241F1A) else Color(0xFFF7F2EC)
    }
    val borderColor = if (isSelected) accentColor else if (isDark) Color(0x33FFFFFF) else Color(0x22000000)

    Surface(
        modifier = Modifier
            .shadow(elevation, RoundedCornerShape(50), ambientColor = Color.Black.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(50))
            .clickable { onClick() },
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
