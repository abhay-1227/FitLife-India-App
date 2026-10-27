package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailySummary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.RingCaloriesOrange
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

enum class ChartDisplayMode {
    COMBINED,
    HYDRATION_ONLY,
    CALORIES_ONLY
}

@Composable
fun UnifiedHydrationCalorieChartCard(
    weeklySummaries: List<DailySummary>,
    onAddWater: (Int) -> Unit,
    onLogMealClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var displayMode by remember { mutableStateOf(ChartDisplayMode.COMBINED) }
    var selectedDayIndex by remember { mutableIntStateOf(weeklySummaries.size - 1) }

    val safeSelectedSummary = weeklySummaries.getOrNull(selectedDayIndex) ?: weeklySummaries.lastOrNull()

    SpatialGlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WEEKLY NOURISHMENT",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Hydration & Calorie Balance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Legend chips
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (displayMode != ChartDisplayMode.CALORIES_ONLY) {
                        LegendDot(color = OceanBlue, label = "Water")
                    }
                    if (displayMode != ChartDisplayMode.HYDRATION_ONLY) {
                        LegendDot(color = PrimarySaffron, label = "Calories")
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs (Combined / Water / Cals)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeomorphicPill(
                    text = "Combined",
                    isSelected = displayMode == ChartDisplayMode.COMBINED,
                    accentColor = PrimarySaffron,
                    onClick = { displayMode = ChartDisplayMode.COMBINED }
                )
                NeomorphicPill(
                    text = "Water Only",
                    isSelected = displayMode == ChartDisplayMode.HYDRATION_ONLY,
                    icon = Icons.Default.LocalDrink,
                    accentColor = OceanBlue,
                    onClick = { displayMode = ChartDisplayMode.HYDRATION_ONLY }
                )
                NeomorphicPill(
                    text = "Calories Only",
                    isSelected = displayMode == ChartDisplayMode.CALORIES_ONLY,
                    icon = Icons.Default.Restaurant,
                    accentColor = RingCaloriesOrange,
                    onClick = { displayMode = ChartDisplayMode.CALORIES_ONLY }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Dual Bars Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val daysCount = weeklySummaries.size.coerceAtLeast(1)
                    val slotWidth = size.width / daysCount
                    val bottomY = size.height - 24.dp.toPx()
                    val chartHeight = bottomY - 10.dp.toPx()

                    // Reference threshold dashed lines:
                    // 2500 ml target water = 0.8 * chartHeight
                    val waterTargetY = bottomY - (0.8f * chartHeight)
                    val calTargetY = bottomY - (0.75f * chartHeight)

                    drawLine(
                        color = OceanBlue.copy(alpha = 0.25f),
                        start = Offset(0f, waterTargetY),
                        end = Offset(size.width, waterTargetY),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawLine(
                        color = PrimarySaffron.copy(alpha = 0.25f),
                        start = Offset(0f, calTargetY),
                        end = Offset(size.width, calTargetY),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    weeklySummaries.forEachIndexed { i, item ->
                        val slotCenter = (i * slotWidth) + (slotWidth / 2f)
                        val isSelected = i == selectedDayIndex

                        // Highlight background for selected day
                        if (isSelected) {
                            drawRoundRect(
                                color = PrimarySaffron.copy(alpha = 0.08f),
                                topLeft = Offset(i * slotWidth + 4.dp.toPx(), 0f),
                                size = Size(slotWidth - 8.dp.toPx(), bottomY),
                                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                            )
                        }

                        // Max reference: Water = 3500ml, Cals = 2800kcal
                        val waterFraction = (item.waterMl.toFloat() / 3200f).coerceIn(0.04f, 1f)
                        val calFraction = (item.caloriesConsumed.toFloat() / 2600f).coerceIn(0.04f, 1f)

                        val barWidth = 10.dp.toPx()

                        when (displayMode) {
                            ChartDisplayMode.COMBINED -> {
                                val wBarH = waterFraction * chartHeight
                                val cBarH = calFraction * chartHeight

                                // Water bar (left)
                                drawRoundRect(
                                    color = if (isSelected) OceanBlue else OceanBlue.copy(alpha = 0.70f),
                                    topLeft = Offset(slotCenter - barWidth - 2.dp.toPx(), bottomY - wBarH),
                                    size = Size(barWidth, wBarH),
                                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                )

                                // Calorie bar (right)
                                drawRoundRect(
                                    color = if (isSelected) PrimarySaffron else PrimarySaffron.copy(alpha = 0.70f),
                                    topLeft = Offset(slotCenter + 2.dp.toPx(), bottomY - cBarH),
                                    size = Size(barWidth, cBarH),
                                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                )
                            }
                            ChartDisplayMode.HYDRATION_ONLY -> {
                                val wBarH = waterFraction * chartHeight
                                val singleWidth = 18.dp.toPx()
                                drawRoundRect(
                                    color = if (isSelected) OceanBlue else OceanBlue.copy(alpha = 0.75f),
                                    topLeft = Offset(slotCenter - (singleWidth / 2f), bottomY - wBarH),
                                    size = Size(singleWidth, wBarH),
                                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                )
                            }
                            ChartDisplayMode.CALORIES_ONLY -> {
                                val cBarH = calFraction * chartHeight
                                val singleWidth = 18.dp.toPx()
                                drawRoundRect(
                                    color = if (isSelected) PrimarySaffron else PrimarySaffron.copy(alpha = 0.75f),
                                    topLeft = Offset(slotCenter - (singleWidth / 2f), bottomY - cBarH),
                                    size = Size(singleWidth, cBarH),
                                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            // Day Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weeklySummaries.forEachIndexed { index, item ->
                    val dayLabel = try {
                        val parsed = LocalDate.parse(item.date)
                        parsed.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).take(2)
                    } catch (_: Exception) {
                        "D${index + 1}"
                    }
                    val isSelected = index == selectedDayIndex

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { selectedDayIndex = index }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) PrimarySaffron else Color.Transparent
                    ) {
                        Text(
                            text = dayLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detail Panel for selected day
            if (safeSelectedSummary != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Water Intake", fontSize = 11.sp, color = OceanBlue, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${safeSelectedSummary.waterMl} ml",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Goal: ${safeSelectedSummary.waterGoal} ml",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Calories Eaten", fontSize = 11.sp, color = PrimarySaffron, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${safeSelectedSummary.caloriesConsumed} kcal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Goal: ${safeSelectedSummary.calorieGoal} kcal",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick In-Card Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ElevatedButton(
                    onClick = { onAddWater(250) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = OceanBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+250ml Water", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onLogMealClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimarySaffron
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySaffron)
                ) {
                    Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Log Meal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
