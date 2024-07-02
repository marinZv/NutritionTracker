package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

sealed class AddMealState {
    object Success: AddMealState()
    data class Error(val message: String): AddMealState()
}