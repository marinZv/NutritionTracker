package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity

sealed class CalorieMealState {
    object Loading: CalorieMealState()
    object DataFetched: CalorieMealState()
    data class Success(val meals: List<CalorieMealEntity>): CalorieMealState()
    data class Error(val message: String): CalorieMealState()
}