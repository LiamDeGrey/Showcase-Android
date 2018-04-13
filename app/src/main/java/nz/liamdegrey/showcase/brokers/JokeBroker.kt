package nz.liamdegrey.showcase.brokers

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.brokers.services.JokeService
import nz.liamdegrey.showcase.models.JokesHolder
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class JokeBroker(endpoint: String, okHttpClient: OkHttpClient, converterFactory: Converter.Factory) {
    private val service: JokeService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(endpoint)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        service = retrofit.create(JokeService::class.java)
    }

    fun getRandomJokes(amount: Int): Single<JokesHolder> = service.getRandomJokes(amount)

    fun searchForJokes(searchTerm: String): Single<JokesHolder> = service.searchForJokes(searchTerm)
}