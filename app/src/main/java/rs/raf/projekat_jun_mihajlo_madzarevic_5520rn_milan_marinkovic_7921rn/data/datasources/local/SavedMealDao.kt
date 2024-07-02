package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity

@Dao
abstract class SavedMealDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(meal: SavedMealEntity): Completable

    @Query("SELECT * FROM savedmeals WHERE strMeal LIKE :name")
    abstract fun getAllByName(name: String): Observable<List<SavedMealEntity>>

    @Query("SELECT * FROM savedmeals where idMeal = :id")
    abstract fun getById(id: Int): Observable<SavedMealEntity>

    @Query("SELECT * FROM savedmeals")
    abstract fun getAll(): Observable<List<SavedMealEntity>>

    @Update
    abstract fun updateSavedMeal(savedMeal: SavedMealEntity): Completable

    @Query("DELETE FROM savedmeals WHERE idMeal = :id")
    abstract fun delete(id: Int): Completable
}