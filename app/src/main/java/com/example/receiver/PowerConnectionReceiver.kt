package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.local.PreferencesHelper

/**
 * System Broadcast Receiver (CSE3142 CO5 / Lecture 26)
 * Demonstrates handling system-wide broadcast intents for power connectivity.
 */
class PowerConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val prefs = PreferencesHelper(context)
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                prefs.batterySaverMode = false
                Toast.makeText(
                    context,
                    "⚡ Power Connected: Step sensor operating in high-precision mode",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                prefs.batterySaverMode = true
                Toast.makeText(
                    context,
                    "🔋 On Battery: Sensor fusion tuned for optimal energy efficiency",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
