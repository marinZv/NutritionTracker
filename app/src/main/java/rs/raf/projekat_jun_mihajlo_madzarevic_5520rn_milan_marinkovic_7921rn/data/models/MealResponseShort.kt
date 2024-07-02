package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealResponseShort(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)
