package com.example.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.time.LocalDate

data class UserProfile(
    val id: Long = 0,
    val email: String,
    val name: String,
    val authType: String, // "LOCAL" or "GOOGLE"
    val isLoggedIn: Boolean = true
)

data class DailySummary(
    val date: String,
    val steps: Int = 0,
    val stepGoal: Int = 10000,
    val caloriesConsumed: Int = 0,
    val calorieGoal: Int = 2000,
    val caloriesBurned: Int = 0,
    val burnGoal: Int = 500,
    val waterMl: Int = 0,
    val waterGoal: Int = 2500,
    val sleepHours: Float = 0f
) {
    val stepsPercent: Float get() = if (stepGoal > 0) (steps.toFloat() / stepGoal).coerceAtMost(1.5f) else 0f
    val caloriesPercent: Float get() = if (calorieGoal > 0) (caloriesConsumed.toFloat() / calorieGoal).coerceAtMost(1.5f) else 0f
    val burnPercent: Float get() = if (burnGoal > 0) (caloriesBurned.toFloat() / burnGoal).coerceAtMost(1.5f) else 0f
    val waterPercent: Float get() = if (waterGoal > 0) (waterMl.toFloat() / waterGoal).coerceAtMost(1.5f) else 0f
}

data class MealRecord(
    val id: Long = 0,
    val date: String,
    val foodName: String,
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatsG: Float,
    val mealType: String,
    val timeFormatted: String
)

data class SpecialChallengeRecord(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val startDate: String,
    val targetDays: Int,
    val currentStreak: Int
)

class FitLifeLocalDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "fitlife_india_local.db"
        const val DATABASE_VERSION = 2

        private fun hashPassword(password: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                password_hash TEXT,
                name TEXT NOT NULL,
                auth_type TEXT NOT NULL,
                created_at INTEGER NOT NULL
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE daily_metrics (
                date TEXT PRIMARY KEY,
                steps INTEGER DEFAULT 0,
                step_goal INTEGER DEFAULT 10000,
                calories_consumed INTEGER DEFAULT 0,
                calorie_goal INTEGER DEFAULT 2000,
                calories_burned INTEGER DEFAULT 0,
                burn_goal INTEGER DEFAULT 500,
                water_ml INTEGER DEFAULT 0,
                water_goal INTEGER DEFAULT 2500,
                sleep_hours REAL DEFAULT 7.5,
                updated_at INTEGER NOT NULL
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE meals (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                food_name TEXT NOT NULL,
                calories INTEGER NOT NULL,
                protein REAL NOT NULL,
                carbs REAL NOT NULL,
                fats REAL NOT NULL,
                meal_type TEXT NOT NULL,
                time_formatted TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE challenges (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                is_active INTEGER DEFAULT 0,
                start_date TEXT,
                target_days INTEGER NOT NULL,
                current_streak INTEGER DEFAULT 0
            );
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE settings (
                key TEXT PRIMARY KEY,
                value TEXT NOT NULL
            );
            """.trimIndent()
        )

        // Seed default challenges: Winter Arc and 75 Hard
        db.execSQL(
            """
            INSERT OR IGNORE INTO challenges (id, name, description, is_active, start_date, target_days, current_streak)
            VALUES 
            ('winter_arc', 'Winter Arc', 'Discipline through cold months. 5am waking, intense sadhana & zero junk food.', 1, date('now', '-5 days'), 90, 6),
            ('75_hard', '75 Hard Challenge', 'Two 45-min workouts daily, clean diet, 4L water, 10 pages reading.', 0, '', 75, 0),
            ('sattvic_21', '21 Days Sattvic Reset', 'Pure vegetarian Ayurvedic diet, daily Surya Namaskar and mindfulness.', 0, '', 21, 0);
            """.trimIndent()
        )

        // Seed seed user & initial 7 days of realistic Indian wellness metrics
        seedInitialWellnessData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS challenges")
        db.execSQL("DROP TABLE IF EXISTS daily_metrics")
        db.execSQL("DROP TABLE IF EXISTS meals")
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS settings")
        onCreate(db)
    }

    private fun seedInitialWellnessData(db: SQLiteDatabase) {
        val today = LocalDate.now()
        // Default local demo user
        val userValues = ContentValues().apply {
            put("email", "arjun.sharma@fitlife.in")
            put("password_hash", hashPassword("fitlife123"))
            put("name", "Arjun Sharma")
            put("auth_type", "LOCAL")
            put("created_at", System.currentTimeMillis())
        }
        db.insert("users", null, userValues)

        // Seed last 7 days of metrics
        val sampleDays = listOf(
            Triple(8420, 1850, 480),
            Triple(10150, 2100, 560),
            Triple(7300, 1720, 390),
            Triple(11200, 2250, 610),
            Triple(9400, 1980, 510),
            Triple(12400, 2050, 640),
            Triple(9850, 1920, 530)
        )

        sampleDays.forEachIndexed { index, (steps, cals, burn) ->
            val dayDate = today.minusDays((6 - index).toLong()).toString()
            val water = 2200 + (index * 80)
            val cv = ContentValues().apply {
                put("date", dayDate)
                put("steps", steps)
                put("step_goal", 10000)
                put("calories_consumed", cals)
                put("calorie_goal", 2000)
                put("calories_burned", burn)
                put("burn_goal", 500)
                put("water_ml", water)
                put("water_goal", 2500)
                put("sleep_hours", 7.2f + (index % 2) * 0.4f)
                put("updated_at", System.currentTimeMillis())
            }
            db.insertWithOnConflict("daily_metrics", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        // Seed today's authentic Indian meals
        val todayStr = today.toString()
        val meals = listOf(
            MealRecord(0, todayStr, "Idli with Sambhar & Mint Chutney", 320, 12f, 54f, 6f, "Breakfast", "08:15 AM"),
            MealRecord(0, todayStr, "Masala Spiced Green Tea & Roasted Chana", 140, 6f, 22f, 3f, "Morning Snack", "11:00 AM"),
            MealRecord(0, todayStr, "Dal Tadka, 2 Roti & Paneer Tikka Salad", 620, 28f, 78f, 20f, "Lunch", "01:30 PM"),
            MealRecord(0, todayStr, "Tender Coconut Water & Makhana", 180, 4f, 32f, 4f, "Evening", "05:00 PM"),
            MealRecord(0, todayStr, "Moong Dal Khichdi with Ghee & Curd", 480, 18f, 65f, 14f, "Dinner", "08:00 PM")
        )

        meals.forEach { meal ->
            val mCv = ContentValues().apply {
                put("date", meal.date)
                put("food_name", meal.foodName)
                put("calories", meal.calories)
                put("protein", meal.proteinG)
                put("carbs", meal.carbsG)
                put("fats", meal.fatsG)
                put("meal_type", meal.mealType)
                put("time_formatted", meal.timeFormatted)
                put("timestamp", System.currentTimeMillis())
            }
            db.insert("meals", null, mCv)
        }
    }

    // 4-Week Data Retention Policy
    fun enforceDataRetentionPolicy(today: LocalDate = LocalDate.now()): Int {
        val db = writableDatabase
        // Check if user is currently performing a special challenge (Winter Arc, 75 Hard)
        val cursor = db.rawQuery("SELECT COUNT(*) FROM challenges WHERE is_active = 1", null)
        var hasActiveChallenge = false
        if (cursor.moveToFirst()) {
            hasActiveChallenge = cursor.getInt(0) > 0
        }
        cursor.close()

        if (hasActiveChallenge) {
            // Protected: user is doing a challenge; keep complete history!
            return 0
        }

        val cutoffDate = today.minusDays(28).toString()
        val deletedMetrics = db.delete("daily_metrics", "date < ?", arrayOf(cutoffDate))
        db.delete("meals", "date < ?", arrayOf(cutoffDate))
        return deletedMetrics
    }

    // User Authentication: Register
    fun registerLocalUser(email: String, password: String, name: String): UserProfile? {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("email", email.trim().lowercase())
            put("password_hash", hashPassword(password))
            put("name", name.trim())
            put("auth_type", "LOCAL")
            put("created_at", System.currentTimeMillis())
        }
        val id = db.insertWithOnConflict("users", null, cv, SQLiteDatabase.CONFLICT_IGNORE)
        return if (id > 0) {
            UserProfile(id, email, name, "LOCAL", true)
        } else {
            null
        }
    }

    // User Authentication: Login
    fun loginLocalUser(email: String, password: String): UserProfile? {
        val db = readableDatabase
        val hash = hashPassword(password)
        val cursor = db.rawQuery(
            "SELECT id, email, name, auth_type FROM users WHERE email = ? AND password_hash = ?",
            arrayOf(email.trim().lowercase(), hash)
        )
        return if (cursor.moveToFirst()) {
            val user = UserProfile(
                id = cursor.getLong(0),
                email = cursor.getString(1),
                name = cursor.getString(2),
                authType = cursor.getString(3),
                isLoggedIn = true
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    // User Authentication: Google Sign-in simulation / integration
    fun loginWithGoogle(email: String, name: String): UserProfile {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("email", email.trim().lowercase())
            put("name", name.trim())
            put("auth_type", "GOOGLE")
            put("created_at", System.currentTimeMillis())
        }
        db.insertWithOnConflict("users", null, cv, SQLiteDatabase.CONFLICT_IGNORE)
        return UserProfile(0, email, name, "GOOGLE", true)
    }

    // Metrics CRUD
    fun getDailySummary(date: String = LocalDate.now().toString()): DailySummary {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT steps, step_goal, calories_consumed, calorie_goal, calories_burned, burn_goal, water_ml, water_goal, sleep_hours FROM daily_metrics WHERE date = ?", arrayOf(date))
        return if (cursor.moveToFirst()) {
            val summary = DailySummary(
                date = date,
                steps = cursor.getInt(0),
                stepGoal = cursor.getInt(1),
                caloriesConsumed = cursor.getInt(2),
                calorieGoal = cursor.getInt(3),
                caloriesBurned = cursor.getInt(4),
                burnGoal = cursor.getInt(5),
                waterMl = cursor.getInt(6),
                waterGoal = cursor.getInt(7),
                sleepHours = cursor.getFloat(8)
            )
            cursor.close()
            summary
        } else {
            cursor.close()
            DailySummary(date)
        }
    }

    fun updateStepsAndBurn(date: String, steps: Int, caloriesBurned: Int) {
        val db = writableDatabase
        val existing = getDailySummary(date)
        val cv = ContentValues().apply {
            put("date", date)
            put("steps", steps)
            put("calories_burned", caloriesBurned)
            put("calories_consumed", existing.caloriesConsumed)
            put("water_ml", existing.waterMl)
            put("updated_at", System.currentTimeMillis())
        }
        db.insertWithOnConflict("daily_metrics", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun addWater(date: String, amountMl: Int) {
        val db = writableDatabase
        val current = getDailySummary(date)
        val newWater = current.waterMl + amountMl
        val cv = ContentValues().apply {
            put("date", date)
            put("water_ml", newWater)
            put("updated_at", System.currentTimeMillis())
        }
        db.insertWithOnConflict("daily_metrics", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun logMeal(meal: MealRecord) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("date", meal.date)
            put("food_name", meal.foodName)
            put("calories", meal.calories)
            put("protein", meal.proteinG)
            put("carbs", meal.carbsG)
            put("fats", meal.fatsG)
            put("meal_type", meal.mealType)
            put("time_formatted", meal.timeFormatted)
            put("timestamp", System.currentTimeMillis())
        }
        db.insert("meals", null, cv)

        // Increment today's calories consumed
        val current = getDailySummary(meal.date)
        val newCals = current.caloriesConsumed + meal.calories
        val updateCv = ContentValues().apply {
            put("date", meal.date)
            put("calories_consumed", newCals)
            put("updated_at", System.currentTimeMillis())
        }
        db.insertWithOnConflict("daily_metrics", null, updateCv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getTodayMeals(date: String = LocalDate.now().toString()): List<MealRecord> {
        val db = readableDatabase
        val list = mutableListOf<MealRecord>()
        val cursor = db.rawQuery("SELECT id, date, food_name, calories, protein, carbs, fats, meal_type, time_formatted FROM meals WHERE date = ? ORDER BY id DESC", arrayOf(date))
        while (cursor.moveToNext()) {
            list.add(
                MealRecord(
                    id = cursor.getLong(0),
                    date = cursor.getString(1),
                    foodName = cursor.getString(2),
                    calories = cursor.getInt(3),
                    proteinG = cursor.getFloat(4),
                    carbsG = cursor.getFloat(5),
                    fatsG = cursor.getFloat(6),
                    mealType = cursor.getString(7),
                    timeFormatted = cursor.getString(8)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getWeeklyMetrics(): List<DailySummary> {
        val today = LocalDate.now()
        val list = mutableListOf<DailySummary>()
        for (i in 6 downTo 0) {
            val d = today.minusDays(i.toLong()).toString()
            list.add(getDailySummary(d))
        }
        return list
    }

    // Challenges
    fun getChallenges(): List<SpecialChallengeRecord> {
        val db = readableDatabase
        val list = mutableListOf<SpecialChallengeRecord>()
        val cursor = db.rawQuery("SELECT id, name, description, is_active, start_date, target_days, current_streak FROM challenges", null)
        while (cursor.moveToNext()) {
            list.add(
                SpecialChallengeRecord(
                    id = cursor.getString(0),
                    name = cursor.getString(1),
                    description = cursor.getString(2),
                    isActive = cursor.getInt(3) == 1,
                    startDate = cursor.getString(4) ?: "",
                    targetDays = cursor.getInt(5),
                    currentStreak = cursor.getInt(6)
                )
            )
        }
        cursor.close()
        return list
    }

    fun toggleChallenge(challengeId: String, activate: Boolean) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("is_active", if (activate) 1 else 0)
            if (activate) {
                put("start_date", LocalDate.now().toString())
            }
        }
        db.update("challenges", cv, "id = ?", arrayOf(challengeId))
    }
}
