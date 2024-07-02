package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity

sealed class MealState {
    object Loading: MealState()
    object DataFetched: MealState()
    data class Success(val meals: List<MealEntity>): MealState()
    data class Error(val message: String): MealState()
}