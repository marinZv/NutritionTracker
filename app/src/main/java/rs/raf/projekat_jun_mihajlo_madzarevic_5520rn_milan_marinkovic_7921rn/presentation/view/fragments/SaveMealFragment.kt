package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.contract.MainContract
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentSaveMealBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.AddMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.MealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.states.UpdateSavedMealState
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.viewmodels.MealViewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class SaveMealFragment(private val meal: MealEntity, private val updateMeal: Boolean, private val calories: Float) : Fragment() {
    private lateinit var binding : FragmentSaveMealBinding

    private val mealViewModel: MainContract.MealViewModel by viewModel<MealViewModel>()

    private var dateSelected: Long = 0L
    private var imgFile: String = ""

    object BitmapUtils {
        fun saveBitmapToFile(context: Context, bitmap: Bitmap, name: String): File {
            val filesDir = context.filesDir
            val imageFile = File(filesDir, name)

            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }

            return imageFile
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveMealBinding.inflate(layoutInflater)
        activity?.title = getString(R.string.saveMeal) + " " + meal.strMeal
        if (updateMeal) {
            binding.saveMealSaveButton.text = getString(R.string.update_meal)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initObservers()
        initListeners()
        initUI()
    }

    private fun initListeners(){
        //Back button returns us to categories list
        binding.saveMealBackButton.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, DetailedMealFragment(meal, false)).commit()
        }

        binding.saveMealPic.setOnClickListener{

            val requestCode = 1
            requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCode)

            val captureImageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(captureImageIntent, requestCode)
        }

        binding.saveMealDate.setOnClickListener{
            showDatePickerDialog()
        }

        binding.saveMealSaveButton.setOnClickListener {
            var imgPath: String = meal.strMealThumb
            if (imgFile != "") imgPath = imgFile

            if (meal.strCategory != null) {
                if (!updateMeal) {
                    mealViewModel.saveMeal(
                        SavedMealEntity(
                            calories,
                            dateSelected,
                            requireActivity().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString(),
                            0,
                            meal.strMeal,
                            meal.strDrinkAlternate,
                            meal.strCategory,
                            meal.strArea,
                            meal.strInstructions,
                            imgPath,
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
                            meal.dateModified
                        )
                    )
                } else {
                    mealViewModel.updateSavedMeal(
                        SavedMealEntity(
                            calories,
                            dateSelected,
                            requireActivity().findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString(),
                            0,
                            meal.strMeal,
                            meal.strDrinkAlternate,
                            meal.strCategory,
                            meal.strArea,
                            meal.strInstructions,
                            imgPath,
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
                            meal.dateModified
                        )
                    )
                }
            }
        }
    }

    private fun initObservers(){
        mealViewModel.addMeal.observe(viewLifecycleOwner) {
            Timber.e(it.toString())
            renderState(it)
        }
        mealViewModel.updateSavedMealState.observe(viewLifecycleOwner) {
            Timber.e(it.toString())
            renderStateUpdate(it)
        }
    }

    private fun renderState(state: AddMealState) {
        when (state) {
            is AddMealState.Success -> {
                Toast.makeText(context, R.string.mealAdded, Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, DetailedMealFragment(meal, false)).commit()
            }
            is AddMealState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun renderStateUpdate(state: UpdateSavedMealState) {
        when (state) {
            is UpdateSavedMealState.Success -> {
                Toast.makeText(context, R.string.mealUpdated, Toast.LENGTH_SHORT).show()
                (context as MainActivity).supportFragmentManager.beginTransaction().replace((context as MainActivity).binding.fragmentContainer.id, DetailedMealFragment(meal, true)).commit()
            }
            is UpdateSavedMealState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initUI(){
        dateSelected = Calendar.getInstance().timeInMillis
        binding.saveMealDate.text = formatDate(Calendar.getInstance())

        Picasso.get().load(meal.strMealThumb).into(binding.saveMealPic)

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.saveMealDate.text = formatDate(selectedDate)
            dateSelected = selectedDate.timeInMillis
        }, year, month, dayOfMonth)
        datePickerDialog.show()
    }

    private fun formatDate(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            val imageUri = BitmapUtils.saveBitmapToFile(requireContext(), imageBitmap, meal.strMeal)

            imgFile = imageUri.absolutePath

            Picasso.get().load(imageUri).into(binding.saveMealPic)
        }
    }


}