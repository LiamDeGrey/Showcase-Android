package nz.liamdegrey.showcase.brokers.services

import io.reactivex.Single
import nz.liamdegrey.showcase.models.JokeHolder
import retrofit2.http.GET
import retrofit2.http.Headers


interface JokeService {

    @GET("/jokes/random")
    @Headers("Accept: application/json")
    fun getRandomJoke(): Single<JokeHolder>
}
