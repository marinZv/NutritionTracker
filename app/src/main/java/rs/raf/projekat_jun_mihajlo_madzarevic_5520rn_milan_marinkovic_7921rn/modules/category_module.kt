package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.database.NutritionDatabase
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.CategoryService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.CategoryRepository
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.CategoryRepositoryImpl
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.CategoryViewModel

val categoryModule = module {

    viewModel { CategoryViewModel(categoryRepository = get()) }

    single<CategoryRepository> { CategoryRepositoryImpl(localDataSource = get(), remoteDataSource =  get()) }

    single { get<NutritionDatabase>().getCategoryDao() }

    single<CategoryService> { create(retrofit = get()) }
}