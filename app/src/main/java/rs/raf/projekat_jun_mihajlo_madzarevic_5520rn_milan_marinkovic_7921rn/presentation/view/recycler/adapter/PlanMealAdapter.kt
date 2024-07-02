package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter

import android.media.Image
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentMealListBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments.DetailedMealFragment
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments.WeeklyPlanFragment
import java.io.File

class PlanMealAdapter(diffCallback: DiffUtil.ItemCallback<MealEntity>, private val fragment: WeeklyPlanFragment): ListAdapter<MealEntity, PlanMealAdapter.PlanMealViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanMealViewHolder {
        return PlanMealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plan_meal_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlanMealViewHolder, position: Int) {
        holder.bind(getItem(position), fragment)
    }


    class PlanMealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        fun bind(meal: MealEntity, fragment: WeeklyPlanFragment){
            itemView.findViewById<TextView>(R.id.planMealTitle).text = meal.strMeal

            if (meal.dateModified != null && meal.dateModified.startsWith("kcal")){
                if (meal.dateModified.split(" ")[1].toFloat() == 0f){
                    itemView.findViewById<TextView>(R.id.planMealCalorie).text = "kcal unavailable"
                }
                else {
                    itemView.findViewById<TextView>(R.id.planMealCalorie).text = meal.dateModified
                }
            }

            if(meal.strMealThumb.startsWith("http")){
                Picasso.get().load(meal.strMealThumb).into(itemView.findViewById(R.id.planMealImage) as ImageView)
            }else{
                Picasso.get().load(File(meal.strMealThumb)).into(itemView.findViewById(R.id.planMealImage) as ImageView)
            }

            (itemView.findViewById(R.id.addMealToDay) as ImageView).setOnClickListener{
                fragment.addMealToDay(meal)
            }

        }

    }

}