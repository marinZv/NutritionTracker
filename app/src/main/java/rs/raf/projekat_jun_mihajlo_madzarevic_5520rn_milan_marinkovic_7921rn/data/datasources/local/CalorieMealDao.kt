package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity

@Dao
abstract class CalorieMealDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(meals: List<CalorieMealEntity>): Completable

    @Query("SELECT * FROM caloriemeals")
    abstract fun getAll(): Observable<List<CalorieMealEntity>>

    @Query("DELETE FROM caloriemeals")
    abstract fun deleteAll(): Completable
}