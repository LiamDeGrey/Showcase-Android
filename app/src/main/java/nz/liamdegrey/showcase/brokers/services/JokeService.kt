package nz.liamdegrey.showcase.brokers.services

import io.reactivex.Single
import nz.liamdegrey.showcase.models.JokeHolder
import nz.liamdegrey.showcase.models.JokesHolder
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface JokeService {

    @GET("/jokes/random")
    @Headers("Accept: application/json")
    fun getRandomJoke(@Query("exclude") exclusions: String = "[explicit]"): Single<JokeHolder>

    @GET("/jokes/random/{AMOUNT}")
    @Headers("Accept: application/json")
    fun getRandomJokes(@Path("AMOUNT") amount: Int, @Query("exclude") exclusions: String = "[explicit]"): Single<JokesHolder>
}
