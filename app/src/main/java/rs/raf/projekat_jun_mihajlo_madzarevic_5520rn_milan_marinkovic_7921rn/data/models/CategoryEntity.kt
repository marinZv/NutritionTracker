package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = false)
    var idCategory: Int,
    var strCategory: String,
    var strCategoryThumb: String,
    var strCategoryDescription: String
)