package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentHomeBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.CategoryAdapter
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ.CategoryDiffItemCallback
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.CategoryState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.CategoryViewModel
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel
import timber.log.Timber

//Fragment showing categories of meals
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView

    private val categoryViewModel: MainContract.CategoryViewModel by viewModel<CategoryViewModel>()
    private val mealViewModel: MainContract.MealViewModel by viewModel<MealViewModel>()

    private lateinit var allCategories: List<CategoryEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        recyclerView = binding.categoriesRV
        activity?.title = getString(R.string.categories)

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
        categoryAdapter = CategoryAdapter(CategoryDiffItemCallback())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categoryAdapter
    }

    private fun initListeners() {
        binding.searchCategoryList.doAfterTextChanged {
            val filter = it.toString()
            if (filter.isEmpty()){
                categoryAdapter.submitList(allCategories)
            }
            else{
                mealViewModel.getAll()
                mealViewModel.getAllByNameSearch(filter)
            }
        }
        binding.filterBtn.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace((requireActivity() as MainActivity).binding.fragmentContainer.id , TabMainFragment()).commit()
        }
        binding.planBtn.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction().replace((requireActivity() as MainActivity).binding.fragmentContainer.id , WeeklyPlanFragment()).commit()
        }
    }

    private fun initObservers() {
        categoryViewModel.categoryState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderState(it)
        })
        mealViewModel.mealState.observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
            renderSearchState(it)
        })
        categoryViewModel.getAll()
        categoryViewModel.fetchAll()
    }

    private fun renderState(state: CategoryState) {
        when (state) {
            is CategoryState.Success -> {
                showLoadingState(false)
                allCategories = state.categories
                categoryAdapter.submitList(state.categories)
            }
            is CategoryState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is CategoryState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is CategoryState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun renderSearchState(state: MealState) {
        when (state) {
            is MealState.Success -> {
                showLoadingStateMeals(false)

                val categoriesWithMeal: List<String> = allMealCategories(state.meals)
                val listToSubmit: MutableList<CategoryEntity> = mutableListOf()

                for (category in allCategories){
                    if (categoriesWithMeal.contains(category.strCategory.lowercase())){
                        listToSubmit.add(category)
                    }
                }

                categoryAdapter.submitList(listToSubmit)
            }
            is MealState.Error -> {
                showLoadingStateMeals(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealState.DataFetched -> {
                showLoadingStateMeals(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealState.Loading -> {
                showLoadingStateMeals(true)
            }
        }
    }

    private fun allMealCategories(meals: List<MealEntity>): List<String>{
        var categories: MutableList<String> = mutableListOf()

        for (meal in meals){
            if (!categories.contains(meal.strCategory)) {
                meal.strCategory?.let { categories.add(it.lowercase()) }
            }
        }

        return categories
    }

    private fun showLoadingState(loading: Boolean) {
        binding.searchCategoryList.isVisible = !loading
        binding.categoriesRV.isVisible = !loading
        binding.loadingCategories.isVisible = loading
    }

    private fun showLoadingStateMeals(loading: Boolean) {
        binding.categoriesRV.isVisible = !loading
        binding.loadingCategories.isVisible = loading
    }

}