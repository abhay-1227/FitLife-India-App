package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.theme.AppThemeMode

/**
 * SharedPreferences persistence layer (CSE3142 CO4 / Lecture 18)
 * Stores user preferences, notification toggles, and daily targets locally on device.
 */
class PreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "fitlife_preferences"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "key_notifications_enabled"
        private const val KEY_DAILY_STEP_GOAL = "key_daily_step_goal"
        private const val KEY_DAILY_WATER_GOAL = "key_daily_water_goal"
        private const val KEY_DAILY_CALORIE_GOAL = "key_daily_calorie_goal"
        private const val KEY_BATTERY_SAVER = "key_battery_saver_mode"
    }

    var themeMode: AppThemeMode
        get() {
            val name = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name
            return try {
                AppThemeMode.valueOf(name)
            } catch (_: Exception) {
                AppThemeMode.SYSTEM
            }
        }
        set(value) {
            prefs.edit().putString(KEY_THEME_MODE, value.name).apply()
        }

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var dailyStepGoal: Int
        get() = prefs.getInt(KEY_DAILY_STEP_GOAL, 10000)
        set(value) = prefs.edit().putInt(KEY_DAILY_STEP_GOAL, value).apply()

    var dailyWaterGoal: Int
        get() = prefs.getInt(KEY_DAILY_WATER_GOAL, 2500)
        set(value) = prefs.edit().putInt(KEY_DAILY_WATER_GOAL, value).apply()

    var dailyCalorieGoal: Int
        get() = prefs.getInt(KEY_DAILY_CALORIE_GOAL, 2000)
        set(value) = prefs.edit().putInt(KEY_DAILY_CALORIE_GOAL, value).apply()

    var batterySaverMode: Boolean
        get() = prefs.getBoolean(KEY_BATTERY_SAVER, false)
        set(value) = prefs.edit().putBoolean(KEY_BATTERY_SAVER, value).apply()
}
