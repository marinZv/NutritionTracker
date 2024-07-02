package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.database.NutritionDatabase
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.AllCategoriesResponse
import java.util.*
import java.util.concurrent.TimeUnit

val coreModule = module {
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(androidApplication().packageName, Context.MODE_PRIVATE)
    }

    single { Room.databaseBuilder(androidContext(), NutritionDatabase::class.java, "NutritionDB")
        .fallbackToDestructiveMigration()
        .build() }

    single { createRetrofit(moshi = get(), httpClient = get()) }

//    single { createNinjaRetrofit(httpClient = get())}

    single { createMoshi() }

    single { createOkHttpClient() }

}

fun createMoshi(): Moshi {
    return Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()
}

fun createRetrofit(moshi: Moshi,
                   httpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
}

//fun createNinjaRetrofit(httpClient: OkHttpClient
//): Retrofit {
//    return Retrofit.Builder()
//        .baseUrl("https://api.api-ninjas.com/v1/")
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient)
//        .build()
//}

fun createOkHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.readTimeout(60, TimeUnit.SECONDS)
    httpClient.connectTimeout(60, TimeUnit.SECONDS)
    httpClient.writeTimeout(60, TimeUnit.SECONDS)

//    httpClient.addInterceptor { chain ->
//        val originalRequest = chain.request()
//        val modifiedRequest = originalRequest.newBuilder()
//            .header("X-Api-Key", "jfzPUX0pkhrxvppBbbckcw==rsaChQvyV0MZ5C1x")
//            .build()
//        chain.proceed(modifiedRequest)
//    }

    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
    }

    return httpClient.build()
}

// Metoda koja kreira servis
inline fun <reified T> create(retrofit: Retrofit): T  {
    return retrofit.create(T::class.java)
}