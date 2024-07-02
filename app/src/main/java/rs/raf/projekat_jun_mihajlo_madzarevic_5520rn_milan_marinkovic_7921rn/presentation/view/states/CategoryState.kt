package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states

import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity

sealed class CategoryState {
    object Loading: CategoryState()
    object DataFetched: CategoryState()
    data class Success(val categories: List<CategoryEntity>): CategoryState()
    data class Error(val message: String): CategoryState()
}