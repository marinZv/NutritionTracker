package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository

import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CategoryDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote.CategoryService
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryResponse
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.Resource

class CategoryRepositoryImpl(
    private val localDataSource: CategoryDao,
    private val remoteDataSource: CategoryService
) : CategoryRepository{

    override fun fetchAll(): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAll()
            .doOnNext{
                val entities = it.categories.map{
                    CategoryEntity(
                        it.idCategory.toInt(),
                        it.strCategory,
                        it.strCategoryThumb,
                        it.strCategoryDescription
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAll(): Observable<List<CategoryEntity>> {
        return localDataSource
            .getAll()
            .map{
                it.map {
                    CategoryEntity(it.idCategory, it.strCategory, it.strCategoryThumb, it.strCategoryDescription)
                }
            }
    }
}