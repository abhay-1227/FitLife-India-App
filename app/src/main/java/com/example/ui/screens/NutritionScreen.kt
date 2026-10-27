package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.FitLifeRepository
import com.example.ui.components.SpatialGlassCard
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.RingCaloriesOrange
import com.example.ui.theme.SageGreen

data class IndianFoodTemplate(
    val name: String,
    val portion: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fats: Float,
    val mealType: String
)

val IndianFoodsCatalog = listOf(
    IndianFoodTemplate("2 Roti with Ghee", "2 pieces (70g)", 220, 6f, 36f, 5f, "Lunch"),
    IndianFoodTemplate("Dal Tadka (Yellow Lentils)", "1 medium bowl (150g)", 180, 11f, 26f, 4f, "Lunch"),
    IndianFoodTemplate("Paneer Tikka Grilled", "6 cubes (120g)", 260, 18f, 8f, 18f, "Dinner"),
    IndianFoodTemplate("Steamed Idli with Sambar", "2 Idlis + Sambar bowl", 280, 10f, 48f, 4f, "Breakfast"),
    IndianFoodTemplate("Masala Dosa with Chutney", "1 medium dosa", 340, 7f, 52f, 12f, "Breakfast"),
    IndianFoodTemplate("Rajma Masala with Brown Rice", "1 plate combo", 420, 16f, 72f, 8f, "Lunch"),
    IndianFoodTemplate("Moong Dal Khichdi with Curd", "1 bowl (200g)", 330, 13f, 50f, 7f, "Dinner"),
    IndianFoodTemplate("Palak Paneer", "1 bowl (180g)", 290, 14f, 12f, 21f, "Dinner"),
    IndianFoodTemplate("Sprouted Moong Salad", "1 bowl (100g)", 120, 8f, 20f, 1f, "Snack"),
    IndianFoodTemplate("Roasted Makhana (Foxnuts)", "1 cup (30g)", 110, 3f, 20f, 2f, "Snack")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    repository: FitLifeRepository
) {
    val todayMeals by repository.todayMeals.collectAsState()
    val todaySummary by repository.todaySummary.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var customFoodName by remember { mutableStateOf("") }
    var customCalories by remember { mutableStateOf("250") }
    var selectedMealType by remember { mutableStateOf("Lunch") }

    val filteredCatalog = remember(searchQuery) {
        if (searchQuery.isBlank()) IndianFoodsCatalog else {
            IndianFoodsCatalog.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    val totalProtein = todayMeals.sumOf { it.proteinG.toDouble() }.toFloat()
    val totalCarbs = todayMeals.sumOf { it.carbsG.toDouble() }.toFloat()
    val totalFats = todayMeals.sumOf { it.fatsG.toDouble() }.toFloat()
    val totalMacros = (totalProtein + totalCarbs + totalFats).coerceAtLeast(1f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nutrition & Meals",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Search & AI Meal Parsing Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("What did you eat? (e.g. Idli, Paneer, Dal)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )
            }

            // 2. Quick Suggestions Chips
            item {
                Text(
                    text = "Quick Indian Foods",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredCatalog) { item ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    repository.logMeal(
                                        name = item.name,
                                        cals = item.calories,
                                        protein = item.protein,
                                        carbs = item.carbs,
                                        fats = item.fats,
                                        type = item.mealType
                                    )
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = PrimarySaffron.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySaffron.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+${item.calories} kcal",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimarySaffron
                                )
                            }
                        }
                    }
                }
            }

            // 3. Macro Breakdown Card with Pie Chart
            item {
                SpatialGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "MACRONUTRIENT BREAKDOWN",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Pie chart
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(110.dp)
                            ) {
                                Canvas(modifier = Modifier.size(110.dp)) {
                                    val strokeW = 14.dp.toPx()
                                    val radius = (size.minDimension / 2f) - (strokeW / 2f)
                                    val center = Offset(size.width / 2f, size.height / 2f)

                                    val pSweep = (totalProtein / totalMacros) * 360f
                                    val cSweep = (totalCarbs / totalMacros) * 360f
                                    val fSweep = (totalFats / totalMacros) * 360f

                                    // Protein (Green)
                                    drawArc(
                                        color = SageGreen,
                                        startAngle = -90f,
                                        sweepAngle = pSweep,
                                        useCenter = false,
                                        topLeft = Offset(center.x - radius, center.y - radius),
                                        size = Size(radius * 2, radius * 2),
                                        style = Stroke(width = strokeW)
                                    )
                                    // Carbs (Blue)
                                    drawArc(
                                        color = OceanBlue,
                                        startAngle = -90f + pSweep,
                                        sweepAngle = cSweep,
                                        useCenter = false,
                                        topLeft = Offset(center.x - radius, center.y - radius),
                                        size = Size(radius * 2, radius * 2),
                                        style = Stroke(width = strokeW)
                                    )
                                    // Fats (Saffron)
                                    drawArc(
                                        color = PrimarySaffron,
                                        startAngle = -90f + pSweep + cSweep,
                                        sweepAngle = fSweep,
                                        useCenter = false,
                                        topLeft = Offset(center.x - radius, center.y - radius),
                                        size = Size(radius * 2, radius * 2),
                                        style = Stroke(width = strokeW)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${todaySummary.caloriesConsumed}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    )
                                    Text(text = "kcal", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            // Macro indicators
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                MacroIndicatorRow(
                                    color = SageGreen,
                                    label = "Protein",
                                    amount = "${totalProtein.toInt()}g",
                                    target = "80g target"
                                )
                                MacroIndicatorRow(
                                    color = OceanBlue,
                                    label = "Carbohydrates",
                                    amount = "${totalCarbs.toInt()}g",
                                    target = "250g target"
                                )
                                MacroIndicatorRow(
                                    color = PrimarySaffron,
                                    label = "Fats",
                                    amount = "${totalFats.toInt()}g",
                                    target = "65g target"
                                )
                            }
                        }
                    }
                }
            }

            // 4. Custom Meal Entry Form
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Log Custom Dish",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = customFoodName,
                                onValueChange = { customFoodName = it },
                                label = { Text("Dish Name") },
                                modifier = Modifier.weight(1.5f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = customCalories,
                                onValueChange = { customCalories = it },
                                label = { Text("Calories") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val cals = customCalories.toIntOrNull() ?: 200
                                val name = if (customFoodName.isNotBlank()) customFoodName else "Custom Sattvic Meal"
                                val protein = (cals * 0.15f / 4f)
                                val carbs = (cals * 0.60f / 4f)
                                val fats = (cals * 0.25f / 9f)

                                repository.logMeal(name, cals, protein, carbs, fats, selectedMealType)
                                customFoodName = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimarySaffron,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Add to Today's Log", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 5. Today's Logged Meals
            item {
                Text(
                    text = "Today's Logged Meals (${todayMeals.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(todayMeals) { meal ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimarySaffron.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = PrimarySaffron,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = meal.foodName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${meal.mealType} • ${meal.timeFormatted}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "P: ${meal.proteinG.toInt()}g | C: ${meal.carbsG.toInt()}g | F: ${meal.fatsG.toInt()}g",
                                fontSize = 11.sp,
                                color = SageGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${meal.calories} kcal",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = RingCaloriesOrange
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MacroIndicatorRow(
    color: Color,
    label: String,
    amount: String,
    target: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row {
                Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "($target)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
