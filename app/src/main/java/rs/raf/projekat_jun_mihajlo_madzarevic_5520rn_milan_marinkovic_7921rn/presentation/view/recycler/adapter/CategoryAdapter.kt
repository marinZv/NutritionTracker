package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.activities.MainActivity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments.MealListFragment
import timber.log.Timber
import java.io.File

class CategoryAdapter(diffCallback: DiffUtil.ItemCallback<CategoryEntity>) : ListAdapter<CategoryEntity, CategoryAdapter.CategoryViewHolder>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener{
            (holder.itemView.context as MainActivity).supportFragmentManager.beginTransaction().replace((holder.itemView.context as MainActivity).binding.fragmentContainer.id , MealListFragment(holder.itemView.findViewById<TextView>(R.id.categoryTitle).text.toString())).commit()
        }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(category: CategoryEntity){
            itemView.findViewById<TextView>(R.id.categoryTitle).text = category.strCategory

            Picasso.get().load(category.strCategoryThumb).into(itemView.findViewById(R.id.categoryImage) as ImageView)

            (itemView.findViewById(R.id.categoryDescriptionImage) as ImageView).setOnClickListener {
                showDialog(itemView.context, category.strCategory, category.strCategoryDescription)
            }
        }

        fun showDialog(context: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}