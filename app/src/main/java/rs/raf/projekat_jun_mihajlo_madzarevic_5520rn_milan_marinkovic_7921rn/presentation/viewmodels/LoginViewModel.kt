package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.UserEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.UserRepository
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import timber.log.Timber

class LoginViewModel (
    private val userRepository: UserRepository
) : ViewModel() , MainContract.UserViewModel {

    private val subscriptions = CompositeDisposable()

    override fun insertUsers(users: List<UserEntity>) {
        val subscription = userRepository
            .insertUsers(users)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.e("INSERTED")
                },
                {
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getByUsernameAndPassword(username: String, password: String) : Single<UserEntity?> {
        return userRepository
            .getByUsernameAndPassword(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Timber.e(it) }
            .doOnSuccess { Timber.e("Retrieved user") }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}