package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.local.DailySummary

/**
 * Android Intent Helper (CSE3142 CO3 / Lectures 14-15)
 * Demonstrates Implicit & Explicit Intents, Extras, and Android Chooser dialogs.
 */
object IntentHelper {

    /**
     * Implicit Intent (ACTION_SEND):
     * Shares today's Apple Fitness-style activity rings and nourishment metrics with external apps.
     */
    fun shareDailyProgress(context: Context, summary: DailySummary) {
        val shareText = """
            🔥 FitLife India Vitality Summary (${summary.date})
            ------------------------------------------------
            👣 Steps: ${summary.steps} / ${summary.stepGoal} (${(summary.stepsPercent * 100).toInt()}%)
            🍲 Calorie Intake: ${summary.caloriesConsumed} / ${summary.calorieGoal} kcal
            ⚡ Calorie Burn: ${summary.caloriesBurned} / ${summary.burnGoal} kcal
            💧 Water: ${summary.waterMl} / ${summary.waterGoal} ml
            
            Sadhana & Vitality logged with 100% on-device privacy!
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "My FitLife India Progress")
            type = "text/plain"
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share Vitality Progress via")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    /**
     * Implicit Intent (ACTION_VIEW):
     * Opens official Ayurvedic Ministry of AYUSH portal or wellness guidelines in browser.
     */
    fun openAyurvedaResource(context: Context) {
        val url = "https://ayush.gov.in"
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(viewIntent)
        } catch (_: Exception) {
            // Handled safely
        }
    }
}
