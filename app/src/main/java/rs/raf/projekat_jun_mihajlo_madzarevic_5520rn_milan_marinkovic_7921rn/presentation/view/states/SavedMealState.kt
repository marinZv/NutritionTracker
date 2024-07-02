package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity

sealed class SavedMealState {
    data class Success(val meals: List<SavedMealEntity>): SavedMealState()
    data class Error(val message: String): SavedMealState()
}