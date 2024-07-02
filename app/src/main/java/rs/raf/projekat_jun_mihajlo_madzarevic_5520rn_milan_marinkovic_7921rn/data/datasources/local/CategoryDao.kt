package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import io.reactivex.Observable
import androidx.room.*
import io.reactivex.Completable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity

@Dao
abstract class CategoryDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(categories: List<CategoryEntity>): Completable

    @Query("SELECT * FROM categories")
    abstract fun getAll(): Observable<List<CategoryEntity>>

    @Query("DELETE FROM categories")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(entities: List<CategoryEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }
}