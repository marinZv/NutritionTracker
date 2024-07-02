package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Entity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentTabItemBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.MealAdapter
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ.MealDiffItemCallback
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel
import timber.log.Timber

class TabCategoryFragment : Fragment() {

    private lateinit var binding: FragmentTabItemBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var mealAdapter: MealAdapter

    private val mealViewModel: MainContract.MealViewModel by viewModel<MealViewModel>()

    private lateinit var allMeals: List<MealEntity>

    private lateinit var allMealsByName: List<MealEntity>

    private var countDownTimer: CountDownTimer? = null

    private var tagSearch = false

    private var mealsPerPage = 10
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabItemBinding.inflate(layoutInflater)
        recyclerView = binding.tabItemRv

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allMeals = listOf()
        init()
    }

    private fun init(){
        initReycler()
        initObservers()
        initListeners()
    }

    private fun initReycler(){
        mealAdapter = MealAdapter(MealDiffItemCallback(), null)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mealAdapter
    }

    private fun initListeners(){

//        binding.inputCriteria.doAfterTextChanged {
//            val filter = it.toString()
//            if(filter.isEmpty()){
//                mealAdapter.submitList(allMeals)
//            }else{
//                mealViewModel.getAll()
//                mealViewModel.fetchAllByCategory(filter)
//            }
//        }

        binding.inputCriteria.doAfterTextChanged { editable ->
            countDownTimer?.cancel() // Prekida prethodni tajmer ako postoji

            countDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Ne radi ništa tokom odbrojavanja
                }

                override fun onFinish() {
                    val filter = editable.toString()
                    mealViewModel.getAll()
                    mealViewModel.fetchAllByCategory(filter)
                }
            }

            countDownTimer?.start() // Pokreće tajmer za 3 sekunde
        }

        binding.inputName.doAfterTextChanged {
            val filter = it.toString()
            if(filter.isEmpty()){
                if (allMeals.size > mealsPerPage) {
                    mealAdapter.submitList(allMeals.subList(0,mealsPerPage))
                }
                else{
                    mealAdapter.submitList(allMeals)
                }
            }else{
                val filteredList = filterByName(allMeals, filter)
                if (filteredList.size > mealsPerPage) {
                    mealAdapter.submitList(filteredList.subList(0,mealsPerPage))
                }
                else{
                    mealAdapter.submitList(filteredList)
                }
            }
        }
        binding.inputTag.doAfterTextChanged {editable->

            countDownTimer?.cancel() // Prekida prethodni tajmer ako postoji

            countDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Ne radi ništa tokom odbrojavanja
                }

                override fun onFinish() {
                    val filter = editable.toString()
                    if (filter.isEmpty()) {
                        tagSearch = false
                        if (allMeals.size > mealsPerPage) {
                            mealAdapter.submitList(allMeals.subList(0,mealsPerPage))
                        }
                        else{
                            mealAdapter.submitList(allMeals)
                        }
                    } else {
                        tagSearch = true
                        for(meal in mealAdapter.currentList){
                            mealViewModel.fetchAllByNameForTag(meal.strMeal)
                        }
                        mealViewModel.getAllByTag(filter)
                    }
                }
            }

            countDownTimer?.start() // Pokreće tajmer za 3 sekunde

        }

        binding.sortBtn.setOnClickListener {
            var sortedMeals : List<MealEntity>
            sortedMeals = sortMealsByName(allMeals)
            if (sortedMeals.size > mealsPerPage) {
                mealAdapter.submitList(sortedMeals.subList(0,mealsPerPage))
            }
            else{
                mealAdapter.submitList(sortedMeals)
            }
        }


        //Load previous 10 meals
        binding.filterBackwardPagination.setOnClickListener{
            if ((currentPage-1) >= 0){
                currentPage -= 1
                mealAdapter.submitList(allMeals.subList(currentPage * mealsPerPage, (currentPage+1) * mealsPerPage))
            }
        }

        //Load next 10 meals
        binding.filterForwardPagination.setOnClickListener{
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

    private fun initObservers(){
        mealViewModel.mealState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
    }

    private fun renderState(state: MealState){
        when(state){
            is MealState.Success -> {
                showLoadingState(false)
                if (tagSearch && state.meals[0].idMeal == "-1")
                {
                    val list = state.meals.subList(1,state.meals.size)
                    if (list.size > mealsPerPage) {
                        mealAdapter.submitList(list.subList(0,mealsPerPage))
                    }
                    else{
                        mealAdapter.submitList(list)
                    }
                }
                else if (!tagSearch)
                {
                    allMeals = state.meals
                    if (state.meals.size > mealsPerPage) {
                        mealAdapter.submitList(state.meals.subList(0,mealsPerPage))
                    }
                    else{
                        mealAdapter.submitList(state.meals)
                    }
                }
            }
            is MealState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun showLoadingState(loading: Boolean){
        binding.tabItemRv.isVisible = !loading
        binding.loadingTabMeals.isVisible = loading
    }

    private fun sortMealsByName(meals: List<MealEntity>): List<MealEntity>{
        val sortedList = meals.toMutableList() // Stvaranje nove liste s istim vrijednostima
        sortedList.sortBy { it.strMeal } // Sortiranje nove liste po imenu
        return sortedList
    }

    private fun filterByName(meals: List<MealEntity>, name: String): List<MealEntity>{
        val filteredList = mutableListOf<MealEntity>()

        for(meal in meals){
            if(meal.strMeal.contains(name, ignoreCase = true)){
                filteredList.add(meal)
            }
        }

        return filteredList
    }
}