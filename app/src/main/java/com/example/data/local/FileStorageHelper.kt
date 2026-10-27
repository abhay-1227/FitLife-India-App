package com.example.data.local

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * File Storage Implementation (CSE3142 CO4 / Lecture 21)
 * Demonstrates internal file storage, file streams (FileInputStream/FileOutputStream),
 * and local manifest export.
 */
object FileStorageHelper {

    private const val EXPORT_FILENAME = "fitlife_telemetry_export.json"

    fun writeTelemetryToFile(context: Context, jsonPayload: String): Boolean {
        return try {
            context.openFileOutput(EXPORT_FILENAME, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(jsonPayload.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readTelemetryFromFile(context: Context): String? {
        val file = File(context.filesDir, EXPORT_FILENAME)
        if (!file.exists()) return null

        return try {
            context.openFileInput(EXPORT_FILENAME).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    reader.readText()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getExportFileSize(context: Context): Long {
        val file = File(context.filesDir, EXPORT_FILENAME)
        return if (file.exists()) file.length() else 0L
    }

    fun generateExportJson(date: String, steps: Int, calories: Int, waterMl: Int): String {
        val json = JSONObject()
        json.put("app", "FitLife India")
        json.put("export_date", date)
        json.put("storage_type", "INTERNAL_FILE_STORAGE")
        json.put("steps", steps)
        json.put("calories_burned", calories)
        json.put("water_intake_ml", waterMl)
        json.put("syllabus_alignment", "CSE3142_Lecture_21_File_Storage")
        return json.toString(2)
    }
}
