package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailySummary
import com.example.ui.theme.RingBurnRed
import com.example.ui.theme.RingCaloriesOrange
import com.example.ui.theme.RingStepsYellow

@Composable
fun AppleFitnessActivityRingsCard(
    summary: DailySummary,
    modifier: Modifier = Modifier
) {
    // Animated progress sweeps
    val animatedStepsSweep by animateFloatAsState(
        targetValue = summary.stepsPercent.coerceIn(0f, 1.5f),
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "stepsSweep"
    )
    val animatedCaloriesSweep by animateFloatAsState(
        targetValue = summary.caloriesPercent.coerceIn(0f, 1.5f),
        animationSpec = tween(durationMillis = 1400, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "caloriesSweep"
    )
    val animatedBurnSweep by animateFloatAsState(
        targetValue = summary.burnPercent.coerceIn(0f, 1.5f),
        animationSpec = tween(durationMillis = 1400, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "burnSweep"
    )

    SpatialGlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACTIVITY RINGS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Daily Vitality Goal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = RingStepsYellow.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${(summary.stepsPercent * 100).toInt()}% Done",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RingCaloriesOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Center Rings Canvas
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(230.dp)
            ) {
                Canvas(modifier = Modifier.size(230.dp)) {
                    val strokeWidth = 18.dp.toPx()
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // 1. Outer Ring: Steps (Yellow #FFD600)
                    val r1 = (size.minDimension / 2f) - (strokeWidth / 2f)
                    val trackYellow = RingStepsYellow.copy(alpha = 0.20f)
                    drawCircle(
                        color = trackYellow,
                        radius = r1,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = RingStepsYellow,
                        startAngle = -90f,
                        sweepAngle = animatedStepsSweep * 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - r1, center.y - r1),
                        size = Size(r1 * 2, r1 * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // 2. Middle Ring: Calorie Intake (Orange #FF9500)
                    val r2 = r1 - strokeWidth - 6.dp.toPx()
                    val trackOrange = RingCaloriesOrange.copy(alpha = 0.20f)
                    drawCircle(
                        color = trackOrange,
                        radius = r2,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = RingCaloriesOrange,
                        startAngle = -90f,
                        sweepAngle = animatedCaloriesSweep * 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - r2, center.y - r2),
                        size = Size(r2 * 2, r2 * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // 3. Inner Ring: Calorie Burn (Red #FF3B30)
                    val r3 = r2 - strokeWidth - 6.dp.toPx()
                    val trackRed = RingBurnRed.copy(alpha = 0.20f)
                    drawCircle(
                        color = trackRed,
                        radius = r3,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = RingBurnRed,
                        startAngle = -90f,
                        sweepAngle = animatedBurnSweep * 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - r3, center.y - r3),
                        size = Size(r3 * 2, r3 * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Center Ring Label
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${summary.steps}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RingStepsYellow
                    )
                    Text(
                        text = "steps taken",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3 Bottom Metric Tiles (Yellow Steps, Orange Intake, Red Burn)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricSummaryTile(
                    modifier = Modifier.weight(1f),
                    title = "STEPS",
                    current = "${summary.steps}",
                    target = "/ ${summary.stepGoal}",
                    unit = "steps",
                    color = RingStepsYellow,
                    icon = Icons.AutoMirrored.Filled.DirectionsWalk
                )
                MetricSummaryTile(
                    modifier = Modifier.weight(1f),
                    title = "INTAKE",
                    current = "${summary.caloriesConsumed}",
                    target = "/ ${summary.calorieGoal}",
                    unit = "kcal",
                    color = RingCaloriesOrange,
                    icon = Icons.Default.Restaurant
                )
                MetricSummaryTile(
                    modifier = Modifier.weight(1f),
                    title = "BURN",
                    current = "${summary.caloriesBurned}",
                    target = "/ ${summary.burnGoal}",
                    unit = "kcal",
                    color = RingBurnRed,
                    icon = Icons.Default.LocalFireDepartment
                )
            }
        }
    }
}

@Composable
private fun MetricSummaryTile(
    modifier: Modifier = Modifier,
    title: String,
    current: String,
    target: String,
    unit: String,
    color: Color,
    icon: ImageVector
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = current,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$target $unit",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
