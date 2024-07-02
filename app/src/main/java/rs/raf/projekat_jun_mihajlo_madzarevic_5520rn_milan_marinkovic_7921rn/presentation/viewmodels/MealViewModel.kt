package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.Resource
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.repository.MealRepository
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.AddMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteCalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteIngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteSavedMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.IngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.SavedMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.UpdateSavedMealState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MealViewModel (
    private val mealRepository: MealRepository
) : ViewModel(), MainContract.MealViewModel {

    private val subscriptions = CompositeDisposable()
    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    override val mealState: MutableLiveData<MealState> = MutableLiveData()
    override val savedMealOriginalState: MutableLiveData<SavedMealState> = MutableLiveData()
    override val addMeal: MutableLiveData<AddMealState> = MutableLiveData()
    override val updateSavedMealState: MutableLiveData<UpdateSavedMealState> = MutableLiveData()
    override val deleteCalorieMealState: MutableLiveData<DeleteCalorieMealState> = MutableLiveData()
    override val deleteIngredientState: MutableLiveData<DeleteIngredientState> = MutableLiveData()
    override val deleteSavedMealState: MutableLiveData<DeleteSavedMealState> = MutableLiveData()
    override val calorieMealState: MutableLiveData<CalorieMealState> = MutableLiveData()
    override val ingredientState: MutableLiveData<IngredientState> = MutableLiveData()

    init {
        var filter = ""
        val subscription = publishSubject
            .debounce(2000, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                filter = it
                mealRepository
                    .fetchAllByName(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in category view model")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> {
                            mealState.value = MealState.DataFetched
                            fetchAllByIngredientForMealList(filter)
                        }
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllByNameSearch(name: String) {
        publishSubject.onNext(name)
    }

    override fun fetchAllIngredientsByName(name: String, mealPos: Int) {
        val subscription = mealRepository
            .fetchAllIngredientsByName(name, mealPos)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> ingredientState.value = IngredientState.Loading
                        is Resource.Success -> ingredientState.value = IngredientState.DataFetched
                        is Resource.Error -> ingredientState.value = IngredientState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    ingredientState.value = IngredientState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllIngredients() {
        val subscription = mealRepository
            .getAllIngredients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    ingredientState.value = IngredientState.Success(it)
                },
                {
                    ingredientState.value = IngredientState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun deleteAllIngredients() {
        val subscription = mealRepository
            .deleteAllIngredients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteIngredientState.value = DeleteIngredientState.Success
                },
                {
                    deleteIngredientState.value = DeleteIngredientState.Error("Error happened while fetching data from db")
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByNameForCalorie(name: String) {
        val subscription = mealRepository
            .fetchAllByNameForCalorie(name)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> calorieMealState.value = CalorieMealState.Loading
                        is Resource.Success -> calorieMealState.value = CalorieMealState.DataFetched
                        is Resource.Error -> calorieMealState.value = CalorieMealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    calorieMealState.value = CalorieMealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllCalorie() {
        val subscription = mealRepository
            .getAllCalorie()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    calorieMealState.value = CalorieMealState.Success(it)
                },
                {
                    calorieMealState.value = CalorieMealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun deleteAllCalorie() {
        val subscription = mealRepository
            .deleteAllCalorie()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteCalorieMealState.value = DeleteCalorieMealState.Success
                },
                {
                    deleteCalorieMealState.value = DeleteCalorieMealState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllByName(name: String) {
        val subscription = mealRepository
            .getAllByName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByName(name: String) {
        val subscription = mealRepository
            .fetchAllByName(name)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByCategory(category: String) {
        val subscription = mealRepository
            .fetchAllByCategory(category)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByArea(area: String) {
        val subscription = mealRepository
            .fetchAllByArea(area)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it){
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetchin data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByIngredient(ingredient: String) {
        val subscription = mealRepository
            .fetchAllByIngredient(ingredient)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it){
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByIngredientForMealList(ingredient: String) {
        val subscription = mealRepository
            .fetchAllByIngredientForMealList(ingredient)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it){
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAllByNameForTag(name: String) {
        val subscription = mealRepository
            .fetchAllByNameForTag(name)
            .startWith(Resource.Loading())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> mealState.value = MealState.Loading
                        is Resource.Success -> mealState.value = MealState.DataFetched
                        is Resource.Error -> mealState.value = MealState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllByTag(tag: String) {
        val subscription = mealRepository
            .getAllByTag(tag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(listOf(MealEntity("-1","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","")) + it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }


    override fun getAll() {
        val subscription = mealRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun saveMeal(meal: SavedMealEntity) {
        val subscription = mealRepository
            .saveMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    addMeal.value = AddMealState.Success
                },
                {
                    addMeal.value = AddMealState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun updateSavedMeal(meal: SavedMealEntity) {
        val subscription = mealRepository
            .updateSavedMeal(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateSavedMealState.value = UpdateSavedMealState.Success
                },
                {
                    updateSavedMealState.value = UpdateSavedMealState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun deleteMeal(id: Int) {
        val subscription = mealRepository
            .deleteSavedMeal(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    deleteSavedMealState.value = DeleteSavedMealState.Success
                },
                {
                    deleteSavedMealState.value = DeleteSavedMealState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllSaved() {
        val subscription = mealRepository
            .getAllSaved()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    savedMealOriginalState.value = SavedMealState.Success(it)
                },
                {
                    savedMealOriginalState.value = SavedMealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

//    override fun getAllSavedByName(name: String) {
//        val subscription = mealRepository
//            .getAllSavedByName(name)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                    mealState.value = MealState.Success(it)
//                },
//                {
//                    mealState.value = MealState.Error("Error happened while fetching data from db")
//                    Timber.e(it)
//                }
//            )
//        subscriptions.add(subscription)
//    }

    override fun getAllSavedAsMealEntity() {
        val subscription = mealRepository
            .getAllSavedAsMealEntity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getSavedById(id: Int) {
        val subscription = mealRepository
            .getSavedByIdAsMealEntity(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllSavedByNameAsMealEntity(name: String) {
        val subscription = mealRepository
            .getAllSavedByNameAsMealEntity(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealState.value = MealState.Success(it)
                },
                {
                    mealState.value = MealState.Error("Error happened while fetching data from db")
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