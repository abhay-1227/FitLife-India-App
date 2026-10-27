package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.repository.FitLifeRepository
import com.example.ui.components.NeomorphicPill
import com.example.ui.components.SpatialGlassCard
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.RingBurnRed
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

data class AsanaItem(
    val id: String,
    val sanskritName: String,
    val englishName: String,
    val category: String, // Asanas, Pranayama, Meditation
    val level: String,    // Beginner, Intermediate, Advanced
    val durationMinutes: Int,
    val calorieBurn: Int,
    val benefits: List<String>,
    val instructions: List<String>
)

val AsanasDatabase = listOf(
    AsanaItem(
        "surya_namaskar",
        "Surya Namaskar",
        "Sun Salutations (12 Poses)",
        "Asanas",
        "Beginner",
        15,
        120,
        listOf("Strengthens spine & core", "Boosts circulation", "Improves mental clarity"),
        listOf("Stand tall in Pranamasana", "Inhale, arch into Hastauttanasana", "Exhale down to Padahastasana", "Step into Ashwa Sanchalanasana", "Flow through Dandasana & Ashtanga Namaskara")
    ),
    AsanaItem(
        "vrikshasana",
        "Vrikshasana",
        "Tree Pose",
        "Asanas",
        "Beginner",
        5,
        35,
        listOf("Improves physical balance", "Strengthens thighs & ankles", "Increases concentration"),
        listOf("Shift weight to left foot", "Place right foot against inner left thigh", "Bring palms together at heart center (Anjali Mudra)", "Gaze steadily at a fixed focal point (Drishti)")
    ),
    AsanaItem(
        "bhujangasana",
        "Bhujangasana",
        "Cobra Pose",
        "Asanas",
        "Beginner",
        8,
        45,
        listOf("Opens chest & heart", "Elevates digestive Agni", "Relieves spinal stiffness"),
        listOf("Lie prone on stomach", "Place palms directly beneath shoulders", "Inhale and lift chest gracefully while keeping navel grounded", "Breathe smoothly for 5 deep cycles")
    ),
    AsanaItem(
        "adho_mukha",
        "Adho Mukha Svanasana",
        "Downward-Facing Dog",
        "Asanas",
        "Intermediate",
        6,
        50,
        listOf("Energizes entire body", "Calms brain & relieves stress", "Stretches hamstrings"),
        listOf("Come to hands and knees", "Tuck toes and lift hips toward the sky", "Press palms firmly, lengthen spine", "Ground heels gently toward floor")
    ),
    AsanaItem(
        "nadi_shodhana",
        "Nadi Shodhana",
        "Alternate Nostril Breathing",
        "Pranayama",
        "Beginner",
        10,
        25,
        listOf("Balances Ida & Pingala nadis", "Reduces sympathetic nervous stress", "Harmonizes brain hemispheres"),
        listOf("Sit in comfortable Padmasana or Sukhasana", "Close right nostril with thumb, inhale through left", "Close left with ring finger, exhale right", "Inhale right, exhale left. Repeat 10 rounds")
    ),
    AsanaItem(
        "kapalbhati",
        "Kapalbhati",
        "Skull Shining Breath",
        "Pranayama",
        "Intermediate",
        8,
        40,
        listOf("Cleanses respiratory channels", "Stimulates abdominal organs", "Eliminates lethargy"),
        listOf("Sit erect with spine aligned", "Take a passive inhalation", "Exhale forcefully pulling navel inward", "Repeat rhythmic expulsion 60 times")
    ),
    AsanaItem(
        "dhyana_om",
        "Dhyana & Om Chanting",
        "Mindfulness Meditation",
        "Meditation",
        "All Levels",
        12,
        20,
        listOf("Transcends mental chatter", "Stimulates vagus nerve", "Cultivates profound inner peace"),
        listOf("Adopt Chin Mudra with hands resting on knees", "Close eyes gently, witness steady breathing", "Chant A-U-M allowing resonance to vibrate from navel to crown")
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaScreen(
    repository: FitLifeRepository
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedLevel by remember { mutableStateOf("All") }
    var selectedAsana by remember { mutableStateOf<AsanaItem?>(null) }
    var practicingAsana by remember { mutableStateOf<AsanaItem?>(null) }
    var favorites by remember { mutableStateOf(setOf("surya_namaskar", "nadi_shodhana")) }

    val filteredList = remember(selectedCategory, selectedLevel) {
        AsanasDatabase.filter { item ->
            val matchCat = selectedCategory == "All" || item.category.equals(selectedCategory, ignoreCase = true)
            val matchLevel = selectedLevel == "All" || item.level.equals(selectedLevel, ignoreCase = true) || item.level == "All Levels"
            matchCat && matchLevel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Yoga & Sadhana",
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
            // Category Tabs
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val cats = listOf("All", "Asanas", "Pranayama", "Meditation")
                    items(cats) { cat ->
                        NeomorphicPill(
                            text = cat,
                            isSelected = selectedCategory == cat,
                            accentColor = SageGreen,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }
            }

            // Level Filter Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Level:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    listOf("All", "Beginner", "Intermediate").forEach { level ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedLevel = level }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedLevel == level) SageGreen.copy(alpha = 0.2f) else Color.Transparent
                        ) {
                            Text(
                                text = level,
                                fontSize = 12.sp,
                                fontWeight = if (selectedLevel == level) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedLevel == level) SageGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Asanas Grid / Cards
            items(filteredList) { asana ->
                val isFav = favorites.contains(asana.id)
                SpatialGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedAsana = asana }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SageGreen.copy(alpha = 0.15f),
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SelfImprovement,
                                        contentDescription = null,
                                        tint = SageGreen,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = asana.sanskritName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = asana.englishName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    favorites = if (isFav) favorites - asana.id else favorites + asana.id
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFav) RingBurnRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = PrimarySaffron.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${asana.durationMinutes} min",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimarySaffron,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = RingBurnRed.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${asana.calorieBurn} kcal burn",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RingBurnRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = SageGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = asana.level,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SageGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { practicingAsana = asana },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SageGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Start Guided Practice", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Modal Bottom Sheet: Asana Details
    if (selectedAsana != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { selectedAsana = null },
            sheetState = sheetState
        ) {
            val asana = selectedAsana!!
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
                    Column {
                        Text(
                            text = asana.sanskritName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = asana.englishName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { selectedAsana = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Key Benefits:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                asana.benefits.forEach { b ->
                    Text(text = "• $b", fontSize = 13.sp, color = SageGreen, modifier = Modifier.padding(vertical = 2.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Step-by-Step Instructions:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                asana.instructions.forEachIndexed { i, step ->
                    Text(text = "${i + 1}. $step", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 2.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val toPractice = selectedAsana
                        selectedAsana = null
                        practicingAsana = toPractice
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimarySaffron,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Begin Practice (${asana.durationMinutes} min)", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Guided Practice Timer View
    if (practicingAsana != null) {
        val asana = practicingAsana!!
        PracticeTimerModal(
            asana = asana,
            onClose = { practicingAsana = null },
            onComplete = { burn ->
                repository.addStepsAndBurn(0, burn)
                practicingAsana = null
            }
        )
    }
}

@Composable
private fun PracticeTimerModal(
    asana: AsanaItem,
    onClose: () -> Unit,
    onComplete: (Int) -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(asana.durationMinutes * 60) }
    var isRunning by remember { mutableStateOf(true) }
    var breathPhase by remember { mutableStateOf("Inhale") }

    val infiniteTransition = rememberInfiniteTransition(label = "breath")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    LaunchedEffect(isRunning) {
        while (isRunning && secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
            breathPhase = if ((secondsLeft / 4) % 2 == 0) "Inhale (Prana)" else "Exhale (Apana)"
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = asana.sanskritName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Guided Practice", fontSize = 12.sp, color = SageGreen)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Exit")
                }
            }

            // Animated Breathing Lotus Center
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size((180 * breathScale).dp)
                        .clip(CircleShape)
                        .background(SageGreen.copy(alpha = 0.20f))
                )
                Box(
                    modifier = Modifier
                        .size((140 * breathScale).dp)
                        .clip(CircleShape)
                        .background(PrimarySaffron.copy(alpha = 0.25f))
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val mins = secondsLeft / 60
                    val secs = secondsLeft % 60
                    Text(
                        text = "%02d:%02d".format(mins, secs),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = breathPhase,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffron
                    )
                }
            }

            // Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrimarySaffron)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Button(
                        onClick = { onComplete(asana.calorieBurn) },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Complete & Log", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
