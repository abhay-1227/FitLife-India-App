package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlin.math.sqrt

class StepFusionManager(
    private val context: Context,
    private val onStepDetected: (stepDelta: Int, calorieBurnDelta: Int) -> Unit
) : SensorEventListener, LocationListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null

    private var initialHardwareSteps: Float = -1f
    private var lastRecordedSteps: Int = 0

    // Accelerometer filter & peak-valley fusion state
    private var lastMagnitude = 9.8f
    private var isPeak = false
    private var lastStepTimestamp = 0L
    private val minStepIntervalMs = 280L // ~3.5 Hz max human cadence
    private val stepThreshold = 1.65f     // Dynamic acceleration delta above gravity

    // GPS speed sanity check
    private var currentSpeedKmh: Float = 0f

    fun startListening() {
        if (sensorManager == null) return

        // 1. Try Hardware Step Detector / Counter first
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI)
        } else if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        }

        // 2. Register Accelerometer for sensor fusion and peak verification
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }

        // 3. Location GPS sanity listener
        try {
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L,
                    5f,
                    this
                )
            }
        } catch (_: SecurityException) {
            // Handled gracefully if permission not yet granted
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
        try {
            locationManager?.removeUpdates(this)
        } catch (_: SecurityException) {}
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_DETECTOR -> {
                // Direct hardware step detector trigger
                if (currentSpeedKmh < 22f) { // Reject vehicle false positives
                    val burn = 1 // ~0.045 kcal, batch or count 1 cal per 20 steps
                    onStepDetected(1, if ((lastRecordedSteps % 20) == 0) 1 else 0)
                    lastRecordedSteps++
                }
            }
            Sensor.TYPE_STEP_COUNTER -> {
                val totalSteps = event.values[0]
                if (initialHardwareSteps < 0) {
                    initialHardwareSteps = totalSteps
                } else {
                    val delta = (totalSteps - (initialHardwareSteps + lastRecordedSteps)).toInt()
                    if (delta > 0 && currentSpeedKmh < 22f) {
                        lastRecordedSteps += delta
                        val burnDelta = (delta * 0.045f).toInt().coerceAtLeast(1)
                        onStepDetected(delta, burnDelta)
                    }
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                // Hybrid fallback algorithm when hardware step counter is absent or stationary
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)
                val delta = magnitude - 9.81f
                val now = System.currentTimeMillis()

                if (delta > stepThreshold && !isPeak && (now - lastStepTimestamp > minStepIntervalMs)) {
                    if (currentSpeedKmh < 22f) {
                        isPeak = true
                        lastStepTimestamp = now
                        lastRecordedSteps++
                        val burn = if (lastRecordedSteps % 22 == 0) 1 else 0
                        onStepDetected(1, burn)
                    }
                } else if (delta < -0.5f) {
                    isPeak = false
                }
                lastMagnitude = magnitude
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onLocationChanged(location: Location) {
        if (location.hasSpeed()) {
            currentSpeedKmh = location.speed * 3.6f
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
