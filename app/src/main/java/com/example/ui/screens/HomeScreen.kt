package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.platform.LocalContext
import com.example.util.IntentHelper
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MealRecord
import com.example.data.repository.FitLifeRepository
import com.example.ui.components.AppleFitnessActivityRingsCard
import com.example.ui.components.AuthDialog
import com.example.ui.components.SpatialGlassCard
import com.example.ui.components.UnifiedHydrationCalorieChartCard
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.LocalCurrentThemeMode
import com.example.ui.theme.LocalThemeController
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.RingBurnRed
import com.example.ui.theme.RingCaloriesOrange
import com.example.ui.theme.RingStepsYellow
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SuccessGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: FitLifeRepository,
    onNavigateToNutrition: () -> Unit,
    onNavigateToYoga: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val user by repository.currentUser.collectAsState()
    val todaySummary by repository.todaySummary.collectAsState()
    val weeklyMetrics by repository.weeklyMetrics.collectAsState()
    val todayMeals by repository.todayMeals.collectAsState()
    val challenges by repository.challenges.collectAsState()

    val currentTheme = LocalCurrentThemeMode.current
    val onThemeChange = LocalThemeController.current
    val context = LocalContext.current

    var showAuthDialog by remember { mutableStateOf(false) }

    val activeChallenge = challenges.firstOrNull { it.isActive }
    val todayDateFormatted = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = PrimarySaffron.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = PrimarySaffron,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Namaste, ${user?.name?.split(" ")?.firstOrNull() ?: "Yogi"}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = todayDateFormatted,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Theme Switcher Quick Toggle
                    IconButton(onClick = {
                        val nextMode = when (currentTheme) {
                            AppThemeMode.LIGHT -> AppThemeMode.DARK
                            AppThemeMode.DARK -> AppThemeMode.SYSTEM
                            AppThemeMode.SYSTEM -> AppThemeMode.LIGHT
                        }
                        onThemeChange(nextMode)
                    }) {
                        Icon(
                            imageVector = if (currentTheme == AppThemeMode.DARK) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Toggle Theme",
                            tint = PrimarySaffron
                        )
                    }

                    // T&C Quick Link
                    IconButton(onClick = onNavigateToTerms) {
                        Icon(
                            imageVector = Icons.Default.Policy,
                            contentDescription = "Terms and Conditions",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Account dialog
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = PrimarySaffron.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { showAuthDialog = true }
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = if (user != null) "Profile" else "Sign In",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffron
                        )
                    }
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
            // 1. APPLE FITNESS STYLE concentric activity rings card at the top
            item {
                AppleFitnessActivityRingsCard(summary = todaySummary)
            }

            // 2. ACTIVE SPECIAL CHALLENGE BANNER (Winter Arc / 75 Hard)
            item {
                if (activeChallenge != null) {
                    SpatialGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = RingBurnRed.copy(alpha = 0.15f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = RingBurnRed,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = activeChallenge.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = RingBurnRed
                                    ) {
                                        Text(
                                            text = "DAY ${activeChallenge.currentStreak}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "4-Week Cleanup Suspended: Challenge logs are protected.",
                                    fontSize = 11.sp,
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 3. UNIFIED HYDRATION & CALORIE WEEKLY CHART IN THE SAME CARD
            item {
                UnifiedHydrationCalorieChartCard(
                    weeklySummaries = weeklyMetrics,
                    onAddWater = { amount -> repository.addWater(amount) },
                    onLogMealClick = onNavigateToNutrition
                )
            }

            // 4. QUICK ACTIONS BAR (CSE3142 UI, Intents & Notifications)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ElevatedButton(
                            onClick = onNavigateToNutrition,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = PrimarySaffron,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Log Meal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        ElevatedButton(
                            onClick = onNavigateToYoga,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = SageGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.SelfImprovement, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Start Yoga", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        ElevatedButton(
                            onClick = { repository.addStepsAndBurn(500, 22) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = RingStepsYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsWalk, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+500 Steps", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Second Action Row: Android Implicit Intent & Local Notification
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ElevatedButton(
                            onClick = { IntentHelper.shareDailyProgress(context, todaySummary) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = OceanBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share Rings (Intent)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        ElevatedButton(
                            onClick = { repository.triggerHydrationReminderNotification() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Hydrate Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 5. AYURVEDIC WISDOM CARD
            item {
                SpatialGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrimarySaffron.copy(alpha = 0.2f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Text(
                                    text = "🕉️",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dinacharya Insight",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = PrimarySaffron
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Drink copper-charged warm water first thing at dawn (Ushapan) to balance Pitta, cleanse your gut, and activate digestive Agni before morning Surya Namaskar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 6. TODAY'S MEALS TIMELINE
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Meals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${todayMeals.size} logged",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (todayMeals.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = "No meals logged yet today. Tap 'Log Meal' to track Indian food macros.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(todayMeals) { meal ->
                    MealItemCard(meal)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAuthDialog) {
        AuthDialog(
            onDismiss = { showAuthDialog = false },
            onLocalRegister = { email, pass, name -> repository.register(email, pass, name) },
            onLocalLogin = { email, pass -> repository.login(email, pass) },
            onGoogleLogin = { email, name -> repository.loginGoogle(email, name) }
        )
    }
}

@Composable
private fun MealItemCard(meal: MealRecord) {
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
                modifier = Modifier.size(42.dp)
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${meal.mealType} • ${meal.timeFormatted}",
                    style = MaterialTheme.typography.labelSmall,
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
                fontSize = 14.sp,
                color = RingCaloriesOrange
            )
        }
    }
}
