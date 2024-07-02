package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentCalorieCalculatorBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity


class CalorieCalculatorFragment(private val category: String) : Fragment() {
    private lateinit var binding : FragmentCalorieCalculatorBinding
    private val sharedPreferences: SharedPreferences by inject()

    private var sexSelected = 0
    private var activitySelected = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalorieCalculatorBinding.inflate(layoutInflater)
        activity?.title = getString(R.string.calorieCalculator)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initUi()
        initListeners()
    }

    private fun initUi(){
        val adapter1: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf(getString(R.string.male),getString(R.string.female)))
        binding.calorieCalculatorSex.adapter = adapter1
        binding.calorieCalculatorSex.setSelection(0)

        val adapter2: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf(getString(R.string.inactive),getString(R.string.mediumActive),getString(R.string.highlyActive)))
        binding.calorieCalculatorLevelOfActivity.adapter = adapter2
        binding.calorieCalculatorLevelOfActivity.setSelection(0)
    }

    private fun initListeners() {
        binding.calorieCalculatorSubmit.setOnClickListener {
            try {
                val age = binding.calorieCalculatorAge.text.toString().toInt()
                val weight = binding.calorieCalculatorWeight.text.toString().toInt()
                val height = binding.calorieCalculatorHeight.text.toString().toInt()

                var caloriesDaily = 0.0

                if (sexSelected == 0){
                    caloriesDaily = (10 * weight) + (6.25 * height) - (5 * age) + 5

                    when (activitySelected){
                        0 -> {caloriesDaily *= 1.2f}
                        1 -> {caloriesDaily *= 1.55f}
                        2 -> {caloriesDaily *= 1.9f}
                    }
                }
                else{
                    caloriesDaily = (10 * weight)  + (6.25 * height) - (5 * age) - 161

                    when (activitySelected){
                        0 -> {caloriesDaily *= 1.2f}
                        1 -> {caloriesDaily *= 1.375f}
                        2 -> {caloriesDaily *= 1.55f}
                    }
                }

                sharedPreferences.edit().putFloat("kcalDaily", caloriesDaily.toFloat()).apply()
                Toast.makeText(context, getString(R.string.yourDailyIntake) + " " + caloriesDaily + " kcal", Toast.LENGTH_LONG).show()
                (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, StatisticsFragment(category)).commit()

            } catch (e: NumberFormatException) {
                Toast.makeText(context, getString(R.string.error_whole_numbers), Toast.LENGTH_LONG).show()
            }
        }

        binding.calorieCalculatorSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sexSelected = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.calorieCalculatorLevelOfActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activitySelected = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.calorieCalculatorBackButton.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, StatisticsFragment(category)).commit()
        }
    }
}