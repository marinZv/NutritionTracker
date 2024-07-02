package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.NinjaMealResponse

interface NinjaMealService {

    @Headers("X-Api-Key: jfzPUX0pkhrxvppBbbckcw==rsaChQvyV0MZ5C1x")
    @GET("https://api.api-ninjas.com/v1/nutrition")
    fun getAllByTitle(@Query("query") title: String): Observable<List<NinjaMealResponse>>
}