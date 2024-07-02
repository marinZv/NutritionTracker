package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.Resource
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.CategoryRepository
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CategoryState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel(), MainContract.CategoryViewModel {

    private val subscriptions = CompositeDisposable()
    override val categoryState: MutableLiveData<CategoryState> = MutableLiveData()

    override fun fetchAll() {
        val subscription = categoryRepository
            .fetchAll()
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> categoryState.value = CategoryState.Loading
                        is Resource.Success -> categoryState.value = CategoryState.DataFetched
                        is Resource.Error -> categoryState.value = CategoryState.Error("Error happened while fetching data from the server for categories")
                    }
                },
                {
                    categoryState.value = CategoryState.Error("Error happened while fetching data from the server for categories")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAll() {
        val subscription = categoryRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    categoryState.value = CategoryState.Success(it)
                },
                {
                    categoryState.value = CategoryState.Error("Error happened while fetching data from db for categories")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}