package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.UserEntity

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users : List<UserEntity>): Single<List<Long>>

    @Query("SELECT * FROM users WHERE username == :username and password == :password")
    abstract fun getByUsernameAndPassword(username: String, password: String): Single<UserEntity?>
}