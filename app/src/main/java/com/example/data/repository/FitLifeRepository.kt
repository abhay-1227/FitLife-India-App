package com.example.data.repository

import android.content.Context
import com.example.data.local.DailySummary
import com.example.data.local.FitLifeLocalDatabase
import com.example.data.local.MealRecord
import com.example.data.local.PreferencesHelper
import com.example.data.local.SpecialChallengeRecord
import com.example.data.local.UserProfile
import com.example.data.remote.AyurvedicDietaryTip
import com.example.data.remote.JsonNutritionParser
import com.example.service.FitLifeNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class FitLifeRepository(private val context: Context) {
    private val db = FitLifeLocalDatabase(context)
    val preferences = PreferencesHelper(context)

    private val _currentUser = MutableStateFlow<UserProfile?>(
        UserProfile(1, "arjun.sharma@fitlife.in", "Arjun Sharma", "LOCAL", true)
    )
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _todaySummary = MutableStateFlow(db.getDailySummary())
    val todaySummary: StateFlow<DailySummary> = _todaySummary.asStateFlow()

    private val _todayMeals = MutableStateFlow(db.getTodayMeals())
    val todayMeals: StateFlow<List<MealRecord>> = _todayMeals.asStateFlow()

    private val _weeklyMetrics = MutableStateFlow(db.getWeeklyMetrics())
    val weeklyMetrics: StateFlow<List<DailySummary>> = _weeklyMetrics.asStateFlow()

    private val _challenges = MutableStateFlow(db.getChallenges())
    val challenges: StateFlow<List<SpecialChallengeRecord>> = _challenges.asStateFlow()

    init {
        // Enforce 4-week data cleanup on app initialization
        db.enforceDataRetentionPolicy()
        FitLifeNotificationHelper.createNotificationChannel(context)
    }

    fun refreshAll() {
        _todaySummary.value = db.getDailySummary()
        _todayMeals.value = db.getTodayMeals()
        _weeklyMetrics.value = db.getWeeklyMetrics()
        _challenges.value = db.getChallenges()
    }

    fun register(email: String, pass: String, name: String): Boolean {
        val user = db.registerLocalUser(email, pass, name)
        if (user != null) {
            _currentUser.value = user
            return true
        }
        return false
    }

    fun login(email: String, pass: String): Boolean {
        val user = db.loginLocalUser(email, pass)
        if (user != null) {
            _currentUser.value = user
            return true
        }
        return false
    }

    fun loginGoogle(email: String, name: String) {
        val user = db.loginWithGoogle(email, name)
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun addStepsAndBurn(stepDelta: Int, burnDelta: Int) {
        val current = _todaySummary.value
        val newSteps = current.steps + stepDelta
        val newBurn = current.caloriesBurned + burnDelta
        db.updateStepsAndBurn(current.date, newSteps, newBurn)

        if (current.steps < current.stepGoal && newSteps >= current.stepGoal && preferences.notificationsEnabled) {
            FitLifeNotificationHelper.sendGoalCelebration(context, "10,000 Steps")
        }
        refreshAll()
    }

    fun addWater(amountMl: Int) {
        val current = _todaySummary.value
        db.addWater(current.date, amountMl)
        if (current.waterMl < current.waterGoal && (current.waterMl + amountMl) >= current.waterGoal && preferences.notificationsEnabled) {
            FitLifeNotificationHelper.sendGoalCelebration(context, "Hydration")
        }
        refreshAll()
    }

    fun triggerHydrationReminderNotification() {
        FitLifeNotificationHelper.sendHydrationReminder(context)
    }

    fun logMeal(name: String, cals: Int, protein: Float, carbs: Float, fats: Float, type: String) {
        val record = MealRecord(
            date = LocalDate.now().toString(),
            foodName = name,
            calories = cals,
            proteinG = protein,
            carbsG = carbs,
            fatsG = fats,
            mealType = type,
            timeFormatted = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
        )
        db.logMeal(record)
        refreshAll()
    }

    fun toggleChallenge(id: String, active: Boolean) {
        db.toggleChallenge(id, active)
        refreshAll()
    }

    fun runRetentionCleanup(): Int {
        val count = db.enforceDataRetentionPolicy()
        refreshAll()
        return count
    }

    fun getDietaryGuidelines(): List<AyurvedicDietaryTip> {
        return JsonNutritionParser.parseDietaryGuidelines()
    }
}
