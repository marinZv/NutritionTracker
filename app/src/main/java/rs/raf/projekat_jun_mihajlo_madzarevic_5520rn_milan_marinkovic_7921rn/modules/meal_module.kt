package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.database.NutritionDatabase
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.MealService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.NinjaMealService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.MealRepository
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.MealRepositoryImpl
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel

val mealModule = module {

    viewModel { MealViewModel(mealRepository = get()) }

    single<MealRepository> { MealRepositoryImpl(localDataSource = get(), savedLocalDataSource = get(), calorieLocalDataSource = get(), calorieIngredientLocalDataSource = get(), remoteDataSource =  get(), ninjaRemoteDataSource = get()) }

    single { get<NutritionDatabase>().getMealDao() }

    single { get<NutritionDatabase>().getSavedMealDao() }

    single { get<NutritionDatabase>().getCalorieMealDao() }

    single { get<NutritionDatabase>().getCalorieIngredientDao() }

    single<MealService> { create(retrofit = get()) }

    single<NinjaMealService> { create(retrofit = get())}
}