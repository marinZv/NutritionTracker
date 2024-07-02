package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.AllMealsResponse
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.AllMealsResponseShort

interface MealService {

    @GET("search.php")
    fun getAllByName(@Query("s") name: String): Observable<AllMealsResponse>

    @GET("filter.php")
    fun getAllByCategory(@Query("c") category: String): Observable<AllMealsResponseShort>

    @GET("filter.php")
    fun getAllByArea(@Query("a") area: String): Observable<AllMealsResponseShort>

    @GET("filter.php")
    fun getAllByIngredient(@Query("i") ingredient: String): Observable<AllMealsResponseShort>

}