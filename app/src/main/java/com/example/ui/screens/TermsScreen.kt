package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SpatialGlassCard
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.PrimarySaffron
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terms & Conditions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Card
            SpatialGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoxIcon(icon = Icons.Default.Policy, color = PrimarySaffron)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "FitLife India Governance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Effective: 2026 • Local-First Architecture",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TermsSectionCard(
                icon = Icons.Default.Security,
                title = "1. Zero Cloud Data Leak & Local Storage",
                color = SuccessGreen,
                content = "FitLife India stores 100% of your personal profile, workout logs, nutrition records, and biometric measurements inside an encrypted local SQLite database directly on your mobile device. There are no tracking scripts, third-party analytics telemetry, or server-side cloud backups. Your health data remains strictly under your sovereign physical control."
            )

            Spacer(modifier = Modifier.height(12.dp))

            TermsSectionCard(
                icon = Icons.Default.LocationOn,
                title = "2. Sensor & Location Permissions",
                color = OceanBlue,
                content = "When first launched, the app requests Location and Physical Activity permissions solely to operate the hybrid step counting engine. The GPS sensor evaluates instantaneous velocity solely to distinguish genuine human walking strides from motorized transport (filtering out false positives above 22 km/h). Your GPS coordinates are never recorded, mapped, logged to disk, or transmitted."
            )

            Spacer(modifier = Modifier.height(12.dp))

            TermsSectionCard(
                icon = Icons.Default.Restore,
                title = "3. 4-Week Data Retention Policy",
                color = PrimarySaffron,
                content = "To preserve high-speed mobile performance and prevent unnecessary disk storage accumulation, daily summaries and individual meal records older than 28 days are automatically purged from the local database.\n\nEXCEPTION: If you actively enroll in a Special Challenge (such as Winter Arc or 75 Hard), this automatic 4-week pruning policy is immediately suspended for the duration of the challenge to protect your historical streak data."
            )

            Spacer(modifier = Modifier.height(12.dp))

            TermsSectionCard(
                icon = Icons.Default.Lock,
                title = "4. User Accounts & Google Sign-In",
                color = SageGreen,
                content = "You may register locally using an email and password or use the Google Sign-In button. Credentials registered via email are cryptographically hashed using SHA-256 and stored on-device. You can log in anytime offline without needing an active internet connection."
            )

            Spacer(modifier = Modifier.height(12.dp))

            TermsSectionCard(
                icon = Icons.Default.HealthAndSafety,
                title = "5. Holistic Ayurvedic Wellness Disclaimer",
                color = PrimarySaffron,
                content = "FitLife India combines classical Ayurvedic principles, Asana recommendations, and nutritional macronutrient guidelines to encourage a disciplined lifestyle. This application does not provide clinical medical diagnosis, prescription treatment, or emergency advice. Consult a certified medical practitioner or Ayurvedic Vaidya before commencing rigorous fasting or intense physical exertion."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "By using FitLife India, you agree to these transparent local-first terms. You maintain the right to delete all data at any time via the Profile settings.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TermsSectionCard(
    icon: ImageVector,
    title: String,
    color: Color,
    content: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = color.copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BoxIcon(icon: ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f),
        modifier = Modifier.size(44.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.padding(10.dp)
        )
    }
}
