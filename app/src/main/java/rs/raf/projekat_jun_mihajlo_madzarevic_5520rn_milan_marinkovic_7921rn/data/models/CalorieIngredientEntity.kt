package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calorieingredients", primaryKeys = ["mealPos", "name"])
data class CalorieIngredientEntity (
    val mealPos: Int,
    val name: String,
    val calories: Float,
    val serving_size_g: Float,
    val fat_total_g: Float,
    val fat_saturated_g: Float,
    val protein_g: Float,
    val sodium_mg: Float,
    val potassium_mg: Float,
    val cholesterol_mg: Float,
    val carbohydrates_total_g: Float,
    val fiber_g: Float,
    val sugar_g:Float
)
