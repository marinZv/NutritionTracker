package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentPlanBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.MealAdapter
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.PlanMealAdapter
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ.MealDiffItemCallback
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteCalorieMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.DeleteIngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.IngredientState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel
import timber.log.Timber

class WeeklyPlanFragment: Fragment() {

    private lateinit var binding: FragmentPlanBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var mealAdapter: PlanMealAdapter

    private val mealViewModel: MainContract.MealViewModel by viewModel<MealViewModel>()

    //for kcal
    private var calorieCounter = 0
    var allMealsFull: MutableList<CalorieMealEntity> = mutableListOf()

    private lateinit var allMeals: List<MealEntity>
    private var countDownTimer: CountDownTimer? = null

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    private var mealsPerDay: List<MealEntity> = mutableListOf()

    private var dayCounter: Int = 1

    private val hashMap: HashMap<Int, List<MealEntity>> = HashMap()

    init{
        for(i in 1..7){
            hashMap[i] = mutableListOf()
        }
    }

    fun addMealToDay(meal: MealEntity) {
        if (meal.dateModified != null && meal.dateModified.split(" ")[1].toFloat() == 0f){
            Toast.makeText(context, "Meal couldn't be added, no calorie data found!", Toast.LENGTH_LONG).show()
        }
        else {
            if (!mealsPerDay.contains(meal)) {
                mealsPerDay = mealsPerDay + meal
            }
            Toast.makeText(context, "Meal added to plan", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanBinding.inflate(layoutInflater)
        recyclerView = binding.weeklyPlanRv

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initUI()
        initRecycler()
        initObservers()
        initListeners()
    }

    private fun initUI(){
        binding.dayTv.text = daysOfWeek.get(dayCounter-1)
        binding.weeklyPlanEmail.isEnabled = false;
        binding.sendPlanBtn.isEnabled = false
    }

    private fun initRecycler(){
        mealAdapter = PlanMealAdapter(MealDiffItemCallback(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mealAdapter
    }

    private fun initListeners(){
        binding.weeklyPlanInputCategory.doAfterTextChanged { editable ->
            countDownTimer?.cancel()

            countDownTimer = object: CountDownTimer(2000, 1000){
                override fun onTick(millisUntilFinished: Long) {
                    //Ne radi nista
                }

                override fun onFinish() {
                    val filter = editable.toString()
                    if(filter.isEmpty()){
                        mealAdapter.submitList(listOf())
                    }else{
                        mealViewModel.getAll()
                        mealViewModel.fetchAllByName(filter)
                    }
                }
            }
            countDownTimer?.start()
        }

        binding.nextDayBtn.setOnClickListener {

            hashMap[dayCounter] = mealsPerDay

            if(dayCounter >= 1 && dayCounter <= 7){
                Toast.makeText(context,"You added meals for ${daysOfWeek.get(dayCounter-1)}", Toast.LENGTH_SHORT).show()
            }

            mealsPerDay = mutableListOf()

            dayCounter++

            if(dayCounter >= 1 && dayCounter <= 7){
                binding.dayTv.text = daysOfWeek.get(dayCounter-1)
            }

            if(dayCounter == 8){
                binding.nextDayBtn.isEnabled = false
                Toast.makeText(context,"Now you can enter the email and click send button to send plan", Toast.LENGTH_SHORT).show()
                binding.weeklyPlanEmail.isEnabled = true
                binding.sendPlanBtn.isEnabled = true
                dayCounter = 1
            }

        }
        binding.sendPlanBtn.setOnClickListener {

            val stringBuilder = StringBuilder()

            if(binding.weeklyPlanEmail.toString().isEmpty()){
               Toast.makeText(context, "You have to enter email before click on Send button", Toast.LENGTH_SHORT).show()
            }else{
                var day: String
                hashMap.forEach{(key, value)->
                    day = daysOfWeek[key-1]
                    stringBuilder.append(day)
                    stringBuilder.append(":")
                    stringBuilder.append("\n")
                    value.forEach{meal ->
                        stringBuilder.append(meal.strMeal + " " + meal.dateModified)
                        stringBuilder.append("\n")
                    }
                    stringBuilder.append("\n\n")
                }

                stringBuilder.append("You can open application by clicking on following link: ")
                stringBuilder.append("<a href='https://www.nutrition_tracker.rs'>https://www.nutrition_tracker.rs</a>")

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type = "text/plain"

                var email: String = binding.weeklyPlanEmail.toString().trim()

                emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Weekly meal plan")
                emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())

                try {
                    //start email intent
                    startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."))
                }
                catch (e: Exception){
                    //if any thing goes wrong for example no email client application or any exception
                    //get and show exception message
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }

                Timber.e(stringBuilder.toString())

                mealsPerDay = mutableListOf()
                for(i in 1..7){
                    hashMap[i] = mutableListOf()
                }

                binding.nextDayBtn.isEnabled = true
                Toast.makeText(context, "You created weekly meal plan succesfully!", Toast.LENGTH_SHORT).show()
                binding.sendPlanBtn.isEnabled = false
                binding.weeklyPlanEmail.text.clear()
                binding.weeklyPlanEmail.isEnabled = false
            }

        }

    }

    private fun initObservers(){
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
    }

    private fun renderState(state: MealState){
        when(state){
            is MealState.Success -> {
                allMeals = state.meals
                mealAdapter.submitList(state.meals)

                calorieCounter = allMeals.size

                allMealsFull.clear()
                mealViewModel.deleteAllCalorie()
            }
            is MealState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.DataFetched -> {
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealState.Loading -> {

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
                showLoadingState(false)
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
                showLoadingState(false)
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
                    mealAdapter.submitList(allMeals)
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

    private fun showLoadingState(loading: Boolean) {
        binding.weeklyPlanRv.isVisible = !loading
        binding.dayTv.isVisible = !loading
        binding.weeklyPlanEmail.isVisible = !loading
        binding.nextDayBtn.isVisible = !loading
        binding.sendPlanBtn.isVisible = !loading
        binding.weeklyPlanInputCategory.isVisible = !loading
        binding.loadingPlan.isVisible = loading
    }
}