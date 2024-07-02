package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity

@Dao
abstract class MealDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(meals: List<MealEntity>): Completable

    @Query("SELECT * FROM meals WHERE strMeal LIKE :name")
    abstract fun getAllByName(name: String): Observable<List<MealEntity>>

    @Query("SELECT * FROM meals")
    abstract fun getAll(): Observable<List<MealEntity>>

    @Query("DELETE FROM meals")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(entities: List<MealEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Query("SELECT * FROM meals WHERE strTags LIKE '%' || LOWER(:tag) || '%'")
    abstract fun getAllByTag(tag: String): Observable<List<MealEntity>>
}