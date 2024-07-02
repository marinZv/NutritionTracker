package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.CalorieFilterBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentMealListBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.MealAdapter
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ.MealDiffItemCallback
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteCalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteIngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.IngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel
import timber.log.Timber

class MealListFragment(private val category: String?) : Fragment() {
    private lateinit var binding : FragmentMealListBinding

    private lateinit var mealAdapter: MealAdapter
    private lateinit var recyclerView: RecyclerView

    private val mealViewModel: MainContract.MealViewModel by viewModel<MealViewModel>()

    private lateinit var allMeals: List<MealEntity>
    var allMealsFull: MutableList<CalorieMealEntity> = mutableListOf()

    private var mealsPerPage = 10
    private var currentPage = 0

    private var calorieCounter = 0
    private var calorieLowBound = -1
    private var calorieUpperBound = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMealListBinding.inflate(layoutInflater)
        recyclerView = binding.mealsRV
        activity?.title = getString(R.string.mealsInCategory) + " " + category + " category"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initRecycler()
        initObservers()
        initListeners()
    }

    private fun initRecycler(){
        mealAdapter = MealAdapter(MealDiffItemCallback(), binding)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mealAdapter
    }

    private fun initListeners() {
        binding.mealListTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        mealViewModel.getAll()
                        if (category != null) {
                            mealViewModel.fetchAllByCategory(category)
                        }
                    }
                    1 -> {
                        mealViewModel.getAllSavedAsMealEntity()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.searchMealList.doAfterTextChanged {
            val filter = it.toString()
            if (filter.isEmpty()){
                if (category != null) {
                    if (allMeals.size > mealsPerPage) {
                        mealAdapter.submitList(allMeals.subList(0, mealsPerPage))
                    }
                    else {
                        mealAdapter.submitList(allMeals)
                    }
                }
            }
            else{
                if (binding.mealListTabLayout.selectedTabPosition == 0) {
                    mealViewModel.getAll()
                    mealViewModel.getAllByNameSearch(filter)
                }
                else{
                    mealViewModel.getAllSavedByNameAsMealEntity(filter)
                }
            }
        }

        //Back button returns us to categories list
        binding.mealListBackButton.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, HomeFragment()).commit()
        }

        binding.mealStatisticsButton.setOnClickListener{
            if (category != null) {
                (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, StatisticsFragment(category)).commit()
            }
        }

        binding.mealCaloriesButton.setOnClickListener{
            showCalorieFilter()
        }

        //Load previous 10 meals
        binding.mealBackwardPagination.setOnClickListener{
            if ((currentPage-1) >= 0){
                currentPage -= 1
                mealAdapter.submitList(allMeals.subList(currentPage * mealsPerPage, (currentPage+1) * mealsPerPage))
            }
        }

        //Load next 10 meals
        binding.mealForwardPagination.setOnClickListener{
            if ((currentPage+1) * mealsPerPage < allMeals.size){
                currentPage += 1
                if ((currentPage+1) * mealsPerPage < allMeals.size){
                    mealAdapter.submitList(allMeals.subList(currentPage * mealsPerPage, (currentPage+1) * mealsPerPage))
                }
                else {
                    mealAdapter.submitList(allMeals.subList(currentPage * mealsPerPage, allMeals.size))
                }
            }
        }
    }

    private fun initObservers() {
        mealViewModel.mealState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
        mealViewModel.deleteCalorieMealState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderStateDeleteCalorie(it)
        })
        mealViewModel.deleteIngredientState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderStateDeleteIngredient(it)
        })
        mealViewModel.calorieMealState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderStateCalorie(it)
        })
        mealViewModel.ingredientState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderStateIngredient(it)
        })
        mealViewModel.getAll()

        if(category != null){
            mealViewModel.fetchAllByCategory(category)
        }
    }

    private fun renderState(state: MealState) {
        when (state) {
            is MealState.Success -> {
                showLoadingState(false)
                allMeals = state.meals
                if (mealsPerPage <= state.meals.size) {
                    mealAdapter.submitList(state.meals.subList(0, mealsPerPage))
                }
                else {
                    mealAdapter.submitList(state.meals)
                }

            }
            is MealState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, getString(R.string.data_fetched), Toast.LENGTH_LONG).show()
            }
            is MealState.Loading -> {
                showLoadingState(true)
            }

        }
    }

    private fun renderStateDeleteCalorie(state: DeleteCalorieMealState) {
        when (state) {
            is DeleteCalorieMealState.Success -> {
                showLoadingState(true)
                mealViewModel.deleteAllIngredients()
            }
            is DeleteCalorieMealState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun renderStateDeleteIngredient(state: DeleteIngredientState) {
        when (state) {
            is DeleteIngredientState.Success -> {
                showLoadingState(true)
                mealViewModel.getAllCalorie()
                for (meal in allMeals){
                    mealViewModel.fetchAllByNameForCalorie(meal.strMeal)
                }
            }
            is DeleteIngredientState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun renderStateCalorie(state: CalorieMealState) {
        when (state) {
            is CalorieMealState.Success -> {
                if (state.meals.size < calorieCounter){
                    showLoadingState(true)
                }
                else {
                    showLoadingState(false)
                    allMealsFull = state.meals.toMutableList()

                    mealViewModel.getAllIngredients()

                    var i: Int = 0
                    for (meal in allMealsFull) {

                        var ingredients: MutableList<String?> = mutableListOf()
                        var measures: MutableList<String?> = mutableListOf()

                        var allIngredientsToSubmit = ""

                        ingredients.addAll(
                            listOf(
                                meal.strIngredient1,
                                meal.strIngredient2,
                                meal.strIngredient3,
                                meal.strIngredient4,
                                meal.strIngredient5,
                                meal.strIngredient6,
                                meal.strIngredient7,
                                meal.strIngredient8,
                                meal.strIngredient9,
                                meal.strIngredient10,
                                meal.strIngredient11,
                                meal.strIngredient12,
                                meal.strIngredient13,
                                meal.strIngredient14,
                                meal.strIngredient15,
                                meal.strIngredient16,
                                meal.strIngredient17,
                                meal.strIngredient18,
                                meal.strIngredient19,
                                meal.strIngredient20
                            )
                        )

                        measures.addAll(
                            listOf(
                                meal.strMeasure1,
                                meal.strMeasure2,
                                meal.strMeasure3,
                                meal.strMeasure4,
                                meal.strMeasure5,
                                meal.strMeasure6,
                                meal.strMeasure7,
                                meal.strMeasure8,
                                meal.strMeasure9,
                                meal.strMeasure10,
                                meal.strMeasure11,
                                meal.strMeasure12,
                                meal.strMeasure13,
                                meal.strMeasure14,
                                meal.strMeasure15,
                                meal.strMeasure16,
                                meal.strMeasure17,
                                meal.strMeasure18,
                                meal.strMeasure19,
                                meal.strMeasure20
                            )
                        )

                        for (i in 0..19) {
                            if (ingredients[i] == null || measures[i] == null || ingredients[i] == "" || measures[i] == "") break
                            allIngredientsToSubmit =
                                allIngredientsToSubmit + " " + measures[i] + " " + ingredients[i]?.replace(
                                    "-",
                                    " "
                                )
                        }

                        mealViewModel.fetchAllIngredientsByName(allIngredientsToSubmit, i)
                        i++
                    }
                }

            }
            is CalorieMealState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is CalorieMealState.DataFetched -> {
                showLoadingState(false)
            }
            is CalorieMealState.Loading -> {
                showLoadingState(true)
            }

        }
    }

    private fun renderStateIngredient(state: IngredientState) {
        when (state) {
            is IngredientState.Success -> {
                if (state.ingredients.isNotEmpty())
                {
                    var ignorePos = mutableListOf<Int>()
                    for (i in 0 until allMealsFull.size){
                        if (allMealsFull[i].calorie != 0f){
                            ignorePos.add(i)
                        }
                    }

                    for (ingredient in state.ingredients) {
                        if (!ignorePos.contains(ingredient.mealPos)) {
                            allMealsFull[ingredient.mealPos].calorie += ingredient.calories
                        }
                    }

                    var allMealsCopy = mutableListOf<MealEntity>()

                    fun addToMealsCopy(meal: CalorieMealEntity) {
                        allMealsCopy.add(
                            MealEntity(
                                meal.idMeal.toString(),
                                meal.strMeal,
                                meal.strDrinkAlternate,
                                meal.strCategory,
                                meal.strArea,
                                meal.strInstructions,
                                meal.strMealThumb,
                                meal.strTags,
                                meal.strYoutube,
                                meal.strIngredient1,
                                meal.strIngredient2,
                                meal.strIngredient3,
                                meal.strIngredient4,
                                meal.strIngredient5,
                                meal.strIngredient6,
                                meal.strIngredient7,
                                meal.strIngredient8,
                                meal.strIngredient9,
                                meal.strIngredient10,
                                meal.strIngredient11,
                                meal.strIngredient12,
                                meal.strIngredient13,
                                meal.strIngredient14,
                                meal.strIngredient15,
                                meal.strIngredient16,
                                meal.strIngredient17,
                                meal.strIngredient18,
                                meal.strIngredient19,
                                meal.strIngredient20,
                                meal.strMeasure1,
                                meal.strMeasure2,
                                meal.strMeasure3,
                                meal.strMeasure4,
                                meal.strMeasure5,
                                meal.strMeasure6,
                                meal.strMeasure7,
                                meal.strMeasure8,
                                meal.strMeasure9,
                                meal.strMeasure10,
                                meal.strMeasure11,
                                meal.strMeasure12,
                                meal.strMeasure13,
                                meal.strMeasure14,
                                meal.strMeasure15,
                                meal.strMeasure16,
                                meal.strMeasure17,
                                meal.strMeasure18,
                                meal.strMeasure19,
                                meal.strMeasure20,
                                meal.strSource,
                                meal.strImageSource,
                                meal.strCreativeCommonsConfirmed,
                                "kcal " + meal.calorie
                            )
                        )
                    }

                    for (meal in allMealsFull) {
                        addToMealsCopy(meal)
                    }

                    allMeals = allMealsCopy

                    if (allMeals.size > mealsPerPage) {
                        mealAdapter.submitList(allMeals.subList(0, mealsPerPage))
                        currentPage = 0
                    } else {
                        mealAdapter.submitList(allMeals)
                        currentPage = 0
                    }
                }
            }
            is IngredientState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is IngredientState.DataFetched -> {
                showLoadingState(false) }
            is IngredientState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun showCalorieFilter() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.calorieFilterTitle))

        val dialogBind: CalorieFilterBinding = CalorieFilterBinding.inflate(layoutInflater)

        builder.setView(dialogBind.root)

        val dialog = builder.create()

        dialogBind.calorieFilterButton.setOnClickListener{

            calorieCounter = allMeals.size

            allMealsFull.clear()
            mealViewModel.deleteAllCalorie()

            dialog.dismiss()
        }

        dialogBind.calorieSortButton.setOnClickListener{

            if (!allMealsFull.isEmpty()) {

                calorieLowBound = -1
                calorieUpperBound = -1

                if (dialogBind.calorieLowerBond.text.toString() != "") {
                    try {
                        calorieLowBound = dialogBind.calorieLowerBond.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context,R.string.error_string_not_number, Toast.LENGTH_SHORT).show()
                    }
                }
                if (dialogBind.calorieUpperBond.text.toString() != "") {
                    try {
                        calorieUpperBound = dialogBind.calorieUpperBond.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context,R.string.error_string_not_number, Toast.LENGTH_SHORT).show()
                    }
                }

                allMealsFull.sortBy { it.calorie }
                var allMealsCopy = mutableListOf<MealEntity>()

                fun addToMealsCopy(meal: CalorieMealEntity) {
                    allMealsCopy.add(
                        MealEntity(
                            meal.idMeal.toString(),
                            meal.strMeal,
                            meal.strDrinkAlternate,
                            meal.strCategory,
                            meal.strArea,
                            meal.strInstructions,
                            meal.strMealThumb,
                            meal.strTags,
                            meal.strYoutube,
                            meal.strIngredient1,
                            meal.strIngredient2,
                            meal.strIngredient3,
                            meal.strIngredient4,
                            meal.strIngredient5,
                            meal.strIngredient6,
                            meal.strIngredient7,
                            meal.strIngredient8,
                            meal.strIngredient9,
                            meal.strIngredient10,
                            meal.strIngredient11,
                            meal.strIngredient12,
                            meal.strIngredient13,
                            meal.strIngredient14,
                            meal.strIngredient15,
                            meal.strIngredient16,
                            meal.strIngredient17,
                            meal.strIngredient18,
                            meal.strIngredient19,
                            meal.strIngredient20,
                            meal.strMeasure1,
                            meal.strMeasure2,
                            meal.strMeasure3,
                            meal.strMeasure4,
                            meal.strMeasure5,
                            meal.strMeasure6,
                            meal.strMeasure7,
                            meal.strMeasure8,
                            meal.strMeasure9,
                            meal.strMeasure10,
                            meal.strMeasure11,
                            meal.strMeasure12,
                            meal.strMeasure13,
                            meal.strMeasure14,
                            meal.strMeasure15,
                            meal.strMeasure16,
                            meal.strMeasure17,
                            meal.strMeasure18,
                            meal.strMeasure19,
                            meal.strMeasure20,
                            meal.strSource,
                            meal.strImageSource,
                            meal.strCreativeCommonsConfirmed,
                            "kcal " + meal.calorie
                        )
                    )
                }

                for (meal in allMealsFull){
                    if (calorieLowBound != -1 && calorieUpperBound != -1) {
                        if (meal.calorie.toInt() in calorieLowBound..calorieUpperBound) {
                            addToMealsCopy(meal)
                        }
                    } else if (calorieLowBound != -1) {
                        if (meal.calorie > calorieLowBound) {
                            addToMealsCopy(meal)
                        }
                    } else if (calorieUpperBound != -1) {
                        if (meal.calorie < calorieUpperBound) {
                            addToMealsCopy(meal)
                        }
                    } else {
                        addToMealsCopy(meal)
                    }
                }

                allMeals = allMealsCopy
                if (allMeals.size > mealsPerPage){
                    mealAdapter.submitList(allMeals.subList(0,mealsPerPage))
                }
                else{
                    mealAdapter.submitList(allMeals)
                }

            }
            else{
                Toast.makeText(context, getString(R.string.error_no_kcal_list), Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLoadingState(loading: Boolean) {
        if (binding.mealListTabLayout.selectedTabPosition == 0) {
            binding.searchMealList.isVisible = !loading
        }
        else{
            binding.searchMealList.isVisible = false
        }
        binding.mealsRV.isVisible = !loading
        binding.mealForwardPagination.isVisible = !loading
        binding.mealBackwardPagination.isVisible = !loading
        binding.mealStatisticsButton.isVisible = !loading
        binding.mealCaloriesButton.isVisible = !loading
        binding.loadingMeals.isVisible = loading
    }
}