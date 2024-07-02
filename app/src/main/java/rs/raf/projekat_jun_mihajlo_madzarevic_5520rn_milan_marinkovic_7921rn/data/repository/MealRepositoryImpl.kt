package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CalorieIngredientDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CalorieMealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.MealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.SavedMealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.MealService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.NinjaMealService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieIngredientEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.Resource
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity
import timber.log.Timber

class MealRepositoryImpl (
    private val localDataSource: MealDao,
    private val savedLocalDataSource: SavedMealDao,
    private val calorieLocalDataSource: CalorieMealDao,
    private val calorieIngredientLocalDataSource: CalorieIngredientDao,
    private val remoteDataSource: MealService,
    private val ninjaRemoteDataSource: NinjaMealService
) : MealRepository {

    override fun fetchAllIngredientsByName(name: String, mealPos: Int): Observable<Resource<Unit>> {
        return ninjaRemoteDataSource
            .getAllByTitle(name)
            .doOnNext{
                val entities = it.map{
                    CalorieIngredientEntity(
                        mealPos,
                        it.name,
                        it.calories,
                        it.serving_size_g,
                        it.fat_total_g,
                        it.fat_saturated_g,
                        it.protein_g,
                        it.sodium_mg,
                        it.potassium_mg,
                        it.cholesterol_mg,
                        it.carbohydrates_total_g,
                        it.fiber_g,
                        it.sugar_g
                    )
                }
                calorieIngredientLocalDataSource.insertAll(entities).blockingAwait()
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAllIngredients(): Observable<List<CalorieIngredientEntity>> {
        return calorieIngredientLocalDataSource.getAll()
    }

    override fun deleteAllIngredients(): Completable {
        return calorieIngredientLocalDataSource.deleteAll()
    }

    override fun fetchAllByNameForCalorie(name: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByName(name)
            .doOnNext{
                val entities = it.meals.map{
                    CalorieMealEntity(
                        0f,
                        null,
                        null,
                        it.idMeal.toInt(),
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.dateModified,
                        )
                }
                calorieLocalDataSource.insertAll(entities).blockingAwait()
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAllCalorie(): Observable<List<CalorieMealEntity>> {
        return calorieLocalDataSource.getAll()
    }

    override fun deleteAllCalorie(): Completable {
        return calorieLocalDataSource.deleteAll()
    }

    override fun fetchAllByName(name: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByName(name)
            .doOnNext{
                if (it != null && it.meals != null) {
                    val entities = it.meals.map {
                        MealEntity(
                            it.idMeal,
                            it.strMeal,
                            it.strDrinkAlternate,
                            it.strCategory,
                            it.strArea,
                            it.strInstructions,
                            it.strMealThumb,
                            it.strTags,
                            it.strYoutube,
                            it.strIngredient1,
                            it.strIngredient2,
                            it.strIngredient3,
                            it.strIngredient4,
                            it.strIngredient5,
                            it.strIngredient6,
                            it.strIngredient7,
                            it.strIngredient8,
                            it.strIngredient9,
                            it.strIngredient10,
                            it.strIngredient11,
                            it.strIngredient12,
                            it.strIngredient13,
                            it.strIngredient14,
                            it.strIngredient15,
                            it.strIngredient16,
                            it.strIngredient17,
                            it.strIngredient18,
                            it.strIngredient19,
                            it.strIngredient20,
                            it.strMeasure1,
                            it.strMeasure2,
                            it.strMeasure3,
                            it.strMeasure4,
                            it.strMeasure5,
                            it.strMeasure6,
                            it.strMeasure7,
                            it.strMeasure8,
                            it.strMeasure9,
                            it.strMeasure10,
                            it.strMeasure11,
                            it.strMeasure12,
                            it.strMeasure13,
                            it.strMeasure14,
                            it.strMeasure15,
                            it.strMeasure16,
                            it.strMeasure17,
                            it.strMeasure18,
                            it.strMeasure19,
                            it.strMeasure20,
                            it.strSource,
                            it.strImageSource,
                            it.strCreativeCommonsConfirmed,
                            it.dateModified,

                            )
                    }
                    localDataSource.deleteAndInsertAll(entities)
                }
                else{
                    localDataSource.deleteAndInsertAll(listOf())
                }
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchAllByCategory(category: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByCategory(category)
            .doOnNext {
                if (it != null && it.meals != null) {
                    val entities = it.meals.map {
                        MealEntity(
                            it.idMeal,
                            it.strMeal,
                            null,
                            category,
                            null,
                            null,
                            it.strMealThumb,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                    localDataSource.deleteAndInsertAll(entities)
                }
                else{
                    localDataSource.deleteAndInsertAll(listOf())
                }
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAllByName(name: String): Observable<List<MealEntity>> {
        return localDataSource
            .getAllByName(name)
            .map{
                it.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.dateModified,
                    )
                }
            }
    }

    override fun getAll(): Observable<List<MealEntity>> {
        return localDataSource
            .getAll()
            .map{
                it.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.dateModified,
                    )
                }
            }
    }

    override fun saveMeal(meal: SavedMealEntity): Completable {
        return savedLocalDataSource.insert(meal)
    }

    override fun updateSavedMeal(meal: SavedMealEntity): Completable {
        return savedLocalDataSource.updateSavedMeal(meal)
    }

    override fun deleteSavedMeal(id: Int): Completable {
        return savedLocalDataSource.delete(id)
    }

    override fun getAllSaved(): Observable<List<SavedMealEntity>> {
        return savedLocalDataSource.getAll()
    }

    override fun getAllSavedByName(name: String): Observable<List<SavedMealEntity>> {
        return savedLocalDataSource.getAllByName(name)
    }

    override fun getAllSavedAsMealEntity(): Observable<List<MealEntity>> {
        return savedLocalDataSource
            .getAll()
            .map {
                it.map{
                    MealEntity(
                        it.idMeal.toString(),
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        "kcal " + it.calorie,
                    )
                }
            }
    }

    override fun getSavedByIdAsMealEntity(id: Int): Observable<List<MealEntity>> {
        return savedLocalDataSource
            .getById(id)
            .map {
                listOf(
                MealEntity(
                    it.idMeal.toString(),
                    it.strMeal,
                    it.strDrinkAlternate,
                    it.strCategory,
                    it.strArea,
                    it.strInstructions,
                    it.strMealThumb,
                    it.strTags,
                    it.strYoutube,
                    it.strIngredient1,
                    it.strIngredient2,
                    it.strIngredient3,
                    it.strIngredient4,
                    it.strIngredient5,
                    it.strIngredient6,
                    it.strIngredient7,
                    it.strIngredient8,
                    it.strIngredient9,
                    it.strIngredient10,
                    it.strIngredient11,
                    it.strIngredient12,
                    it.strIngredient13,
                    it.strIngredient14,
                    it.strIngredient15,
                    it.strIngredient16,
                    it.strIngredient17,
                    it.strIngredient18,
                    it.strIngredient19,
                    it.strIngredient20,
                    it.strMeasure1,
                    it.strMeasure2,
                    it.strMeasure3,
                    it.strMeasure4,
                    it.strMeasure5,
                    it.strMeasure6,
                    it.strMeasure7,
                    it.strMeasure8,
                    it.strMeasure9,
                    it.strMeasure10,
                    it.strMeasure11,
                    it.strMeasure12,
                    it.strMeasure13,
                    it.strMeasure14,
                    it.strMeasure15,
                    it.strMeasure16,
                    it.strMeasure17,
                    it.strMeasure18,
                    it.strMeasure19,
                    it.strMeasure20,
                    it.strSource,
                    it.strImageSource,
                    it.strCreativeCommonsConfirmed,
                    it.calorie.toString(),
                    )
                )
            }
    }

    override fun getAllSavedByNameAsMealEntity(name: String): Observable<List<MealEntity>> {
        return savedLocalDataSource
            .getAllByName(name)
            .map {
                it.map{
                    MealEntity(
                        it.idMeal.toString(),
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.calorie.toString(),
                    )
                }
            }
    }

    override fun fetchAllByArea(area: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByArea(area)
            .doOnNext{
                if (it != null && it.meals != null) {
                    val entities = it.meals.map {
                        MealEntity(
                            it.idMeal,
                            it.strMeal,
                            null,
                            null,
                            area,
                            null,
                            it.strMealThumb,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                    localDataSource.deleteAndInsertAll(entities)
                }
                else{
                    localDataSource.deleteAndInsertAll(listOf())
                }
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchAllByIngredient(ingredient: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByIngredient(ingredient)
            .doOnNext{
                if (it != null && it.meals != null) {
                    val entities = it.meals.map {
                        MealEntity(
                            it.idMeal,
                            it.strMeal,
                            null,
                            null,
                            null,
                            null,
                            it.strMealThumb,
                            null,
                            null,
                            ingredient,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                    localDataSource.deleteAndInsertAll(entities)
                }
                else{
                    localDataSource.deleteAndInsertAll(listOf())
                }
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchAllByIngredientForMealList(ingredient: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByIngredient(ingredient)
            .doOnNext{
                if (it != null && it.meals != null) {
                    val entities = it.meals.map {
                        MealEntity(
                            it.idMeal,
                            it.strMeal,
                            null,
                            null,
                            null,
                            null,
                            it.strMealThumb,
                            null,
                            null,
                            ingredient,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                    localDataSource.insertAll(entities).blockingAwait()
                }
                else{
                    localDataSource.insertAll(listOf()).blockingAwait()
                }
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchAllByNameForTag(name: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAllByName(name)
            .doOnNext{
                val entities = it.meals.map{
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.dateModified,
                    )
                }
                localDataSource.insertAll(entities).blockingAwait()
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAllByTag(tag: String): Observable<List<MealEntity>> {
        return localDataSource
            .getAllByTag(tag)
            .map{
                it.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        it.strDrinkAlternate,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strMealThumb,
                        it.strTags,
                        it.strYoutube,
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15,
                        it.strIngredient16,
                        it.strIngredient17,
                        it.strIngredient18,
                        it.strIngredient19,
                        it.strIngredient20,
                        it.strMeasure1,
                        it.strMeasure2,
                        it.strMeasure3,
                        it.strMeasure4,
                        it.strMeasure5,
                        it.strMeasure6,
                        it.strMeasure7,
                        it.strMeasure8,
                        it.strMeasure9,
                        it.strMeasure10,
                        it.strMeasure11,
                        it.strMeasure12,
                        it.strMeasure13,
                        it.strMeasure14,
                        it.strMeasure15,
                        it.strMeasure16,
                        it.strMeasure17,
                        it.strMeasure18,
                        it.strMeasure19,
                        it.strMeasure20,
                        it.strSource,
                        it.strImageSource,
                        it.strCreativeCommonsConfirmed,
                        it.dateModified,
                    )
                }
            }
    }

}