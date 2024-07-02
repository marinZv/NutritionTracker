package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract

import androidx.lifecycle.LiveData
import io.reactivex.Single
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.UserEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.AddMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CategoryState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteCalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteIngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteSavedMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.IngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.SavedMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.UpdateSavedMealState

interface MainContract {
    interface UserViewModel {
        fun insertUsers(users : List<UserEntity>)
        fun getByUsernameAndPassword(username : String, password: String): Single<UserEntity?>
    }

    interface CategoryViewModel {

        val categoryState: LiveData<CategoryState>

        fun fetchAll()
        fun getAll()
    }

    interface MealViewModel {

        val mealState: LiveData<MealState>

        val savedMealOriginalState: LiveData<SavedMealState>

        val calorieMealState: LiveData<CalorieMealState>

        val addMeal: LiveData<AddMealState>

        val updateSavedMealState: LiveData<UpdateSavedMealState>

        val deleteCalorieMealState: LiveData<DeleteCalorieMealState>

        val deleteIngredientState: LiveData<DeleteIngredientState>

        val deleteSavedMealState: LiveData<DeleteSavedMealState>

        val ingredientState: LiveData<IngredientState>

        fun fetchAllIngredientsByName(name: String, mealPos: Int)

        fun getAllIngredients()

        fun deleteAllIngredients()

        fun fetchAllByNameForCalorie(name: String)

        fun getAllCalorie()

        fun deleteAllCalorie()


        fun fetchAllByName(name: String)

        fun fetchAllByCategory(category: String)

        fun getAllByNameSearch(name: String)

        fun getAllByName(name: String)

        fun getAll()

        fun fetchAllByArea(area: String)
        fun fetchAllByIngredient(ingredient: String)

        fun fetchAllByIngredientForMealList(ingredient: String)

        fun fetchAllByNameForTag(name: String)
        fun getAllByTag(tag: String)

        fun saveMeal(meal: SavedMealEntity)

        fun updateSavedMeal(meal: SavedMealEntity)

        fun deleteMeal(id: Int)

        fun getAllSaved()

//        fun getAllSavedByName(name: String)

        fun getAllSavedAsMealEntity()

        fun getSavedById(id: Int)

        fun getAllSavedByNameAsMealEntity(name: String)
    }
}