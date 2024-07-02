package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieIngredientEntity

sealed class IngredientState {
    object Loading: IngredientState()
    object DataFetched: IngredientState()
    data class Success(val ingredients: List<CalorieIngredientEntity>): IngredientState()
    data class Error(val message: String): IngredientState()
}