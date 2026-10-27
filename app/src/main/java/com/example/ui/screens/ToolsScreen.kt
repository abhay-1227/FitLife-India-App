package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.repository.FitLifeRepository
import com.example.ui.components.SpatialGlassCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.CheckCircle
import android.view.LayoutInflater
import android.widget.Button as AndroidButton
import android.widget.EditText as AndroidEditText
import android.widget.TextView as AndroidTextView
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.data.remote.AyurvedicDietaryTip
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.RingBurnRed
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

data class WellnessToolItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color
)

val WellnessToolsList = listOf(
    WellnessToolItem("dashboard", "Vitality Dashboard", "Weekly summary & charts", Icons.Default.Dashboard, OceanBlue),
    WellnessToolItem("bmi", "BMI Calculator", "South Asian WHO thresholds", Icons.Default.Calculate, PrimarySaffron),
    WellnessToolItem("water", "Water Tracker", "Ushapan & hydration log", Icons.Default.LocalDrink, OceanBlue),
    WellnessToolItem("sleep", "Sleep Quality", "Ayurvedic circadian rhythm", Icons.Default.NightsStay, SageGreen),
    WellnessToolItem("planner", "Meal Planner", "Sattvic 7-day schedule", Icons.Default.CalendarMonth, PrimarySaffron),
    WellnessToolItem("dosha", "Dosha Quiz", "Discover Vata, Pitta, Kapha", Icons.Default.AutoAwesome, SageGreen),
    WellnessToolItem("recipes", "Recipe Browser", "Traditional Ayurvedic dishes", Icons.Default.Book, PrimarySaffron),
    WellnessToolItem("yoga_timer", "Yoga Timer", "Guided asana intervals", Icons.Default.Timer, RingBurnRed),
    WellnessToolItem("meditate", "Meditation Timer", "Om chanting & pranayama", Icons.Default.SelfImprovement, SageGreen),
    WellnessToolItem("streak", "Streak Tracker", "Winter Arc & 75 Hard", Icons.Default.LocalFireDepartment, RingBurnRed),
    WellnessToolItem("syllabus", "Academic Scope (CSE3142)", "Course Outcomes CO1-CO5 mapping", Icons.Default.School, PrimarySaffron),
    WellnessToolItem("json_diet", "Ayurvedic JSON Guide", "Native JSON parser demo", Icons.Default.DataObject, OceanBlue)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    repository: FitLifeRepository,
    onNavigateToHome: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToYoga: () -> Unit
) {
    var activeModal by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wellness Tools & Lab Scope",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(WellnessToolsList) { tool ->
                    SpatialGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(138.dp)
                            .clickable {
                                when (tool.id) {
                                    "dashboard" -> onNavigateToHome()
                                    "recipes" -> onNavigateToNutrition()
                                    "yoga_timer", "meditate" -> onNavigateToYoga()
                                    else -> activeModal = tool.id
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = tool.accentColor.copy(alpha = 0.15f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = tool.icon,
                                    contentDescription = null,
                                    tint = tool.accentColor,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = tool.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = tool.subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // BMI Modal
    if (activeModal == "bmi") {
        BmiCalculatorModal(onDismiss = { activeModal = null })
    }

    // Dosha Quiz Modal
    if (activeModal == "dosha") {
        DoshaQuizModal(onDismiss = { activeModal = null })
    }

    // Water Tracker Modal
    if (activeModal == "water") {
        WaterTrackerModal(
            repository = repository,
            onDismiss = { activeModal = null }
        )
    }

    // Academic Syllabus Scope Modal (CSE3142)
    if (activeModal == "syllabus") {
        SyllabusScopeModal(onDismiss = { activeModal = null })
    }

    // Native JSON Diet Guide Modal
    if (activeModal == "json_diet") {
        JsonDietGuideModal(
            repository = repository,
            onDismiss = { activeModal = null }
        )
    }

    // Sleep or Streak Simple Dialogs
    if (activeModal == "sleep" || activeModal == "streak" || activeModal == "planner") {
        AlertDialog(
            onDismissRequest = { activeModal = null },
            title = { Text(text = "Ayurvedic Tool Active") },
            text = {
                Text(
                    text = when (activeModal) {
                        "sleep" -> "Circadian Rhythm Guide: Align your sleep from 10:00 PM to 6:00 AM (Kapha cycle) for deepest cellular regeneration."
                        "streak" -> "Streak Policy: Active challenge participation (e.g. Winter Arc or 75 Hard) completely protects your history from the 28-day cleanup rule."
                        else -> "Sattvic Meal Schedule: Emphasizes light breakfast, hearty digestion lunch during peak sun (12-2 PM), and easy-to-digest dinner before 8 PM."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = { activeModal = null },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text("Understood")
                }
            }
        )
    }
}

@Composable
private fun BmiCalculatorModal(onDismiss: () -> Unit) {
    var heightCm by remember { mutableStateOf("172") }
    var weightKg by remember { mutableStateOf("68") }

    val h = heightCm.toFloatOrNull() ?: 170f
    val w = weightKg.toFloatOrNull() ?: 70f
    val bmi = if (h > 0) w / ((h / 100f) * (h / 100f)) else 22f

    // South Asian WHO adjusted cutoffs
    val category = when {
        bmi < 18.0f -> "Underweight" to OceanBlue
        bmi < 23.0f -> "Normal (South Asian Target)" to SuccessGreen
        bmi < 27.5f -> "Overweight (South Asian Cutoff)" to WarningAmber
        else -> "Obese" to RingBurnRed
    }

    Dialog(onDismissRequest = onDismiss) {
        SpatialGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "South Asian BMI Calculator", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = heightCm,
                        onValueChange = { heightCm = it },
                        label = { Text("Height (cm)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { weightKg = it },
                        label = { Text("Weight (kg)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = category.second.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "YOUR BMI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = category.second)
                        Text(text = "%.1f".format(bmi), fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = category.second)
                        Text(text = category.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = category.second)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "South Asian Thresholds: Normal is 18 - 22.9 BMI. Keeping BMI under 23 reduces visceral fat and metabolic risk.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DoshaQuizModal(onDismiss: () -> Unit) {
    var qIndex by remember { mutableIntStateOf(0) }
    var vataScore by remember { mutableIntStateOf(0) }
    var pittaScore by remember { mutableIntStateOf(0) }
    var kaphaScore by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val questions = listOf(
        "What is your typical body frame?" to listOf("Slim, light, narrow bones (Vata)", "Medium, athletic, muscular (Pitta)", "Solid, sturdy, broad frame (Kapha)"),
        "How is your skin quality?" to listOf("Dry, cool, tends to chapping (Vata)", "Warm, reddish, prone to sunburn (Pitta)", "Thick, soft, naturally moisturized (Kapha)"),
        "How do you respond under stress?" to listOf("Anxiety, overthinking, restless (Vata)", "Irritation, intensity, impatience (Pitta)", "Withdrawal, calm, resistance to change (Kapha)")
    )

    Dialog(onDismissRequest = onDismiss) {
        SpatialGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ayurvedic Dosha Assessment", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isFinished) {
                    val q = questions[qIndex]
                    Text(text = "Question ${qIndex + 1} of ${questions.size}", fontSize = 11.sp, color = PrimarySaffron, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = q.first, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    q.second.forEachIndexed { optIndex, text ->
                        Button(
                            onClick = {
                                if (optIndex == 0) vataScore++
                                if (optIndex == 1) pittaScore++
                                if (optIndex == 2) kaphaScore++

                                if (qIndex < questions.size - 1) {
                                    qIndex++
                                } else {
                                    isFinished = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(text = text, fontSize = 12.sp)
                        }
                    }
                } else {
                    val dominant = when {
                        pittaScore >= vataScore && pittaScore >= kaphaScore -> "Pitta (Fire & Water)" to PrimarySaffron
                        vataScore >= pittaScore && vataScore >= kaphaScore -> "Vata (Air & Space)" to OceanBlue
                        else -> "Kapha (Earth & Water)" to SageGreen
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = dominant.second.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "DOMINANT CONSTITUTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = dominant.second)
                            Text(text = dominant.first, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = dominant.second)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Guidance: Prioritize warm, lightly spiced, Sattvic foods and gentle daily pranayama to balance internal Agni.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                    ) {
                        Text("Save to Profile")
                    }
                }
            }
        }
    }
}

@Composable
private fun WaterTrackerModal(
    repository: FitLifeRepository,
    onDismiss: () -> Unit
) {
    val todaySummary by repository.todaySummary.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        SpatialGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ushapan Water Tracker", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "${todaySummary.waterMl} / ${todaySummary.waterGoal} ml",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OceanBlue
                )
                Text(
                    text = "${(todaySummary.waterPercent * 100).toInt()}% of daily goal",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { repository.addWater(250) },
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                    ) {
                        Text("+250 ml")
                    }
                    Button(
                        onClick = { repository.addWater(500) },
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                    ) {
                        Text("+500 ml")
                    }
                }
            }
        }
    }
}

@Composable
private fun SyllabusScopeModal(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        SpatialGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CSE3142 Syllabus Scope",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Manipal University Jaipur • B.Tech V Sem",
                            fontSize = 11.sp,
                            color = PrimarySaffron,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                var showXmlViewDemo by remember { mutableStateOf(false) }

                CourseOutcomeItem(
                    code = "CO1",
                    title = "Android Architecture & Lifecycle",
                    lecture = "Lec 1-4",
                    description = "Android OS layered architecture (Linux kernel, HAL, ART, Framework, Apps), Gradle build system, Android Project Structure, and Activity lifecycle management in MainActivity."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CourseOutcomeItem(
                    code = "CO2",
                    title = "UI Components, XML Layouts & Events",
                    lecture = "Lec 5-12",
                    description = "XML UI design, View & ViewGroups (LinearLayout, RelativeLayout), Common UI Widgets (TextView, EditText, Button), and Event Listeners with user input validation. Also incorporates modern Jetpack Compose spatial glassmorphism."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CourseOutcomeItem(
                    code = "CO3",
                    title = "Activities, Fragments & Intents",
                    lecture = "Lec 13-17",
                    description = "Activities & state management, Fragment lifecycle & back stack navigation (FitLifeBaseFragment, VitalityFragment), Bundle arguments, and Implicit Intents (Intent.ACTION_SEND with system chooser) & Explicit Intents."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CourseOutcomeItem(
                    code = "CO4",
                    title = "Data Persistence: Prefs, SQLite & Files",
                    lecture = "Lec 18-22",
                    description = "Triple persistence architecture: SharedPreferences (fitlife_prefs) for key-value user settings, SQLiteOpenHelper (FitLifeLocalDatabase) with 4-week privacy auto-pruning, and Internal File Storage (FileStorageHelper) via openFileOutput/openFileInput."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CourseOutcomeItem(
                    code = "CO5",
                    title = "Advanced: Services, Sensors & JSON",
                    lecture = "Lec 23-28",
                    description = "FitLifeVitalityService (Android Service lifecycle), PowerConnectionReceiver (BroadcastReceiver), SensorManager (hardware Accelerometer + GPS fusion), Android NotificationChannel, and native org.json API parser."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive XML View demo toggle (Lec 5-12)
                Button(
                    onClick = { showXmlViewDemo = !showXmlViewDemo },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Text(
                        text = if (showXmlViewDemo) "Hide Native XML View Demo" else "Inspect Native XML Layout & Widgets (Lec 5-12)",
                        fontSize = 12.sp
                    )
                }

                if (showXmlViewDemo) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1E1E1E),
                        border = BorderStroke(1.dp, Color(0xFF333333))
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                LayoutInflater.from(ctx).inflate(R.layout.layout_syllabus_academic_card, null).apply {
                                    val noteEt = findViewById<AndroidEditText>(R.id.et_academic_quick_note)
                                    val saveBtn = findViewById<AndroidButton>(R.id.btn_academic_save_note)
                                    val descTv = findViewById<AndroidTextView>(R.id.tv_syllabus_desc)

                                    saveBtn.setOnClickListener {
                                        val text = noteEt.text.toString()
                                        if (text.isNotBlank()) {
                                            descTv.text = "✓ Event Listener captured: \"$text\""
                                        } else {
                                            descTv.text = "Please enter text before clicking button."
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text("Close Academic Scope")
                }
            }
        }
    }
}

@Composable
private fun CourseOutcomeItem(
    code: String,
    title: String,
    lecture: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PrimarySaffron.copy(alpha = 0.18f)
                    ) {
                        Text(
                            text = code,
                            color = PrimarySaffron,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = lecture,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun JsonDietGuideModal(
    repository: FitLifeRepository,
    onDismiss: () -> Unit
) {
    val guides = remember { repository.getDietaryGuidelines() }
    val guide = guides.firstOrNull() ?: AyurvedicDietaryTip(
        season = "Sharad (Autumn)",
        doshaBalancing = "Pitta Pacifying",
        recommendedFoods = listOf("Ghee", "Basmati Rice", "Moong Dal"),
        avoidingFoods = listOf("Pungent Spices", "Fermented Foods"),
        tip = "Consume cooling foods to calm seasonal heat."
    )
    var showRawJson by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        SpatialGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ayurvedic JSON Guide",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Parsed using native org.json (CO5)",
                            fontSize = 11.sp,
                            color = OceanBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = OceanBlue.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Season: ${guide.season}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OceanBlue
                        )
                        Text(
                            text = guide.tip,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target Dosha: ${guide.doshaBalancing}",
                            fontSize = 11.sp,
                            color = SageGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Foods to Favor (Pill Array):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    guide.recommendedFoods.take(3).forEach { food ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "✓ $food",
                                color = SuccessGreen,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Foods to Moderate / Avoid:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    guide.avoidingFoods.take(3).forEach { food ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RingBurnRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "✕ $food",
                                color = RingBurnRed,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(
                    onClick = { showRawJson = !showRawJson },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (showRawJson) "Hide Raw JSON Output" else "Inspect Raw JSON Response (CO5)",
                        fontSize = 11.sp,
                        color = OceanBlue
                    )
                }

                if (showRawJson) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = """
                            {
                              "status": "success",
                              "season": "${guide.season}",
                              "dosha_balance": "${guide.doshaBalancing}",
                              "favor": ${guide.recommendedFoods.joinToString(prefix = "[\"", separator = "\", \"", postfix = "\"]")},
                              "avoid": ${guide.avoidingFoods.joinToString(prefix = "[\"", separator = "\", \"", postfix = "\"]")}
                            }
                            """.trimIndent(),
                            fontSize = 10.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            modifier = Modifier.padding(10.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Text("Close JSON Guide")
                }
            }
        }
    }
}

