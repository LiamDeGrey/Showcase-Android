package nz.liamdegrey.showcase.brokers.services

import io.reactivex.Single
import nz.liamdegrey.showcase.models.JokesHolder
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface JokeService {

    @GET("/search")
    @Headers("Accept: application/json")
    fun getRandomJokes(@Query("limit") amount: Int, @Query("page") page: Int = 1): Single<JokesHolder>

    @GET("/search")
    @Headers("Accept: application/json")
    fun searchForJokes(@Query("term") term: String, @Query("limit") amount: Int = 30, @Query("page") page: Int = 1): Single<JokesHolder>
}
