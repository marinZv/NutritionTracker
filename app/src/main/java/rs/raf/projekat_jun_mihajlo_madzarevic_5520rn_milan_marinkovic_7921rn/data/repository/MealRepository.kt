package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieIngredientEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.Resource
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity

interface MealRepository {

    fun fetchAllIngredientsByName(name: String, mealPos: Int): Observable<Resource<Unit>>

    fun getAllIngredients(): Observable<List<CalorieIngredientEntity>>

    fun deleteAllIngredients(): Completable
    fun fetchAllByNameForCalorie(name: String): Observable<Resource<Unit>>

    fun getAllCalorie(): Observable<List<CalorieMealEntity>>

    fun deleteAllCalorie(): Completable

    fun fetchAllByName(name: String): Observable<Resource<Unit>>

    fun getAllByName(name: String): Observable<List<MealEntity>>

    fun fetchAllByCategory(category: String): Observable<Resource<Unit>>

    fun getAll(): Observable<List<MealEntity>>

    fun saveMeal(meal: SavedMealEntity): Completable

    fun updateSavedMeal(meal: SavedMealEntity): Completable

    fun deleteSavedMeal(id: Int): Completable

    fun getAllSaved(): Observable<List<SavedMealEntity>>

    fun getAllSavedByName(name: String): Observable<List<SavedMealEntity>>

    fun getAllSavedAsMealEntity(): Observable<List<MealEntity>>


    fun getSavedByIdAsMealEntity(id: Int): Observable<List<MealEntity>>

    fun getAllSavedByNameAsMealEntity(name: String): Observable<List<MealEntity>>

    fun fetchAllByArea(area: String): Observable<Resource<Unit>>
    fun fetchAllByIngredient(ingredient: String): Observable<Resource<Unit>>

    fun fetchAllByIngredientForMealList(ingredient: String): Observable<Resource<Unit>>

    fun fetchAllByNameForTag(name: String): Observable<Resource<Unit>>
    fun getAllByTag(tag: String):Observable<List<MealEntity>>
}