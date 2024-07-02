package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

sealed class DeleteSavedMealState {
    object Success: DeleteSavedMealState()
    data class Error(val message: String): DeleteSavedMealState()
}