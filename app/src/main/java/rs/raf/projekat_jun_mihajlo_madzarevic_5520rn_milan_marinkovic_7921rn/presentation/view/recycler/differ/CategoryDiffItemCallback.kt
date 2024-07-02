package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.differ

import androidx.recyclerview.widget.DiffUtil
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity

class CategoryDiffItemCallback : DiffUtil.ItemCallback<CategoryEntity>() {
    override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
        return oldItem.idCategory == newItem.idCategory
                && oldItem.strCategory == newItem.strCategory
                && oldItem.strCategoryThumb == newItem.strCategoryThumb
                && oldItem.strCategoryDescription == newItem.strCategoryDescription
    }
}