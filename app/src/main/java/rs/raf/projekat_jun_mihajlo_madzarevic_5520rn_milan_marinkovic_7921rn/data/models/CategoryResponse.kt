package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse (
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)