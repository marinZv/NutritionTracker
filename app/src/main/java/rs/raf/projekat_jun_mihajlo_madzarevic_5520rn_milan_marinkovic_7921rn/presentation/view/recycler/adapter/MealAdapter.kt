package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentMealListBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments.DetailedMealFragment
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments.MealListFragment
import timber.log.Timber
import java.io.File

class MealAdapter(diffCallback: DiffUtil.ItemCallback<MealEntity>, private val fragment : FragmentMealListBinding?) : ListAdapter<MealEntity, MealAdapter.MealViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position), fragment)
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(meal: MealEntity, fragment : FragmentMealListBinding?){
            itemView.findViewById<TextView>(R.id.mealTitle).text = meal.strMeal

            if (meal.dateModified != null && meal.dateModified.startsWith("kcal")){
                if (meal.dateModified.split(" ")[1].toFloat() == 0f){
                    itemView.findViewById<TextView>(R.id.mealCalories).text = "kcal unavailable"
                }
                else {
                    itemView.findViewById<TextView>(R.id.mealCalories).text = meal.dateModified
                }
            }

            if (meal.strMealThumb.startsWith("http")){
                Picasso.get().load(meal.strMealThumb).into(itemView.findViewById(R.id.mealImage) as ImageView)
            }
            else{
                Picasso.get().load(File(meal.strMealThumb)).into(itemView.findViewById(R.id.mealImage) as ImageView)
            }

            var saveMeal : Boolean = false
            if (fragment != null) {
                saveMeal = fragment.mealListTabLayout.selectedTabPosition == 1
            }
            itemView.setOnClickListener{
                (itemView.context as MainActivity).supportFragmentManager.beginTransaction().replace((itemView.context as MainActivity).binding.fragmentContainer.id , DetailedMealFragment(meal, saveMeal)).commit()
            }
        }
    }
}