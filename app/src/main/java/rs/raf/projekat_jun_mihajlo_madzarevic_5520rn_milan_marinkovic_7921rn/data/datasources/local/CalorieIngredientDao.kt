package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieIngredientEntity

@Dao
abstract class CalorieIngredientDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(meals: List<CalorieIngredientEntity>): Completable

    @Query("SELECT * FROM calorieingredients")
    abstract fun getAll(): Observable<List<CalorieIngredientEntity>>

    @Query("DELETE FROM calorieingredients")
    abstract fun deleteAll(): Completable

    @Transaction
    open fun deleteAndInsertAll(entities: List<CalorieIngredientEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }
}