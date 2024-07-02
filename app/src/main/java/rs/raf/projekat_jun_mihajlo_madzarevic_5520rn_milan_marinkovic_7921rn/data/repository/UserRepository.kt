package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository

import io.reactivex.Single
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.UserEntity

interface UserRepository {

    fun insertUsers(users : List<UserEntity>): Single<List<Long>>

    fun getByUsernameAndPassword(username: String, password: String): Single<UserEntity?>
}