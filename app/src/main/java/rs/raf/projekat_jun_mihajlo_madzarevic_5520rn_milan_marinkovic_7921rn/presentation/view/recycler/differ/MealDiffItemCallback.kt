package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ

import androidx.recyclerview.widget.DiffUtil
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity

class MealDiffItemCallback : DiffUtil.ItemCallback<MealEntity>() {

    override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
        return oldItem.idMeal == newItem.idMeal
                && oldItem.strMeal == newItem.strMeal
                && oldItem.strCategory == newItem.strCategory
                && oldItem.strMealThumb == newItem.strMealThumb
    }
}