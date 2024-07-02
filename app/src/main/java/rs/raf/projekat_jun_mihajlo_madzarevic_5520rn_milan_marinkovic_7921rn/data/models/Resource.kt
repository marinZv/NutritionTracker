package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val message: String = "") : Resource<T>()
    data class Error<out T>(val error: Throwable = Throwable(), val data: T? = null): Resource<T>()
}