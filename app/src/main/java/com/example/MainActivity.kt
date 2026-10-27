package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.data.repository.FitLifeRepository
import com.example.sensor.StepFusionManager
import com.example.ui.navigation.MainAppNavigation
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.FitLifeIndiaTheme

class MainActivity : FragmentActivity() {

    private lateinit var repository: FitLifeRepository
    private var stepFusionManager: StepFusionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = FitLifeRepository(applicationContext)

        setContent {
            var currentThemeMode by remember { mutableStateOf(repository.preferences.themeMode) }

            // Permission Request on Launch (Location, Physical Activity, Notifications)
            val permissionsToRequest = remember {
                val list = mutableListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    list.add(Manifest.permission.ACTIVITY_RECOGNITION)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    list.add(Manifest.permission.POST_NOTIFICATIONS)
                }
                list.toTypedArray()
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { _ ->
                startStepSensor()
            }

            LaunchedEffect(Unit) {
                val hasFineLocation = ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (!hasFineLocation) {
                    permissionLauncher.launch(permissionsToRequest)
                } else {
                    startStepSensor()
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    stepFusionManager?.stopListening()
                }
            }

            FitLifeIndiaTheme(
                themeMode = currentThemeMode,
                onThemeModeChange = { newMode ->
                    currentThemeMode = newMode
                    repository.preferences.themeMode = newMode
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppNavigation(repository = repository)
                }
            }
        }
    }

    private fun startStepSensor() {
        if (stepFusionManager == null) {
            stepFusionManager = StepFusionManager(applicationContext) { stepDelta, burnDelta ->
                repository.addStepsAndBurn(stepDelta, burnDelta)
            }
            stepFusionManager?.startListening()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stepFusionManager?.stopListening()
    }
}
