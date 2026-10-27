package com.example.data.remote

import org.json.JSONArray
import org.json.JSONObject

data class AyurvedicDietaryTip(
    val season: String,
    val doshaBalancing: String,
    val recommendedFoods: List<String>,
    val avoidingFoods: List<String>,
    val tip: String
)

/**
 * JSON Parsing Engine (CSE3142 CO5 / Lecture 23)
 * Demonstrates parsing web API JSON responses and raw JSON payloads using JSONObject & JSONArray.
 */
object JsonNutritionParser {

    private const val RAW_JSON_DATA = """
    {
      "status": "success",
      "version": "2.4.0",
      "guidelines": [
        {
          "season": "Sharad (Autumn)",
          "dosha": "Pitta Pacifying",
          "recommended": ["Ghee", "Basmati Rice", "Sweet Fruits", "Moong Dal", "Coconut Water"],
          "avoid": ["Pungent Spices", "Fermented Foods", "Excessive Mustard Oil"],
          "tip": "Consume cooling foods to calm seasonal heat accumulation in the liver and blood."
        },
        {
          "season": "Hemanta & Shishira (Winter)",
          "dosha": "Vata & Kapha Balancing",
          "recommended": ["Warm Sesame Oil", "Hot Spiced Soups", "Bajra & Jowar Roti", "Nuts & Dates"],
          "avoid": ["Cold Smoothies", "Raw Salads", "Iced Water"],
          "tip": "Stoke digestive Agni with warming spices like ginger, cumin, and cinnamon."
        },
        {
          "season": "Grishma (Summer)",
          "dosha": "Pitta & Vata Soothing",
          "recommended": ["Amla Juice", "Watermelon", "Cucumber", "Sattu Sharbat", "Buttermilk with Cumin"],
          "avoid": ["Heavy Fried Foods", "Excess Garlic", "Vinegar"],
          "tip": "Hydrate with natural electrolytes and take light lunches during peak midday sun."
        }
      ]
    }
    """

    fun parseDietaryGuidelines(): List<AyurvedicDietaryTip> {
        val list = mutableListOf<AyurvedicDietaryTip>()
        try {
            val rootObj = JSONObject(RAW_JSON_DATA)
            val guidelinesArray = rootObj.getJSONArray("guidelines")

            for (i in 0 until guidelinesArray.length()) {
                val itemObj = guidelinesArray.getJSONObject(i)
                val season = itemObj.getString("season")
                val dosha = itemObj.getString("dosha")
                val tip = itemObj.getString("tip")

                val recArray = itemObj.getJSONArray("recommended")
                val recList = mutableListOf<String>()
                for (j in 0 until recArray.length()) {
                    recList.add(recArray.getString(j))
                }

                val avoidArray = itemObj.getJSONArray("avoid")
                val avoidList = mutableListOf<String>()
                for (k in 0 until avoidArray.length()) {
                    avoidList.add(avoidArray.getString(k))
                }

                list.add(
                    AyurvedicDietaryTip(
                        season = season,
                        doshaBalancing = dosha,
                        recommendedFoods = recList,
                        avoidingFoods = avoidList,
                        tip = tip
                    )
                )
            }
        } catch (_: Exception) {
            // Graceful fallback
        }
        return list
    }
}
