package com.example.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Background Service Implementation (CSE3142 CO5 / Lectures 24-25)
 * Demonstrates Android Service lifecycle (onCreate, onStartCommand, onDestroy),
 * background task execution, and status notification triggers.
 */
class FitLifeVitalityService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_EVALUATE_GOALS) {
            serviceScope.launch {
                // Perform background verification of goals
                val stepCount = intent.getIntExtra(EXTRA_STEPS, 0)
                val stepGoal = intent.getIntExtra(EXTRA_STEP_GOAL, 10000)

                if (stepCount >= stepGoal && stepGoal > 0) {
                    FitLifeNotificationHelper.sendGoalCelebration(
                        applicationContext,
                        "Daily Step"
                    )
                }
                stopSelf(startId)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_EVALUATE_GOALS = "com.example.action.EVALUATE_GOALS"
        const val EXTRA_STEPS = "extra_steps"
        const val EXTRA_STEP_GOAL = "extra_step_goal"

        fun startGoalEvaluation(context: Context, currentSteps: Int, goal: Int) {
            val intent = Intent(context, FitLifeVitalityService::class.java).apply {
                action = ACTION_EVALUATE_GOALS
                putExtra(EXTRA_STEPS, currentSteps)
                putExtra(EXTRA_STEP_GOAL, goal)
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
